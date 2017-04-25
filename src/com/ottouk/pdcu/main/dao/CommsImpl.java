package com.ottouk.pdcu.main.dao;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import com.ottouk.pdcu.main.utils.StringUtils;

public class CommsImpl implements Comms {
	
	private static final int MAX_RECONNECTS = 2; // maximum reconnects
	private static final int MAX_ATTEMPTS = 15; // maximum attempts to get response before reconnecting
	private static final int DELAY = 200; // milliseconds delay between attempts
	
	private String server;
	private int basePort;
	private int channels;
	private Integer unitId;
	private int port;
	private Socket socket;
	private PrintWriter output;
	private BufferedReader input;
	private String errorMessage;
	private String response;
	
	private boolean connectedOK;

	
	public boolean connect(String server, int basePort, int channels, Integer unitId) {
		
		this.channels = channels;
		this.basePort = basePort;
		this.server = server;
		this.unitId = (unitId == null ? new Integer(0) : unitId);
		this.port = basePort + (this.unitId.intValue() % channels);
		
		return connect();
	}
	
	private boolean connect() {
		
		errorMessage = "";
		connectedOK = true;

		try {
			socket = new Socket(server, port);
			socket.setSoTimeout(MAX_RECONNECTS * MAX_ATTEMPTS * DELAY);
			output = new PrintWriter(socket.getOutputStream(), true); // Enable flushing
			input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			errorMessage = e.getMessage();
			StringUtils.log("I/O error: " + toString() + " " + errorMessage);
			connectedOK = false;
		}

		if (connectedOK) {
			StringUtils.log("Connected to " + toString());
		} else {
			StringUtils.log("Failed to connect to " + toString());
		}
		
		return connectedOK;
	}

	public boolean disconnect() {
		
		errorMessage = "";
		connectedOK = false;
		
		try {
			/*output.close();
			input.close();*/
			socket.close();
			/*if (!socket.isClosed()) {
				throw new IOException("Socket still open");
			}*/
		} catch (IOException e) {
			errorMessage = e.getMessage();
			StringUtils.log("Close error: " + toString() + " " + errorMessage);
			connectedOK = true;
		}

		if (!connectedOK) {
			StringUtils.log("Disconnected from " + toString());
		} else {
			StringUtils.log("Failed to disconnect from " + toString());
		}

		return !connectedOK;
	}

	public boolean transact(String message) {

		errorMessage = "";
		response = "";
		boolean transactedOK = false;

		try {

			for (int reconnect = 0; reconnect < MAX_RECONNECTS; reconnect++) {

				// Check connected
				if (!connectedOK) {
					StringUtils.log("Establishing new connection...");
					if (!connect()) {
						continue;
					}
				}
				
				// Transact message
				output.println(message);
				if (output.checkError()) {
					throw new IOException("Write error!");
				}
				
				// Await response
				for (int attempt = 0; (attempt < MAX_ATTEMPTS) && !input.ready(); attempt++) {
					try {
						Thread.sleep(DELAY);
						/*StringUtils.log("Time out attempt " + attempt);*/
					} catch (InterruptedException e) {
						// Thread interrupted - ignore exception
					}
				}

				// Read response
				if (input.ready()) {
					response = input.readLine();
					transactedOK = true;
					break;
				} else {
					StringUtils.log("Time out.  Dropping connection...");
					disconnect();
				}

			}

		} catch (IOException e) {
			errorMessage = e.getMessage();
			response = "";
			StringUtils.log("I/O failure: " + toString() + " " + errorMessage);
			
			// Disconnect to force re-connect on next transact()
			StringUtils.log("Dropping connection...");
			disconnect();
		}

		if (transactedOK) {
			StringUtils.log("Comms request : " + message);
			StringUtils.log("Comms response: " + response);
		} else {
			StringUtils.log("Failed to transact message: " + message);
		}

		return transactedOK;
	}
	
	public boolean responseStartsWith(String pattern) {
		if (response != null && response.length() >= pattern.length()) {
			return (response.startsWith(pattern));
		} else {
			return false;
		}
	}
	
	public String getServer() {
		return server;
	}

	public int getBasePort() {
		return basePort;
	}

	public int getChannels() {
		return channels;
	}

	public Integer getUnitId() {
		return unitId;
	}

	public int getPort() {
		return port;
	}

	public Socket getSocket() {
		return socket;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public String getResponse() {
		return response;
	}

	public String toString() {
		return (server + ":" + port);
	}

}

package com.ottouk.pdcu.main.dao;

import java.net.Socket;

public interface Comms {
	
	boolean connect(String server, int basePort, int channels, Integer unitId);
	
	boolean transact(String message);
	
	boolean disconnect();
	
	
	boolean responseStartsWith(String pattern);
	
	String getServer();

	int getBasePort();

	int getChannels();

	Integer getUnitId();

	int getPort();

	Socket getSocket();

	String getErrorMessage();

	String getResponse();

}

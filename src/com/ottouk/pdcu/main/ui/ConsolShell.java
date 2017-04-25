package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Consol UI.
 * 
 * @author dis065
 * 
 */
public class ConsolShell extends GeneralShell {

	private Text text;
	private Button bOK;
	private Button bEscape;
	
	
	
	public boolean initialise(String[] args) {
		StringUtils.log("ConsolShell" + args);
		
		return true;
	}
	
	public String setTitle() {
		return "Consol";
	}
	
	public void handleListenerEvent(Event event) {
		if (event.widget == bOK) {
			// Submit...
			StringUtils.log(text.getText());
			text.setText("");
			text.setFocus();
		} else if (event.widget == bEscape) {
			// Quit
			shellClose();
		}
	}

	public void createContents() {
		
		text = newText(shell, 10);
		bOK = newButton(shell, "OK");
		bEscape = newButton(shell, "Escape");
		
		// Set focus and default button
		text.setFocus();
	    shell.setDefaultButton(bOK);
	}

}

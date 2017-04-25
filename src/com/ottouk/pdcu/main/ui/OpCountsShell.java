package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;

import com.ottouk.pdcu.main.service.OpCountsService;
import com.ottouk.pdcu.main.service.OpCountsServiceImpl;

public class OpCountsShell extends GeneralShell {

	private OpCountsService opCountsService;
	private Button bOK;
	
	private static final String OP_COUNTS_TITLE = "Operator Counts";
	
	
	public boolean initialise(String[] args) {

		opCountsService = new OpCountsServiceImpl();
		
		while (true) {
			if (opCountsService.requestOpCounts()) {
				return true;
			} else {
				if (questionBox(OP_COUNTS_TITLE,
						"Unable to obtain " + OP_COUNTS_TITLE + ".\nRetry?") == SWT.NO) {
					// Abort
					return false;
				} else {
					showWaitCursor();
				}
			}
		}

	}
	
	public String setTitle() {
		return OP_COUNTS_TITLE;
	}
	
	public void handleListenerEvent(Event event) {
		if (event.widget == bOK) {
			// Quit
			shellClose();
		}
	}
	
	public void createContents() {
		
		// Create a two column composite with a 70/30 split (approx.)
		Composite composite = new Composite(shell, SWT.NONE);
		GridLayout compositeGridLayout = new GridLayout();
		compositeGridLayout.numColumns = 2;
		compositeGridLayout.makeColumnsEqualWidth = false;
		composite.setLayout(compositeGridLayout);
		
		GridData nameLabelData = new GridData(GridData.FILL_HORIZONTAL);
		nameLabelData.horizontalSpan = 1;
		nameLabelData.widthHint = (int) (shell.getClientArea().width * 0.7);
		
		GridData countLabelData = new GridData(GridData.FILL_HORIZONTAL);
		countLabelData.horizontalSpan = 1;
		/*countLabelData.widthHint = (int) (shell.getClientArea().width * 0.3);*/
		
		// Add operator statistics
		newLabel(composite, "Build Totes", nameLabelData);
		newLabel(composite, opCountsService.getBuilt(), countLabelData);

		newLabel(composite, "Build Items", nameLabelData);
		newLabel(composite, opCountsService.getBuild(), countLabelData);
		
		newLabel(composite, "Putaway Totes", nameLabelData);
		newLabel(composite, opCountsService.getPutaway(), countLabelData);

		newLabel(composite, "Putaway Items", nameLabelData);
		newLabel(composite, opCountsService.getTotePutawayItems(), countLabelData);

		newLabel(composite, "Consol Items", nameLabelData);
		newLabel(composite, opCountsService.getConsolItems(), countLabelData);

		newLabel(composite, "PI Totes", nameLabelData);
		newLabel(composite, opCountsService.getPILocations(), countLabelData);

		newLabel(composite, "PI Items", nameLabelData);
		newLabel(composite, opCountsService.getPIItems(), countLabelData);
		
		
		// Add button to shell and set focus
		bOK = newButton(shell, "OK");
	    shell.setDefaultButton(bOK);
	}
	
}

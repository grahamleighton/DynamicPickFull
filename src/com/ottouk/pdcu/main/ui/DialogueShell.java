package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlAdapter;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;

import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Abstract message shell class with helper methods for concrete
 * implementations.
 * 
 * @author dis065
 * 
 */
public abstract class DialogueShell extends GeneralShell {

	/**
	 * Dialogue shell font.
	 */
	private static final Font DIALOGUE_FONT = new Font(DISPLAY, "Arial", 16, SWT.BOLD | SWT.ITALIC);

	/**
	 * Title.
	 */
	private String text;
	
	/**
	 * Question/Error message.
	 */
	private String message;
	
	/**
	 * Calling shell battery meter.
	 */
	private ProgressBar saveBatteryMeter;
	
	/**
	 * Calling shell battery label.
	 */
	private Label saveBatteryLabel;
	
	/**
	 * Value of event.widget (button) selected.
	 */
	private int returnCode;
	
	/**
	 * Default button - does nothing except keep focus.
	 */
	private Button bDummy;
	
	/**
	 * Button. 
	 */
	protected Button bOK;
	
	/**
	 * Button.
	 */
	protected Button bYes;
	
	/**
	 * Button.
	 */
	protected Button bNo;
	
	
	/**
	 * Default constructor.
	 */
	public DialogueShell() {
		super();
	}
	
	/**
	 * Alternate constructor.
	 * 
	 * @param args
	 *            (to be passed to initialise() method)
	 */
	public DialogueShell(String[] args) {
		super(args, SWT.ON_TOP);
	}
	
	/**
	 * Initialise shell.
	 * 
	 * @param args
	 *            from constructor
	 * @return boolean
	 */
	public boolean initialise(String[] args) {
		
		// Initialise
		this.text = args[0];	//title
		this.message = args[1];	//message
		
		// Preserve calling shell and disable
		saveBatteryMeter = getBatteryMeter();
		saveBatteryLabel = getBatteryLabel();
		for (int i = 0; i < DISPLAY.getShells().length; i++) {
			if (shell.handle != DISPLAY.getShells()[i].handle) {
				DISPLAY.getShells()[i].setEnabled(false);
			}
		}
		
		// Open dialogue shell
		beep();
		return true;
	}
	
	/**
	 * Set shell title.
	 * 
	 * @return shell title
	 */
	public String setTitle() {
		return text;
	}
	
	/**
	 * Handle listener event.
	 * 
	 * @param event listener event
	 */
	public void handleListenerEvent(Event event) {
		if (event.widget == bOK) {
			returnCode = SWT.OK;
			escape();
		} else if (event.widget == bYes) {
			returnCode = SWT.YES;
			escape();
		} else if (event.widget == bNo) {
			returnCode = SWT.NO;
			escape();
		} else if (event.widget == bDummy) {
			beep();
		}
	}
	
	/**
	 * Create shell contents.
	 */
	public void createContents() {
		
		Composite composite = new Composite(shell, SWT.NONE);
		
		final GridLayout compositeGridLayout = new GridLayout();
		compositeGridLayout.numColumns = 2;
		composite.setLayout(compositeGridLayout);
		
		/*bDummy = newButton(composite, "");
		bDummy.setImage(shell.getDisplay().getSystemImage(SWT.ICON_ERROR));*/
		
		bDummy = newButton(composite, "!");
		bDummy.forceFocus();
		
		final GridData labelData = new GridData(GridData.FILL_HORIZONTAL);
	    labelData.horizontalSpan = 1;
	    
	    newLabel(composite, message, DIALOGUE_FONT, labelData, SWT.WRAP);
		
		// Need to provide widthhint for swt to wrap label text correctly
		shell.addControlListener(new ControlAdapter() {
			public void controlResized(ControlEvent e) {
				
				// take total screen width
				labelData.widthHint = shell.getClientArea().width

				// subtract margins (left and right)
				- (2 * compositeGridLayout.numColumns * compositeGridLayout.marginWidth)
				
				// subtract column spacing
				- ((compositeGridLayout.numColumns - 1) * compositeGridLayout.horizontalSpacing)
				
				// subtract width of button (column 1)
				- bDummy.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).x;
				
				// leaves width available for label (column 2)
				
				
				// Call layout on parent
				shell.layout(true);
			}
		});
	    
		shell = addButtonsToShell(shell);
	}
	
	/**
	 * Exit shell.
	 */
	private void escape() {
		
		// Log dialogue
		String log = text 
			+ ": " 
			+ StringUtils.replace(message, "\n", "  ")
			+ " -> ";
		
		switch (returnCode) {
		case SWT.OK:
			log += "OK";
			break;
		case SWT.YES:
			log += "YES";
			break;
		case SWT.NO:
			log += "NO";
			break;
		default:
			log += returnCode;
			break;
		}
		StringUtils.log(log);
		
		// Restore calling shell and enable
		setBatteryMeter(saveBatteryMeter);
		setBatteryLabel(saveBatteryLabel);
		for (int i = 0; i < DISPLAY.getShells().length; i++) {
			DISPLAY.getShells()[i].setEnabled(true);
		}
		
		// Pass back returnCode and close dialogue shell. 
		setReturnCode(returnCode);
		shellClose(false);
	}
	
	/**
	 * Add button widgets to shell.
	 * 
	 * @param shell shell
	 * @return shell populated with button widgets
	 */
	protected abstract Shell addButtonsToShell(Shell shell);
	
}

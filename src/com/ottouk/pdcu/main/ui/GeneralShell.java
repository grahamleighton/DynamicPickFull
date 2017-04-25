package com.ottouk.pdcu.main.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ProgressBar;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import com.ottouk.pdcu.main.domain.BatteryStatus;
import com.ottouk.pdcu.main.service.BatteryService;
import com.ottouk.pdcu.main.service.BatteryServiceImpl;
import com.ottouk.pdcu.main.service.LogonService;
import com.ottouk.pdcu.main.utils.StringUtils;

/**
 * Abstract shell class with event loop and helper methods for concrete
 * implementations.
 * 
 * @author dis065
 * 
 */
public abstract class GeneralShell extends GeneralDisplay {

	/**
	 * Default label font. 
	 */
	private static final Font DEFAULT_LABEL_FONT = new Font(DISPLAY, "Arial", 14, SWT.BOLD);
	
	/**
	 * Footer font.
	 */
	private static final Font FOOTER_LABEL_FONT = new Font(DISPLAY, "Arial", 10, SWT.BOLD);
	
	/**
	 * Button font.
	 */
	private static final Font BUTTON_FONT = new Font(DISPLAY, "Tahoma", 14, SWT.BOLD);
	
	/**
	 * Text font.
	 */
	private static final Font TEXT_FONT = new Font(DISPLAY, "Tahoma", 14, SWT.NORMAL);

	/**
	 * Default label style.
	 */
	private static final int DEFAULT_LABEL_STYLE = SWT.CENTER;
	
	/**
	 * Footer label layout data.
	 */
	private static final GridData FOOTER_LABEL_LAYOUT_DATA = new GridData(GridData.HORIZONTAL_ALIGN_BEGINNING);
	
	/**
	 * Default layout data. 
	 */
	private static final GridData DEFAULT_LAYOUT_DATA = defaultLayoutData();
	
	/**
	 * Default layout data for footer. 
	 */
	private static final GridData FOOTER_LAYOUT_DATA = footerLayoutData();
	
	/**
	 * Battery meter layout data.
	 */
	private static final GridData BATTERY_METER_LAYOUT_DATA = new GridData(GridData.FILL_HORIZONTAL);
	
	/**
	 * Default layout.
	 */
	private static final GridLayout DEFAULT_LAYOUT = defaultLayout();
	
	/**
	 * Footer layout.
	 */
	private static final GridLayout FOOTER_LAYOUT = footerLayout();

	/**
	 * Battery service.
	 */
	private static final BatteryService BATTERY_SERVICE = new BatteryServiceImpl();
	
	/**
	 * High battery label text. 
	 */
	private static final String BATTERY_TEXT_HIGH = "Battery OK ";
	
	/**
	 * Low battery label text.
	 */
	private static final String BATTERY_TEXT_LOW = "Battery Low";
	
	/**
	 * Battery meter label text.
	 */
	private static String batteryLabelText = BATTERY_TEXT_HIGH;
	
	/**
	 * Battery meter.
	 */
	private static ProgressBar batteryMeter;
	
	/**
	 * Battery meter label.
	 */
	private static Label batteryLabel;
	
	/**
	 * Set by DialogueShell on exit.
	 */
	private static int returnCode;

	/**
	 * Once set, this service and it's methods are available to all other
	 * implementations of this class.
	 */
	protected static LogonService logonService;

	/**
	 * Shell.
	 */
	protected Shell shell;

	/**
	 * Shell title.
	 */
	private Label title;
	
	/**
	 * Default shell listener.
	 */
	private Listener listener;
	
	
	/**
	 * Default constructor.
	 */
	public GeneralShell() {
		this(new String[0]);
	}
	
	/**
	 * Alternate constructor.
	 * 
	 * @param args (to be passed to initialise() method)
	 */
	public GeneralShell(String[] args) {
		this(args, SWT.NO_TRIM);
	}

	
	/**
	 * Alternate constructor.
	 * 
	 * @param args (to be passed to initialise() method)
	 * @param style shell style
	 */
	public GeneralShell(String[] args, int style) {
		super();

		// Create full-screen shell 
		shell = new Shell(DISPLAY, style);
		shell.setMaximized(true);
		shell.setLayoutData(DEFAULT_LAYOUT_DATA);
		shell.setLayout(DEFAULT_LAYOUT);

		if (initialise(args)) {
			addHeader();
			addListener();
			createContents();
			addFooter();
			shellOpen();
		} else {
			shellClose(false);
			StringUtils.log("Shell abandoned!");
		}
		
		while (!shell.isDisposed()) {
			if (!DISPLAY.readAndDispatch()) { // process next message
				updateFooter(); // update battery meter display
				DISPLAY.sleep(); // wait for next message
			}
		}
		
	}
	
	/**
	 * Initialise shell.
	 * 
	 * @param args
	 *            from constructor
	 * @return boolean
	 */
	public abstract boolean initialise(String[] args);

	/**
	 * Set shell title.
	 * 
	 * @return shell title
	 */
	public abstract String setTitle();

	/**
	 * Handle listener event.
	 * 
	 * @param event listener event
	 */
	public abstract void handleListenerEvent(Event event);

	/**
	 * Create shell contents.
	 */
	public abstract void createContents();

	/**
	 * @return DEFAULT_LAYOUT_DATA
	 */
	private static GridData defaultLayoutData() {
	 	GridData defaultLayoutData = new GridData();
		defaultLayoutData.verticalAlignment = GridData.FILL;
		defaultLayoutData.horizontalAlignment = GridData.FILL;
		defaultLayoutData.grabExcessHorizontalSpace = true;
		defaultLayoutData.grabExcessVerticalSpace = true;
		return defaultLayoutData;
	}
	
	/**
	 * @return FOOTER_LAYOUT_DATA
	 */
	private static GridData footerLayoutData() {
		GridData footerLayoutData = new GridData();
		footerLayoutData.verticalAlignment = GridData.VERTICAL_ALIGN_END;
		footerLayoutData.horizontalAlignment = GridData.FILL;
		footerLayoutData.grabExcessHorizontalSpace = true;
		footerLayoutData.grabExcessVerticalSpace = false;
		return footerLayoutData;
	}
	
	/**
	 * This data cannot be shared across widgets and has to be recalculated each
	 * time (rather than calculated once and stored as a constant).
	 * 
	 * @return defaultLabelLayoutLayoutData
	 */
	private static GridData defaultLabelLayoutData() {
		GridData defaultLabelLayoutLayoutData = new GridData(GridData.FILL_HORIZONTAL);
		defaultLabelLayoutLayoutData.horizontalSpan = 1;
		return defaultLabelLayoutLayoutData;
	}
	
	/**
	 * @return DEFAULT_LAYOUT
	 */
	private static GridLayout defaultLayout() {
		GridLayout defaultGridLayout = new GridLayout();
		defaultGridLayout.numColumns = 1;
		defaultGridLayout.makeColumnsEqualWidth = true;
		return defaultGridLayout;
	}
	
	/**
	 * @return FOOTER_LAYOUT
	 */
	private static GridLayout footerLayout() {
		GridLayout footerGridLayout = new GridLayout();
		footerGridLayout.numColumns = 2;
		return footerGridLayout;
	}
	
	/**
	 * Add default shell listener.
	 */
	private void addListener() {
		listener = new Listener() {
			public void handleEvent(Event event) {

				showWaitCursor(shell);
				handleListenerEvent(event);
				hideCursor(shell);

			};
		};
	}
	
	/**
	 * Add shell title.
	 */
	private void addHeader() {
		String title = setTitle();
		this.title = newLabel(shell, title);

		// Also set shell title
		setShellTitle(title);
	}
	
	/**
	 * Set shell title.
	 * 
	 * @param title new title
	 */
	private void setShellTitle(String title) {
		if (title != null) {
			shell.setText(title);
		}
	}

	/**
	 * Reset shell title with new text.
	 * 
	 * @param title new title
	 */
	protected void resetTitle(String title) {
		
		this.title.setText(title);

		// Also reset shell title
		setShellTitle(title);
	}
	
	/**
	 * Add battery meter.
	 */
	private void addFooter() {
		
		// Add widgets
		Composite footer = new Composite(shell, SWT.NONE);
		footer.setLayoutData(FOOTER_LAYOUT_DATA);
		footer.setLayout(FOOTER_LAYOUT);
		
		batteryLabel = newLabel(footer, batteryLabelText, FOOTER_LABEL_FONT, FOOTER_LABEL_LAYOUT_DATA, SWT.WRAP);
		
		batteryMeter = new ProgressBar(footer, SWT.HORIZONTAL);
	    batteryMeter.setLayoutData(BATTERY_METER_LAYOUT_DATA);
	    
	    // Initialise
	    updateBatteryMeter();
	}
	
	/**
	 * Add asynchronous footer update request to event queue.
	 */
	private static void updateFooter() {

		DISPLAY.asyncExec(new Runnable() {
			public void run() {
				updateBatteryMeter();
			}
		});
	}
	
	/**
	 * Update battery meter and if necessary alert user of status change.
	 * Battery meter may be disposed at actual time of execution (when called
	 * asynchronously) so this exception is caught and handled.
	 */
	private static void updateBatteryMeter() {

		try {

			if (BATTERY_SERVICE.checkBattery()) {
				
				// Battery status has changed
				switch (BATTERY_SERVICE.getStatus()) {
				case BatteryStatus.MORIBUND:
					batteryLabelText = BATTERY_TEXT_LOW;
					while (BATTERY_SERVICE.isPercentageCritical()) {
						errorBox("WARNING", "Battery must be replaced now!");
						continue; // re-test to force battery change
					}
					break;
				case BatteryStatus.CRITICAL:
					batteryLabelText = BATTERY_TEXT_LOW;
					errorBox("WARNING",
							"Battery level is critical.\nPlease replace.");
					break;
				case BatteryStatus.LOW:
					batteryLabelText = BATTERY_TEXT_LOW;
					break;
				default:
					batteryLabelText = BATTERY_TEXT_HIGH;
					break;
				}
				
				batteryLabel.setText(batteryLabelText);
				batteryLabel.getParent().layout(true);
			}
			
			batteryMeter.setSelection(BATTERY_SERVICE.getPercent());
			
		} catch (SWTException e) {
			// Do nothing (battery meter has been disposed)
		}
	}
	
	/**
	 * Add empty label using default layout data, default font and default label
	 * style.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @return new label
	 */
	protected static Label newLabel(Object obj) {
		return newLabel(obj, null);
	}

	/**
	 * Add new label using default layout data, default font and default label
	 * style.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @param text
	 *            label text
	 * @return new label
	 */
	protected static Label newLabel(Object obj, String text) {
		return newLabel(obj, text, defaultLabelLayoutData());
	}

	/**
	 * Add new label using default layout data and default label style.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @param text
	 *            label text
	 * @param font
	 *            label font
	 * @return new label
	 */
	protected static Label newLabel(Object obj, String text, Font font) {
		return newLabel(obj, text, font, defaultLabelLayoutData());
	}
	
	/**
	 * Add new label using default font and default label style.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @param text
	 *            label text
	 * @param gridData
	 *            layout data
	 * @return new label
	 */
	protected static Label newLabel(Object obj, String text, GridData gridData) {
		return newLabel(obj, text, DEFAULT_LABEL_FONT, gridData);
	}
	
	/**
	 * Add new label using default label style.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @param text
	 *            label text
	 * @param font
	 *            label font
	 * @param gridData
	 *            layout data
	 * @return new label
	 */
	protected static Label newLabel(Object obj, String text, Font font, GridData gridData) {
		return newLabel(obj, text, font, gridData, DEFAULT_LABEL_STYLE);
	}
	
	/**
	 * Add new label.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add label
	 * @param text
	 *            label text
	 * @param font
	 *            label font
	 * @param gridData
	 *            layout data
	 * @param style
	 *            layout style
	 * @return new label
	 */
	protected static Label newLabel(Object obj, String text, Font font, GridData gridData, int style) {
		
		Label label;
		
		// Only Group is instanceof Group
		// Only Shell is instanceof Shell
		// Composite, Group and Shell are all instanceof Composite
		if (obj instanceof Group) {
			label = new Label((Group) obj, style);
		} else if (obj instanceof Shell) {
			label = new Label((Shell) obj, style);
		} else {
			label = new Label((Composite) obj, style);
		}

		if (text != null) {
			label.setText(text);
		}
		label.setFont(font);
		label.setLayoutData(gridData);

		return label;
	}

	/**
	 * Add new button using default listener and default layout data.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add button
	 * @param text
	 *            button text
	 * @return new button
	 */
	protected Button newButton(Object obj, String text) {
		return newButton(obj, text, listener);
	}

	/**
	 * Add new button using default listener.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add button
	 * @param text
	 *            button text
	 * @param gridData
	 *            layout data
	 * @return new button
	 */
	protected Button newButton(Object obj, String text, GridData gridData) {
		return newButton(obj, text, gridData, listener);
	}

	/**
	 * Add new button using default layout data.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add button
	 * @param text
	 *            button text
	 * @param listener
	 *            button listener
	 * @return new button
	 */
	protected static Button newButton(Object obj, String text, Listener listener) {
		return newButton(obj, text, DEFAULT_LAYOUT_DATA, listener);
	}

	/**
	 * Add new button.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add button
	 * @param text
	 *            button text
	 * @param gridData
	 *            layout data
	 * @param listener
	 *            button listener
	 * @return new button
	 */
	protected static Button newButton(Object obj, String text,
			GridData gridData, Listener listener) {

		Button button;

		// Only Group is instanceof Group
		// Only Shell is instanceof Shell
		// Composite, Group and Shell are all instanceof Composite
		if (obj instanceof Group) {
			button = new Button((Group) obj, SWT.PUSH);
		} else if (obj instanceof Shell) {
			button = new Button((Shell) obj, SWT.PUSH);
		} else {
			button = new Button((Composite) obj, SWT.PUSH);
		}

		if (text != null) {
			button.setText(text);
		}
		button.setFont(BUTTON_FONT);
		button.addListener(SWT.Selection, listener);
		button.setLayoutData(gridData);

		return button;
	}

	/**
	 * Add empty text box using default listener and default layout data.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add text box
	 * @param textLimit
	 *            width of text box in characters
	 * @return new text box
	 */
	protected Text newText(Object obj, int textLimit) {
		return newText(obj, null, textLimit, listener);
	}
	
	/**
	 * Add new text box using default listener and default layout data.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add text box
	 * @param initialText
	 *            initial text to display
	 * @param textLimit
	 *            width of text box in characters
	 * @return new text box
	 */
	protected Text newText(Object obj, String initialText, int textLimit) {
		return newText(obj, initialText, textLimit, listener);
	}

	/**
	 * Add new text box using default listener.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add text box
	 * @param initialText
	 *            initial text to display
	 * @param textLimit
	 *            width of text box in characters
	 * @param gridData
	 *            layout data
	 * @return new text box
	 */
	protected Text newText(Object obj, String initialText, int textLimit,
			GridData gridData) {
		return newText(obj, initialText, textLimit, gridData, listener);
	}

	/**
	 * Add new text box using default layout data.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add text box
	 * @param initialText
	 *            initial text to display
	 * @param textLimit
	 *            width of text box in characters
	 * @param listener
	 *            text box listener
	 * @return new text box
	 */
	protected static Text newText(Object obj, String initialText,
			int textLimit, Listener listener) {
		return newText(obj, initialText, textLimit, DEFAULT_LAYOUT_DATA, listener);
	}

	/**
	 * Add new text box.
	 * 
	 * @param obj
	 *            Group/Composite/Shell to which to add text box
	 * @param initialText
	 *            initial text to display
	 * @param textLimit
	 *            width of text box in characters
	 * @param gridData
	 *            layout data
	 * @param listener
	 *            text box listener
	 * @return new text box
	 */
	protected static Text newText(Object obj, String initialText,
			int textLimit, GridData gridData, Listener listener) {

		Text text;

		// Only Group is instanceof Group
		// Only Shell is instanceof Shell
		// Composite, Group and Shell are all instanceof Composite
		if (obj instanceof Group) {
			text = new Text((Group) obj, SWT.BORDER);
		} else if (obj instanceof Shell) {
			text = new Text((Shell) obj, SWT.BORDER);
		} else {
			text = new Text((Composite) obj, SWT.BORDER);
		}

		if (initialText != null) {
			text.setText(initialText);
		}
		if (textLimit > -1) {
			text.setTextLimit(textLimit);
		}
		text.setFont(TEXT_FONT);
		text.addListener(SWT.Modify, listener);
		text.setLayoutData(gridData);

		return text;
	}

	/**
	 * Alert user to error (defaulting title from shell) and prompt for
	 * acknowledgement.
	 * 
	 * @param message
	 *            error message
	 * @return <code>returnCode</code>
	 */
	protected int errorBox(String message) {
		return errorBox(shell.getText(), message);
	}

	/**
	 * Alert user to error and prompt for acknowledgement.
	 * 
	 * @param text
	 *            error title
	 * @param message
	 *            error message
	 * @return <code>returnCode</code>
	 */
	public static int errorBox(String text, String message) {
		new ErrorShell(new String[] { text, message });
		return returnCode;
	}

	/**
	 * Prompt user for response (default title from shell).
	 * 
	 * @param message
	 *            question/prompt
	 * @return <code>returnCode</code>
	 */
	protected int questionBox(String message) {
		return questionBox(shell.getText(), message);
	}

	/**
	 * Prompt user for response.
	 * 
	 * @param text
	 *            title
	 * @param message
	 *            question/prompt
	 * @return <code>returnCode</code>
	 */
	protected static int questionBox(String text, String message) {
		new QuestionShell(new String[] { text, message });
		return returnCode;
	}

	/**
	 * Open shell.
	 */
	private void shellOpen() {
		shell.open(); // open shell for user access
		logShellOpen(shell);
		SplashScreen.hide(); // do this last to avoid screen flicker
	}

	/**
	 * Close shell and show splash screen.
	 */
	protected void shellClose() {
		shellClose(true);
	}
	
	
	/**
	 * Close shell.
	 * 
	 * @param showSplashScreen set <code>false</code> to not show splash screen
	 */
	protected void shellClose(boolean showSplashScreen) {
		
		shell.setEnabled(false);
		
		if (showSplashScreen) {
			SplashScreen.show();
		} else {
			// Dispose of any stray hourglasses!
			hideCursor();
		}
		
		logShellClose(shell);
		shell.close();
	}

	/**
	 * @return batteryMeter
	 */
	protected static ProgressBar getBatteryMeter() {
		return batteryMeter;
	}

	/**
	 * @param batteryMeter batteryMeter
	 */
	protected static void setBatteryMeter(ProgressBar batteryMeter) {
		GeneralShell.batteryMeter = batteryMeter;
	}

	/**
	 * @return batteryLabel
	 */
	protected static Label getBatteryLabel() {
		return batteryLabel;
	}

	/**
	 * @param batteryLabel batteryLabel
	 */
	protected static void setBatteryLabel(Label batteryLabel) {
		GeneralShell.batteryLabel = batteryLabel;
	}

	/**
	 * @return returnCode
	 */
	protected static int getReturnCode() {
		return returnCode;
	}

	/**
	 * @param returnCode returnCode
	 */
	protected static void setReturnCode(int returnCode) {
		GeneralShell.returnCode = returnCode;
	}

}

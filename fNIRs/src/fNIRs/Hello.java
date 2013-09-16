package fNIRs;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;

public class Hello {

	protected Shell shell;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Hello window = new Hello();
			window.open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Open the window.
	 */
	public void open() {
		Display display = Display.getDefault();
		createContents();
		shell.open();
		shell.layout();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shell = new Shell();
		shell.setSize(450, 300);
		shell.setText("SWT Application");
		
		final Label lblHelloWorld = new Label(shell, SWT.NONE);
		lblHelloWorld.setBounds(164, 81, 75, 15);
		lblHelloWorld.setText("Hello, World!!");
		lblHelloWorld.setVisible(false);
	
		final Spinner spinner = new Spinner(shell, SWT.BORDER);
		spinner.setBounds(65, 44, 47, 22);
		
		Button btnPressMe = new Button(shell, SWT.NONE);
		btnPressMe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblHelloWorld.setVisible(true);
				lblHelloWorld.setText(spinner.getText());
			}
		});
		btnPressMe.setBounds(164, 114, 75, 25);
		btnPressMe.setText("Hey Justin!");
		
		Button btnExit = new Button(shell, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shell.close();
			}
		});
		btnExit.setBounds(164, 194, 75, 25);
		btnExit.setText("Exit");
		
	
		




	}
}

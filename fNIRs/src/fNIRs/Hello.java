package fNIRs;

import java.util.ArrayList;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DragDetectEvent;
import org.eclipse.swt.events.DragDetectListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Combo;

public class Hello {

	protected Shell shlFnirsDataProcessing;
	private Text text;
	private static ArrayList<Integer> fileList;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Hello window = new Hello();
			fileList = new ArrayList<Integer>();
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
		shlFnirsDataProcessing.open();
		shlFnirsDataProcessing.layout();
		while (!shlFnirsDataProcessing.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
	}

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFnirsDataProcessing = new Shell();
		shlFnirsDataProcessing.setSize(648, 411);
		shlFnirsDataProcessing.setText("fNIRs Data Processing and Analysis");
		
		final Label lblHelloWorld = new Label(shlFnirsDataProcessing, SWT.NONE);
		lblHelloWorld.setBounds(326, 110, 75, 15);
		lblHelloWorld.setText("Hello, World!!");
		lblHelloWorld.setVisible(false);
	
		final Spinner spinner = new Spinner(shlFnirsDataProcessing, SWT.BORDER);
		spinner.setBounds(354, 57, 47, 22);
		
		Button btnPressMe = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnPressMe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblHelloWorld.setVisible(true);
				lblHelloWorld.setText(spinner.getText());
			}
		});
		btnPressMe.setBounds(326, 131, 75, 25);
		btnPressMe.setText("Hey Justin!");
		
		Button btnExit = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlFnirsDataProcessing.close();
			}
		});
		btnExit.setBounds(233, 322, 75, 25);
		btnExit.setText("Exit");
		
		text = new Text(shlFnirsDataProcessing, SWT.BORDER);
		text.setBounds(10, 44, 76, 21);
		
		final List list = new List(shlFnirsDataProcessing, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL);
		list.setBounds(10, 88, 76, 68);
		list.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				for (Integer item: list.getSelectionIndices())
					fileList.add(item);
				int[] indices = new int[fileList.size()];
				for (int i=0; i<fileList.size();i++)
					indices[i] = fileList.get(i);
				list.select(indices);
			}
		});
		
		Button btnBrowse = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing);
				fileDialog.open();
				text.setText(fileDialog.getFileName());
			}
		});
		btnBrowse.setBounds(100, 42, 75, 25);
		btnBrowse.setText("Browse");
		
		
		Button btnEnter = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.add(text.getText());
				text.setText("");
			}
		});
		btnEnter.setBounds(188, 42, 75, 25);
		btnEnter.setText("Enter");
		
		Menu menu = new Menu(shlFnirsDataProcessing, SWT.BAR);
		shlFnirsDataProcessing.setMenuBar(menu);
		
		MenuItem mntmHelp_1 = new MenuItem(menu, SWT.CASCADE);
		mntmHelp_1.setText("Help");
		
		Menu menu_1 = new Menu(mntmHelp_1);
		mntmHelp_1.setMenu(menu_1);
		
		MenuItem mntmHelp = new MenuItem(menu_1, SWT.NONE);
		mntmHelp.setText("Tutorial");
		
		Button btnClear = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.deselectAll();
				fileList.clear();
			}
		});
		btnClear.setBounds(11, 162, 75, 25);
		btnClear.setText("Clear");
		

		
	
		




	}
}

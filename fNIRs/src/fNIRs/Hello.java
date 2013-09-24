package fNIRs;

import java.io.File;
import java.io.IOException;
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
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.wb.swt.SWTResourceManager;
import org.eclipse.swt.widgets.Composite;

public class Hello {

	protected Shell shlFnirsDataProcessing;
	private Text text;
	private static ArrayList<Integer> fileList;
	String fileName;

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
		shlFnirsDataProcessing.setBackground(SWTResourceManager.getColor(255, 255, 255));
		shlFnirsDataProcessing.setSize(1095, 589);
		shlFnirsDataProcessing.setText("fNIRs Data Processing and Analysis");
		
		final Label lblHelloWorld = new Label(shlFnirsDataProcessing, SWT.NONE);
		lblHelloWorld.setBounds(838, 47, 75, 15);
		lblHelloWorld.setText("Hello, World!!");
		//lblHelloWorld.setVisible(false);
	
		final Spinner spinner = new Spinner(shlFnirsDataProcessing, SWT.BORDER);
		spinner.setBounds(620, 44, 47, 22);
		
		Button btnPressMe = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnPressMe.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				lblHelloWorld.setVisible(true);
				lblHelloWorld.setText(spinner.getText());
			}
		});
		btnPressMe.setBounds(725, 42, 75, 25);
		btnPressMe.setText("Hey Justin!");
		
		Button btnExit = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnExit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				shlFnirsDataProcessing.close();
			}
		});
		btnExit.setBounds(994, 496, 75, 25);
		btnExit.setText("Exit");
		
		final List list = new List(shlFnirsDataProcessing, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list.setBounds(35, 81, 213, 382);
		list.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				//lblHelloWorld.setText(list.getFocusIndex() + fileList.toString());
				if (fileList.contains(list.getFocusIndex())) {
					fileList.remove((Object)list.getFocusIndex());
					list.deselect(list.getFocusIndex());
				}
				else {
					for (Integer item: list.getSelectionIndices())
						fileList.add(item);
				}
				int[] indices = new int[fileList.size()];
				for (int i=0; i<fileList.size();i++)
					indices[i] = fileList.get(i);
				list.select(indices);
			}
		});
		
		final Label lblFileDoesNot = new Label(shlFnirsDataProcessing, SWT.NONE);
		lblFileDoesNot.setBounds(10, 60, 100, 15);
		lblFileDoesNot.setText("File does not exist");
		lblFileDoesNot.setVisible(false);
		
		
		Button btnBrowse = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
				fileName = fileDialog.open();
				text.setText(fileName);
			}
		});
		btnBrowse.setBounds(92, 24, 75, 25);
		btnBrowse.setText("Browse");
		
		
		Button btnEnter = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				File file = new File(text.getText());
				if (!file.exists()) {
					lblFileDoesNot.setVisible(true);
				}
				else {
					list.add(text.getText());
					lblFileDoesNot.setVisible(false);
					text.setText("");
				}
				
			}
		});
		btnEnter.setBounds(173, 24, 75, 25);
		btnEnter.setText("Add");
		
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
		btnClear.setBounds(45, 469, 100, 25);
		btnClear.setText("Clear Selections");
		
		TabFolder tabFolder = new TabFolder(shlFnirsDataProcessing, SWT.NONE);
		tabFolder.setBounds(301, 81, 722, 386);
		
		TabItem tbtmFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmFiles.setText("Files");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmFiles.setControl(composite);
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Stats");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		
		TabItem tbtmMachineLearning = new TabItem(tabFolder, SWT.NONE);
		tbtmMachineLearning.setText("Machine Learning");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmMachineLearning.setControl(composite_2);
		
		text = new Text(shlFnirsDataProcessing, SWT.BORDER);
		text.setBounds(10, 26, 76, 21);
		
		Button btnRemove = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnRemove.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				for (Integer item: list.getSelectionIndices())
					fileList.add(item);
				int[] indices = new int[fileList.size()];
				for (int i=0; i<fileList.size();i++)
					indices[i] = fileList.get(i);
				list.remove(indices);
				fileList.clear();
			}
		});
		btnRemove.setBounds(151, 469, 75, 25);
		btnRemove.setText("Remove");
		

		
	
		




	}
}

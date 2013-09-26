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
		shlFnirsDataProcessing.setSize(1000, 600);
		shlFnirsDataProcessing.setText("fNIRs Data Processing and Analysis");
		
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
		list.setBounds(10, 10, 226, 491);
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
		btnClear.setBounds(10, 507, 110, 25);
		btnClear.setText("Clear Selections");
		
		TabFolder tabFolder = new TabFolder(shlFnirsDataProcessing, SWT.NONE);
		tabFolder.setBounds(256, 10, 732, 522);
		
		TabItem tbtmLoadFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmLoadFiles.setText("Load File(s)");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLoadFiles.setControl(composite);
		
		Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setBounds(10, 10, 694, 338);
		
		
		Button btnBrowse = new Button(composite_3, SWT.NONE);
		btnBrowse.setBounds(528, 8, 75, 21);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
				fileName = fileDialog.open();
				text.setText(fileName);
			}
		});
		btnBrowse.setText("Browse");
		
		final Label lblFileDoesNot = new Label(composite_3, SWT.NONE);
		lblFileDoesNot.setBounds(10, 35, 100, 15);
		lblFileDoesNot.setText("File does not exist");
		lblFileDoesNot.setVisible(false);
		
		
		Button btnEnter = new Button(composite_3, SWT.NONE);
		btnEnter.setBounds(609, 8, 75, 21);
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
		btnEnter.setText("Add");
		
		text = new Text(composite_3, SWT.BORDER);
		text.setBounds(10, 8, 512, 21);
		
		TabItem tbtmNewItem = new TabItem(tabFolder, SWT.NONE);
		tbtmNewItem.setText("Preprocessing");
		
		
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Statistical Analysis");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		
		TabItem tbtmMachineLearning = new TabItem(tabFolder, SWT.NONE);
		tbtmMachineLearning.setText("Machine Learning");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmMachineLearning.setControl(composite_2);
		
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
		btnRemove.setBounds(126, 507, 110, 25);
		btnRemove.setText("Remove Files");
		

		
	
		




	}
}

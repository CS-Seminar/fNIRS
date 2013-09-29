package fNIRs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
	private static ArrayList<Integer> indexList;
	private static HashMap<String,Subject> subjectMap;
	private static Integer subjectNumber;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;

	/**
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Hello window = new Hello();
			indexList = new ArrayList<Integer>();
			subjectMap = new HashMap<String,Subject>();
			subjectNumber = 1;
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
		
		final List list = new List(shlFnirsDataProcessing, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list.setBounds(10, 10, 226, 491);
		list.addSelectionListener(new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e) {
				if (indexList.contains(list.getFocusIndex())) {
					indexList.remove((Object)list.getFocusIndex());
					list.deselect(list.getFocusIndex());
				}
				else {
					for (Integer item: list.getSelectionIndices())
						indexList.add(item);
				}
				int[] indices = new int[indexList.size()];
				for (int i=0; i<indexList.size();i++)
					indices[i] = indexList.get(i);
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
				indexList.clear();
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
		
		final Composite composite_3 = new Composite(composite, SWT.NONE);
		composite_3.setBounds(10, 96, 694, 254);
		composite_3.setVisible(false);
		
		
		Button btnBrowse = new Button(composite_3, SWT.NONE);
		btnBrowse.setBounds(528, 8, 75, 21);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
				String fileName = fileDialog.open();
				text.setText(fileName);
			}
		});
		btnBrowse.setText("Browse");
		
		final Label lblFileDoesNot = new Label(composite_3, SWT.NONE);
		lblFileDoesNot.setBounds(422, 41, 100, 15);
		lblFileDoesNot.setText("File does not exist");
		lblFileDoesNot.setVisible(false);
		
		
		Button btnEnter = new Button(composite_3, SWT.NONE);
		btnEnter.setBounds(275, 213, 75, 21);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				

				
			}
		});
		btnEnter.setText("Add");
		
		text = new Text(composite_3, SWT.BORDER);
		text.setBounds(107, 8, 415, 21);
		
		final Spinner spinner = new Spinner(composite_3, SWT.BORDER);
		spinner.setEnabled(false);
		spinner.setBounds(462, 92, 47, 22);
		
		final Button btnCheckButton = new Button(composite_3, SWT.CHECK);
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckButton.getSelection())
					spinner.setEnabled(true);
				else
					spinner.setEnabled(false);
			}
		});
		btnCheckButton.setBounds(352, 94, 103, 16);
		btnCheckButton.setText("Sliding Average");
		
		Label lblDataFile = new Label(composite_3, SWT.NONE);
		lblDataFile.setBounds(46, 11, 55, 15);
		lblDataFile.setText("Data File:");
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setBounds(239, 92, 33, 21);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(117, 95, 116, 15);
		lblNewLabel.setText("Sampling Frequency:");
		
		Label lblHz = new Label(composite_3, SWT.NONE);
		lblHz.setBounds(275, 95, 14, 15);
		lblHz.setText("Hz");
		
		Label lblHighPassFilter = new Label(composite_3, SWT.NONE);
		lblHighPassFilter.setBounds(107, 124, 147, 15);
		lblHighPassFilter.setText("High Pass Filter Frequency:");
		
		text_2 = new Text(composite_3, SWT.BORDER);
		text_2.setText(".1");
		text_2.setBounds(260, 121, 29, 21);
		
		Label label = new Label(composite_3, SWT.NONE);
		label.setText("Hz");
		label.setBounds(295, 124, 14, 15);
		
		Label lblLowPassFilter = new Label(composite_3, SWT.NONE);
		lblLowPassFilter.setBounds(107, 156, 142, 15);
		lblLowPassFilter.setText("Low Pass Filter Frequency:");
		
		text_3 = new Text(composite_3, SWT.BORDER);
		text_3.setText(".01");
		text_3.setBounds(260, 153, 29, 21);
		
		Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setText("Hz");
		label_1.setBounds(295, 156, 14, 15);
		
		Label lblPreprocessingOptions = new Label(composite_3, SWT.NONE);
		lblPreprocessingOptions.setBounds(60, 74, 130, 15);
		lblPreprocessingOptions.setText("Preprocessing Options:");
		
		final Composite composite_4 = new Composite(composite, SWT.NONE);
		composite_4.setBounds(10, 96, 694, 254);
		composite_4.setVisible(false);
		
		text_4 = new Text(composite_4, SWT.BORDER);
		text_4.setBounds(116, 32, 173, 21);
		
		text_5 = new Text(composite_4, SWT.BORDER);
		text_5.setBounds(116, 82, 173, 21);
		
		Button btnNewButton = new Button(composite_4, SWT.NONE);
		btnNewButton.setBounds(295, 32, 75, 25);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
				String fileName = fileDialog.open();
				text_4.setText(fileName);
			}
		});
		btnNewButton.setText("Browse");
		
		Button btnBrowse_1 = new Button(composite_4, SWT.NONE);
		btnBrowse_1.setBounds(295, 78, 75, 25);
		btnBrowse_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				FileDialog fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
				String fileName = fileDialog.open();
				text_5.setText(fileName);
			}
		});
		btnBrowse_1.setText("Browse");
		
		Label lblHboFile = new Label(composite_4, SWT.NONE);
		lblHboFile.setBounds(55, 35, 55, 15);
		lblHboFile.setText("HbO File:");
		
		Label lblHbFile = new Label(composite_4, SWT.NONE);
		lblHbFile.setBounds(66, 85, 44, 15);
		lblHbFile.setText("Hb File:");
		
		Button btnAdd = new Button(composite_4, SWT.NONE);
		btnAdd.setBounds(295, 136, 75, 25);
		btnAdd.setText("Add");
		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				Subject newSubject = new Subject(new File(text_4.getText()),new File(text_5.getText()));
				
				subjectMap.put("Subject "+subjectNumber.toString(), newSubject);

				
				//File file = new File(text.getText());
				//if (!file.exists()) {
			//		lblFileDoesNot.setVisible(true);
				//}
				list.add("Subject "+subjectNumber.toString());
				lblFileDoesNot.setVisible(false);
				text_4.setText("");
				text_5.setText("");
				
				subjectNumber = subjectNumber + 1;
				
			}
		});
		
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
					indexList.add(item);
				int[] indices = new int[indexList.size()];
				for (int i=0; i<indexList.size();i++)
					indices[i] = indexList.get(i);
				list.remove(indices);
				indexList.clear();
			}
		});
		btnRemove.setBounds(126, 507, 110, 25);
		btnRemove.setText("Remove Files");
		
		Button btnIssOxyplex = new Button(composite, SWT.RADIO);
		btnIssOxyplex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite_3.setVisible(true);
				composite_4.setVisible(false);
			}
		});
		btnIssOxyplex.setBounds(53, 10, 90, 16);
		btnIssOxyplex.setText("ISS Oxyplex");
		
		Button btnHatachi = new Button(composite, SWT.RADIO);
		btnHatachi.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite_3.setVisible(false);
				composite_4.setVisible(true);
			}
		});
		btnHatachi.setBounds(53, 32, 90, 16);
		btnHatachi.setText("Hatachi");
		
		Button btnOther = new Button(composite, SWT.RADIO);
		btnOther.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				composite_3.setVisible(false);
				composite_4.setVisible(false);
			}
		});
		btnOther.setBounds(53, 54, 90, 16);
		btnOther.setText("Other");
		
	
		




	}
}

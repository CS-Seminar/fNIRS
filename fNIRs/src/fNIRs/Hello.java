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
	private static ArrayList<Integer> indexList;
	private static HashMap<String,Subject> subjectMap;
	private Text text;
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
		tabFolder.setBounds(242, 10, 746, 522);
		
		TabItem tbtmLoadFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmLoadFiles.setText("Load File(s) / Preprocessing");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		tbtmLoadFiles.setControl(composite);
		
		
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Statistical Analysis");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		Button btnEnter = new Button(composite_1, SWT.NONE);
		btnEnter.setBounds(275, 213, 75, 21);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				File newFile = new File(text.getText());
				if (!newFile.exists()) {
	
					return;
				}
				else {
		
				}

				
			}
		});
		btnEnter.setText("Add");

		
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
		
		TabFolder tabFolder_1 = new TabFolder(composite, SWT.NONE);
		tabFolder_1.setBounds(10, 10, 718, 474);
		
		TabItem tbtmNewItem = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem.setText("ISS Oxyplex");
		
		Composite composite_3 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(108, 29, 55, 15);
		lblNewLabel.setText("Data File:");
		
		text = new Text(composite_3, SWT.BORDER);
		text.setBounds(169, 26, 296, 21);
		
		Button btnNewButton = new Button(composite_3, SWT.NONE);
		btnNewButton.setBounds(486, 24, 75, 25);
		btnNewButton.setText("Browse");
		
		Label lblPreprocessingOptions = new Label(composite_3, SWT.NONE);
		lblPreprocessingOptions.setBounds(113, 90, 169, 15);
		lblPreprocessingOptions.setText("Preprocessing Options:");
		
		Label lblSamplingFrequency = new Label(composite_3, SWT.NONE);
		lblSamplingFrequency.setBounds(108, 140, 129, 15);
		lblSamplingFrequency.setText("Sampling Frequency:");
		
		Label lblHighPassFilter = new Label(composite_3, SWT.NONE);
		lblHighPassFilter.setBounds(113, 176, 146, 15);
		lblHighPassFilter.setText("High Pass Filter Frequency:");
		
		Label lblLowPassFilter = new Label(composite_3, SWT.NONE);
		lblLowPassFilter.setBounds(96, 213, 146, 15);
		lblLowPassFilter.setText("Low Pass Filter Frequency:");
		
		Button btnAdd = new Button(composite_3, SWT.NONE);
		btnAdd.setBounds(254, 264, 75, 25);
		btnAdd.setText("Add");

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				File HbFile = new File(text_1.getText());
				if (!HbFile.exists()) {
					//lblFileDoesNot.setVisible(true);
					return;
				}
				else {
					//lblFileDoesNot.setVisible(false);
				}
				
				File HbOFile = new File(text_2.getText());
				if (!HbOFile.exists()) {
					//lblFileDoesNot.setVisible(true);
					return;
				}
				else {
					//lblFileDoesNot.setVisible(false);
				}


				
				
				Subject newSubject = new Subject(HbFile,HbOFile);
				
				subjectMap.put("Subject "+subjectNumber.toString(), newSubject);

				list.add("Subject "+subjectNumber.toString());
		
				text_1.setText("");
				text_2.setText("");
				
				subjectNumber = subjectNumber + 1;
				
			}
		});
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setBounds(243, 134, 76, 21);
		
		Label lblHz = new Label(composite_3, SWT.NONE);
		lblHz.setBounds(339, 140, 55, 15);
		lblHz.setText("Hz");
		
		text_2 = new Text(composite_3, SWT.BORDER);
		text_2.setBounds(265, 170, 76, 21);
		
		Label lblHz_1 = new Label(composite_3, SWT.NONE);
		lblHz_1.setBounds(347, 176, 55, 15);
		lblHz_1.setText("Hz");
		
		Label lblHz_2 = new Label(composite_3, SWT.NONE);
		lblHz_2.setBounds(339, 213, 55, 15);
		lblHz_2.setText("Hz");
		
		text_3 = new Text(composite_3, SWT.BORDER);
		text_3.setBounds(253, 219, 76, 21);
		
		Button btnSlidingAverage = new Button(composite_3, SWT.CHECK);
		btnSlidingAverage.setBounds(426, 140, 106, 16);
		btnSlidingAverage.setText("Sliding Average:");
		
		Spinner spinner = new Spinner(composite_3, SWT.BORDER);
		spinner.setBounds(537, 137, 47, 22);
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setText("Hatachi");
		
		Composite composite_4 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setControl(composite_4);
		
		Label lblNewLabel_1 = new Label(composite_4, SWT.NONE);
		lblNewLabel_1.setBounds(61, 158, 55, 15);
		lblNewLabel_1.setText("HBO Data");
		
		Label lblNewLabel_2 = new Label(composite_4, SWT.NONE);
		lblNewLabel_2.setBounds(90, 227, 55, 15);
		lblNewLabel_2.setText("HB Data");
		
		text_4 = new Text(composite_4, SWT.BORDER);
		text_4.setBounds(174, 152, 271, 21);
		
		text_5 = new Text(composite_4, SWT.BORDER);
		text_5.setBounds(202, 227, 264, 21);
		
		Button btnNewButton_1 = new Button(composite_4, SWT.NONE);
		btnNewButton_1.setBounds(284, 299, 75, 25);
		btnNewButton_1.setText("Add");
		
		Label lblHitachi = new Label(composite_4, SWT.NONE);
		lblHitachi.setBounds(72, 85, 93, 15);
		lblHitachi.setText("Hitachi");
		
		Button btnNewButton_2 = new Button(composite_4, SWT.NONE);
		btnNewButton_2.setBounds(451, 150, 75, 25);
		btnNewButton_2.setText("Browse");
		
		Button btnNewButton_3 = new Button(composite_4, SWT.NONE);
		btnNewButton_3.setBounds(476, 225, 75, 25);
		btnNewButton_3.setText("Browse");
		
		TabItem tbtmNewItem_1 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_1.setText("Other");
		
	
		




	}
}

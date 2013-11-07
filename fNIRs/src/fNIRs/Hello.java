package fNIRs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

import com.mathworks.toolbox.javabuilder.*;

import preprocess_2.Preprocess;
import org.eclipse.swt.*;
import org.eclipse.swt.events.*;
import org.eclipse.swt.layout.*;
import org.eclipse.swt.widgets.*;

public class Hello {

	protected Shell shlFnirsDataProcessing;
	private static ArrayList<Integer> indexList;
	private static Workspace workspace;
	private Text text;
	private Text text_1;
	private Text text_2;
	private Text text_3;
	private Text text_4;
	private Text text_5;
	private Text text_subName;
	private static Preprocess pre;
	private Text text_subName2;
	private Text text_6;
	private FileDialog fileDialog;
	private Text text_7;

	/*
	 * Launch the application.
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Hello window = new Hello();
			indexList = new ArrayList<Integer>();
			pre = new Preprocess();
			//workspace = new Workspace("C:\\Users\\shammond\\Desktop\\CS_Seminar\\fNIRs\\workspace", pre);
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
	
	boolean setExists(File file, Label label) {
		if (!file.exists()) {
			label.setVisible(true);
			return false;
		}
		label.setVisible(false);
		return true;
	}
	
	void browse(Text text) {
		String fileName = fileDialog.open();
		if (fileName!=null)
			text.setText(fileName);
	}
	

	/**
	 * Create contents of the window.
	 */
	protected void createContents() {
		shlFnirsDataProcessing = new Shell();
		shlFnirsDataProcessing.setImage(SWTResourceManager.getImage(Hello.class, "/fNIRs/logo.png"));
		shlFnirsDataProcessing.setBackground(SWTResourceManager.getColor(255, 255, 255));
		shlFnirsDataProcessing.setSize(1000, 600);
		shlFnirsDataProcessing.setText("fNIRs Data Processing and Analysis");
		
		fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
		
		DirectoryDialog dlg = new DirectoryDialog(shlFnirsDataProcessing);
		dlg.setText("Select Workspace");
	    String selected = dlg.open(); // cannot seem to name New Folder for some reason
	    //System.out.println(selected);
	    workspace = new Workspace(selected,pre);
		
		final List list = new List(shlFnirsDataProcessing, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list.setBounds(10, 10, 226, 491);
		workspace.loadSubjects(list);
		
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

		TabFolder tabFolder_1 = new TabFolder(composite, SWT.NONE);
		tabFolder_1.setBounds(10, 10, 718, 474);

		TabItem tbtmNewItem = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem.setText("ISS Oxyplex");

		Composite composite_3 = new Composite(tabFolder_1, SWT.NONE);
		tbtmNewItem.setControl(composite_3);
		
		Button btnBrowse = new Button(composite_3, SWT.NONE);
		btnBrowse.setBounds(510, 8, 75, 21);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text);
			}
		});
		btnBrowse.setText("Browse");
		
		final Label lblFileDoesNot = new Label(composite_3, SWT.NONE);
		lblFileDoesNot.setBounds(600, 11, 100, 15);
		lblFileDoesNot.setText("File does not exist");
		lblFileDoesNot.setVisible(false);
		
		final Spinner spinner = new Spinner(composite_3, SWT.BORDER);
		spinner.setEnabled(false);
		spinner.setBounds(462, 92, 47, 22);
		
		final Label lblPleaseFillIn = new Label(composite_3, SWT.NONE);
		lblPleaseFillIn.setBounds(363, 138, 147, 15);
		lblPleaseFillIn.setText("Please fill in all frequencies");
		lblPleaseFillIn.setVisible(false);
		
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
		
		Label lblSubjectName = new Label(composite_3, SWT.NONE);
		lblSubjectName.setBounds(60, 197, 83, 15);
		lblSubjectName.setText("Subject Name:");
		
		final Label lblChooseANew = new Label(composite_3, SWT.NONE);
		lblChooseANew.setBounds(295, 197, 110, 15);
		lblChooseANew.setText("Choose a new name");
		lblChooseANew.setVisible(false);
		
		text_subName = new Text(composite_3, SWT.BORDER);
		text_subName.setBounds(162, 197, 110, 21);
		
		final Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setVisible(false);
		label_2.setText("File does not exist");
		label_2.setBounds(600, 41, 100, 15);
		
		Button btnEnter = new Button(composite_3, SWT.NONE);
		btnEnter.setBounds(275, 247, 75, 21);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				
				File newFile = new File(text.getText());
				if (!setExists(newFile,lblFileDoesNot))
					return;
				
				File condFile = new File(text_6.getText());
				if (!setExists(condFile,label_2))
					return;
				
				double freq;
				double hpf;
				double lpf;
				
				try {
					freq = (Double.valueOf(text_1.getText())).doubleValue();
					hpf = (Double.valueOf(text_2.getText())).doubleValue();
					lpf = (Double.valueOf(text_3.getText())).doubleValue();
				}
				catch (NumberFormatException e1) {
					lblPleaseFillIn.setVisible(true);
					return;
				}
				
				lblPleaseFillIn.setVisible(false);
				
				char slideavg = 'n';
				int interval = 0;
				
				String subjectName = text_subName.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					lblChooseANew.setVisible(true);
					return;
				}
				lblChooseANew.setVisible(false);
					
				if (btnCheckButton.getSelection()) {
					slideavg = 'y';
					interval = (Integer.valueOf(spinner.getText())).intValue();
				}
				
				workspace.addSubject(subjectName, newFile, condFile, freq, hpf, lpf, slideavg, interval);
				list.add(subjectName);
				
				text.setText("");
				text_subName.setText("");
				text_6.setText("");
			}

		});
		btnEnter.setText("Add");
		
		text = new Text(composite_3, SWT.BORDER);
		text.setBounds(107, 8, 397, 21);
		
		Label lblDataFile = new Label(composite_3, SWT.NONE);
		lblDataFile.setBounds(46, 11, 55, 15);
		lblDataFile.setText("Data File:");
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setText("2");
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
		
		Label lblConditionsFile = new Label(composite_3, SWT.NONE);
		lblConditionsFile.setBounds(46, 41, 90, 15);
		lblConditionsFile.setText("Conditions File:");
		
		text_6 = new Text(composite_3, SWT.BORDER);
		text_6.setBounds(142, 41, 361, 21);
		
		Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_6);
			}
		});
		button.setText("Browse");
		button.setBounds(510, 41, 75, 21);
		
		final Composite composite_4 = new Composite(tabFolder_1, SWT.NONE);
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
				browse(text_4);
			}
		});
		btnNewButton.setText("Browse");
		
		Button btnBrowse_1 = new Button(composite_4, SWT.NONE);
		btnBrowse_1.setBounds(295, 78, 75, 25);
		btnBrowse_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_5);
			}
		});
		btnBrowse_1.setText("Browse");
		
		Label lblHboFile = new Label(composite_4, SWT.NONE);
		lblHboFile.setBounds(55, 85, 55, 15);
		lblHboFile.setText("HbO File:");
		
		Label lblHbFile = new Label(composite_4, SWT.NONE);
		lblHbFile.setBounds(66, 35, 44, 15);
		lblHbFile.setText("Hb File:");
		
		Button btnAdd = new Button(composite_4, SWT.NONE);
		btnAdd.setBounds(295, 222, 75, 25);
		btnAdd.setText("Add");
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Statistical Analysis");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		
		TabItem tbtmMachineLearning = new TabItem(tabFolder, SWT.NONE);
		tbtmMachineLearning.setText("Data Mining");
		
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
		
		Label lblSubjectName2 = new Label(composite_4, SWT.NONE);
		lblSubjectName2.setBounds(55, 183, 84, 15);
		lblSubjectName2.setText("Subject Name:");
		
		text_subName2 = new Text(composite_4, SWT.BORDER);
		text_subName2.setBounds(145, 180, 144, 21);
		
		final Label lblFileDoesNot_1 = new Label(composite_4, SWT.NONE);
		lblFileDoesNot_1.setBounds(203, 59, 105, 15);
		lblFileDoesNot_1.setText("File Does Not Exist");
		lblFileDoesNot_1.setVisible(false);
		
		final Label lblFileDoesNot_2 = new Label(composite_4, SWT.NONE);
		lblFileDoesNot_2.setText("File Does Not Exist");
		lblFileDoesNot_2.setBounds(203, 109, 105, 15);
		lblFileDoesNot_2.setVisible(false);
		
		final Label lblChooseANew_1 = new Label(composite_4, SWT.NONE);
		lblChooseANew_1.setBounds(312, 183, 155, 15);
		lblChooseANew_1.setText("Choose a new subject name");
		lblChooseANew_1.setVisible(false);
		
		final Spinner spinner_1 = new Spinner(composite_4, SWT.BORDER);
		spinner_1.setBounds(557, 56, 47, 22);
		
		Label lblNumberOfChannels = new Label(composite_4, SWT.NONE);
		lblNumberOfChannels.setBounds(436, 59, 115, 15);
		lblNumberOfChannels.setText("Number of Channels:");
		
		final Label label_4 = new Label(composite_4, SWT.NONE);
		label_4.setVisible(false);
		label_4.setText("File Does Not Exist");
		label_4.setBounds(203, 159, 105, 15);

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				File condFile = new File(text_7.getText());
				if (!setExists(condFile,label_4))
					return;
				
				int channels = (Integer.valueOf(spinner_1.getText())).intValue();
				
				File HbFile = new File(text_4.getText());
				File HbOFile = new File(text_5.getText());
				
				if (text_4.getText()!="")
					if (!setExists(HbFile,lblFileDoesNot_1))
						return;
				
				if (text_5.getText()!="")
					if (!setExists(HbOFile,lblFileDoesNot_2))
						return;
				
				if (!HbFile.exists() && !HbOFile.exists()) {
					return;
				}
				
				String subjectName = text_subName2.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					lblChooseANew_1.setVisible(true);
					return;
				}
				lblChooseANew_1.setVisible(false);

				if (HbFile.exists()) {
					try {
						pre.xlsreadfile(HbFile.getAbsolutePath(), "Hb", channels);
						HbFile = new File("Hb");
					} catch (MWException e1) {
						e1.printStackTrace();
					}
				}
				
				if (HbOFile.exists()) {
					try {
						pre.xlsreadfile(HbOFile.getAbsolutePath(), "HbO", channels);
						HbOFile = new File("HbO");
					} catch (MWException e1) {
						e1.printStackTrace();
					}
				}

				workspace.addSubject(subjectName, HbFile, HbOFile, condFile);
				list.add(subjectName);
				
				text_4.setText("");
				text_5.setText("");
				text_7.setText("");
				text_subName2.setText("");
				
			}
		});
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setText("Hatachi/Other");
		tbtmNewItem_2.setControl(composite_4);
		
		Label label_3 = new Label(composite_4, SWT.NONE);
		label_3.setText("Conditions File:");
		label_3.setBounds(38, 133, 90, 15);
		
		text_7 = new Text(composite_4, SWT.BORDER);
		text_7.setBounds(135, 130, 155, 21);
		
		Button button_1 = new Button(composite_4, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_7);
			}
		});
		button_1.setText("Browse");
		button_1.setBounds(295, 126, 75, 25);
	}
}

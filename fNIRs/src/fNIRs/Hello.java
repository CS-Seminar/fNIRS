package fNIRs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import org.eclipse.jface.dialogs.MessageDialog;
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

import com.rapidminer.operator.OperatorException;

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
	
	private String subjectName = null;
	private int sessionNum;
	private int sessionNumH;

	
	private static RapidDriver rapidDriver;
	private Text text_8;
	private Text text_9;
	private Text text_10;


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
			rapidDriver = new RapidDriver();
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
			MessageBox messageDialog = new MessageBox(shlFnirsDataProcessing, SWT.ERROR);
		    messageDialog.setText("Warning!");
		    messageDialog.setMessage("File does not exist");
		    messageDialog.open();
			return false;
		}
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
		shlFnirsDataProcessing.setText("Zombie MiNIR - fNIRs Data Processing and Analysis");
		
		fileDialog = new FileDialog(shlFnirsDataProcessing, SWT.OPEN);
		DirectoryDialog dlg = new DirectoryDialog(shlFnirsDataProcessing);
		dlg.setText("Select Workspace");
	    String selected = dlg.open(); // cannot seem to name New Folder for some reason
	    //System.out.println(selected);
	    workspace = new Workspace(selected,pre);
		
		final List list = new List(shlFnirsDataProcessing, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		list.setBounds(10, 10, 226, 460);

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
		btnClear.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnClear.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				list.deselectAll();
				indexList.clear();
			}
		});
		btnClear.setBounds(10, 476, 226, 25);
		btnClear.setText("Clear Selections");
		
		TabFolder tabFolder = new TabFolder(shlFnirsDataProcessing, SWT.NONE);
		tabFolder.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		tabFolder.setBounds(242, 10, 742, 522);
		
		TabItem tbtmLoadFiles = new TabItem(tabFolder, SWT.NONE);
		tbtmLoadFiles.setText("Load File(s) / Preprocessing");
		
		Composite composite = new Composite(tabFolder, SWT.NONE);
		composite.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.BOLD | SWT.ITALIC));
		tbtmLoadFiles.setControl(composite);

		TabFolder tabFolder_1 = new TabFolder(composite, SWT.NONE);
		tabFolder_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD | SWT.ITALIC));
		tabFolder_1.setBounds(10, 10, 718, 474);

		TabItem tbtmNewItem = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem.setText("ISS Oxyplex");

		Composite composite_3 = new Composite(tabFolder_1, SWT.NONE);
		composite_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		tbtmNewItem.setControl(composite_3);
		
		final ArrayList<Control> loadItems = new ArrayList<Control>();
		
		final Spinner spinner = new Spinner(composite_3, SWT.BORDER);
		spinner.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		spinner.setEnabled(false);
		spinner.setBounds(568, 228, 47, 25);
		loadItems.add(spinner);
		
		final Label lblOf = new Label(composite_3, SWT.NONE);
		lblOf.setAlignment(SWT.CENTER);
		lblOf.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblOf.setBounds(330, 355, 75, 21);
		lblOf.setText("1 of 1");
		loadItems.add(lblOf);
		
		final Button btnCheckButton = new Button(composite_3, SWT.CHECK);
		btnCheckButton.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnCheckButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				if (btnCheckButton.getSelection())
					spinner.setEnabled(true);
				else
					spinner.setEnabled(false);
			}
		});
		btnCheckButton.setBounds(425, 228, 134, 25);
		btnCheckButton.setText("Sliding Average:");
		loadItems.add(btnCheckButton);
		
		final Spinner num_sessions = new Spinner(composite_3, SWT.BORDER);
		num_sessions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		num_sessions.setMinimum(1);
		num_sessions.setBounds(543, 29, 47, 25);
		
		final Button btnEnter_1 = new Button(composite_3, SWT.NONE);
		btnEnter_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnEnter_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				subjectName = text_subName.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					MessageBox messageDialog = new MessageBox(shlFnirsDataProcessing, SWT.ERROR);
				    messageDialog.setText("Warning!");
				    messageDialog.setMessage("Please enter a new name");
				    messageDialog.open();
					return;
				}
				
				for (Control item : loadItems) {
					item.setVisible(true);
				}
				
				sessionNum = 1;
				
				lblOf.setText("1 of " + num_sessions.getText());
				
				text_subName.setEnabled(false);
				num_sessions.setEnabled(false);
				btnEnter_1.setEnabled(false);
			}
		});
		btnEnter_1.setBounds(596, 30, 80, 25);
		btnEnter_1.setText("Enter");
		
		Button btnEnter = new Button(composite_3, SWT.NONE);
		btnEnter.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnEnter.setBounds(330, 390, 75, 25);
		btnEnter.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent e) {
				double freq = 0;
				double hpf = 0;
				double lpf = 0;
				
				File newFile = new File(text.getText());
				Label lblFileDoesNot = null;
				if (!setExists(newFile,lblFileDoesNot))
					return;
				
				File condFile = new File(text_6.getText());
				Label label_2 = null;
				if (!setExists(condFile,label_2 ))
					return;
				
				try {
					freq = (Double.valueOf(text_1.getText())).doubleValue();
					hpf = (Double.valueOf(text_2.getText())).doubleValue();
					lpf = (Double.valueOf(text_3.getText())).doubleValue();
				}
				catch (NumberFormatException e1) {
					MessageBox messageDialog = new MessageBox(shlFnirsDataProcessing, SWT.ERROR);
				    messageDialog.setText("Warning!");
				    messageDialog.setMessage("Please fill in all frequencies");
				    messageDialog.open();
				}
				
				char slideavg = 'n';
				int interval = 0;
					
				if (btnCheckButton.getSelection()) {
					slideavg = 'y';
					interval = (Integer.valueOf(spinner.getText())).intValue();
				}
				
				if (sessionNum==1) {
					workspace.addSubject(subjectName, newFile, condFile, freq, hpf, lpf, slideavg, interval);
				}
				else {
					workspace.concatSession(subjectName, newFile, condFile, freq, hpf, lpf, slideavg, interval);
				}
				sessionNum++;
				
				if (sessionNum>Integer.valueOf(num_sessions.getText()).intValue()) {
					list.add(subjectName);
					for (Control item : loadItems) {
						item.setVisible(false);
					}
					text_subName.setEnabled(true);
					num_sessions.setEnabled(true);
					btnEnter_1.setEnabled(true);
					text_subName.setText("");
					num_sessions.setSelection(1);
				}
				else {
					lblOf.setText(sessionNum + " of " + num_sessions.getText());
				}
				
				text.setText("");
				text_6.setText("");
			}

		});
		btnEnter.setText("Add");
		loadItems.add(btnEnter);
		
		text_subName = new Text(composite_3, SWT.BORDER);
		text_subName.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_subName.setBounds(140, 30, 220, 25);
		
		Button btnBrowse = new Button(composite_3, SWT.NONE);
		btnBrowse.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnBrowse.setBounds(568, 126, 108, 25);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text);
			}
		});
		btnBrowse.setText("Browse...");
		loadItems.add(btnBrowse);
		
		Label lblSubjectName = new Label(composite_3, SWT.NONE);
		lblSubjectName.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblSubjectName.setBounds(30, 30, 104, 25);
		lblSubjectName.setText("Subject Name:");
		
		text = new Text(composite_3, SWT.BORDER);
		text.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text.setBounds(150, 125, 411, 25);
		loadItems.add(text);
		
		Label lblDataFile = new Label(composite_3, SWT.NONE);
		lblDataFile.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblDataFile.setBounds(30, 125, 67, 25);
		lblDataFile.setText("Data File:");
		loadItems.add(lblDataFile);
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_1.setText("2");
		text_1.setBounds(285, 228, 50, 25);
		loadItems.add(text_1);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel.setBounds(85, 228, 164, 25);
		lblNewLabel.setText("Sampling Frequency:");
		loadItems.add(lblNewLabel);
		
		Label lblHz = new Label(composite_3, SWT.NONE);
		lblHz.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblHz.setBounds(340, 228, 29, 25);
		lblHz.setText("Hz");
		loadItems.add(lblHz);
		
		Label lblHighPassFilter = new Label(composite_3, SWT.NONE);
		lblHighPassFilter.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblHighPassFilter.setBounds(85, 268, 196, 25);
		lblHighPassFilter.setText("High Pass Filter Frequency:");
		loadItems.add(lblHighPassFilter);
		
		text_2 = new Text(composite_3, SWT.BORDER);
		text_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_2.setText(".1");
		text_2.setBounds(285, 268, 50, 25);
		loadItems.add(text_2);
		
		Label label = new Label(composite_3, SWT.NONE);
		label.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label.setText("Hz");
		label.setBounds(340, 268, 29, 25);
		loadItems.add(label);
		
		Label lblLowPassFilter = new Label(composite_3, SWT.NONE);
		lblLowPassFilter.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblLowPassFilter.setBounds(85, 308, 196, 25);
		lblLowPassFilter.setText("Low Pass Filter Frequency:");
		loadItems.add(lblLowPassFilter);
		
		text_3 = new Text(composite_3, SWT.BORDER);
		text_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_3.setText(".01");
		text_3.setBounds(285, 308, 50, 25);
		loadItems.add(text_3);
		
		Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label_1.setText("Hz");
		label_1.setBounds(340, 308, 29, 25);
		loadItems.add(label_1);
		
		Label lblPreprocessingOptions = new Label(composite_3, SWT.NONE);
		lblPreprocessingOptions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		lblPreprocessingOptions.setBounds(30, 84, 189, 25);
		lblPreprocessingOptions.setText("Preprocessing Options:");
		loadItems.add(lblPreprocessingOptions);
		
		Label lblConditionsFile = new Label(composite_3, SWT.NONE);
		lblConditionsFile.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblConditionsFile.setBounds(30, 165, 116, 25);
		lblConditionsFile.setText("Conditions File:");
		loadItems.add(lblConditionsFile);
		
		text_6 = new Text(composite_3, SWT.BORDER);
		text_6.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_6.setBounds(150, 165, 411, 25);
		loadItems.add(text_6);
		
		Button btnBrowse_3 = new Button(composite_3, SWT.NONE);
		btnBrowse_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnBrowse_3.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_6);
			}
		});
		btnBrowse_3.setText("Browse...");
		btnBrowse_3.setBounds(568, 165, 108, 25);
		loadItems.add(btnBrowse_3);
		
		for (Control item : loadItems) {
			item.setVisible(false);
		}
		
		Label lblNumberOfSessions = new Label(composite_3, SWT.NONE);
		lblNumberOfSessions.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNumberOfSessions.setBounds(390, 30, 147, 25);
		lblNumberOfSessions.setText("Number of Sessions:");
		
		final Composite composite_4 = new Composite(tabFolder_1, SWT.NONE);
		composite_4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		composite_4.setBounds(10, 96, 694, 254);
		composite_4.setVisible(false);
		
		text_4 = new Text(composite_4, SWT.BORDER);
		text_4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_4.setBounds(146, 190, 396, 25);
		
		text_5 = new Text(composite_4, SWT.BORDER);
		text_5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_5.setBounds(146, 230, 396, 25);
		
		Button btnNewButton = new Button(composite_4, SWT.NONE);
		btnNewButton.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnNewButton.setBounds(548, 190, 127, 25);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_4);
			}
		});
		btnNewButton.setText("Browse...");
		
		Button btnBrowse_1 = new Button(composite_4, SWT.NONE);
		btnBrowse_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnBrowse_1.setBounds(548, 230, 127, 25);
		btnBrowse_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_5);
			}
		});
		btnBrowse_1.setText("Browse...");
		
		final ArrayList<Control> loadHatachi = new ArrayList<Control>();
		
		Label lblHboFile = new Label(composite_4, SWT.NONE);
		lblHboFile.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblHboFile.setBounds(30, 230, 84, 25);
		lblHboFile.setText("HbO File:");
		loadHatachi.add(lblHboFile);
		
		Label lblHbFile = new Label(composite_4, SWT.NONE);
		lblHbFile.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblHbFile.setBounds(30, 190, 84, 25);
		lblHbFile.setText("Hb File:");
		loadHatachi.add(lblHbFile);
		
		Button btnAdd = new Button(composite_4, SWT.NONE);
		btnAdd.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnAdd.setBounds(330, 390, 75, 25);
		btnAdd.setText("Add");
		loadHatachi.add(btnAdd);
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Statistical Analysis");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		
		Label lblChannelGrouping = new Label(composite_1, SWT.NONE);
		lblChannelGrouping.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD | SWT.ITALIC));
		lblChannelGrouping.setBounds(30, 30, 161, 25);
		lblChannelGrouping.setText("Channel Grouping:");
		
		text_8 = new Text(composite_1, SWT.BORDER);
		text_8.setBounds(145, 70, 439, 25);
		
		Button btnNewButton_2 = new Button(composite_1, SWT.NONE);
		btnNewButton_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
			}
		});
		btnNewButton_2.setBounds(590, 70, 116, 25);
		btnNewButton_2.setText("Browse...");
		
		Button HbCheck = new Button(composite_1, SWT.CHECK);
		HbCheck.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		HbCheck.setBounds(30, 70, 48, 25);
		HbCheck.setText("Hb");
		
		Button HbOCheck = new Button(composite_1, SWT.CHECK);
		HbOCheck.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		HbOCheck.setBounds(80, 70, 64, 25);
		HbOCheck.setText("HbO");
		
		List list_2 = new List(composite_1, SWT.BORDER);
		list_2.setBounds(30, 151, 188, 309);
		
		List list_3 = new List(composite_1, SWT.BORDER);
		list_3.setBounds(224, 151, 188, 309);
		
		Label lblNewLabel_1 = new Label(composite_1, SWT.NONE);
		lblNewLabel_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel_1.setBounds(30, 120, 55, 25);
		lblNewLabel_1.setText("Groups:");
		
		Label lblNewLabel_2 = new Label(composite_1, SWT.NONE);
		lblNewLabel_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel_2.setBounds(224, 120, 116, 25);
		lblNewLabel_2.setText("Conditions:");
		
		Label lblChunking = new Label(composite_1, SWT.NONE);
		lblChunking.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblChunking.setBounds(466, 151, 86, 25);
		lblChunking.setText("Chunking:");
		
		text_9 = new Text(composite_1, SWT.BORDER);
		text_9.setBounds(466, 182, 55, 25);
		
		Label lblNewLabel_3 = new Label(composite_1, SWT.NONE);
		lblNewLabel_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel_3.setBounds(527, 182, 161, 25);
		lblNewLabel_3.setText("chunks");
		
		Label lblNewLabel_4 = new Label(composite_1, SWT.NONE);
		lblNewLabel_4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel_4.setBounds(466, 238, 148, 25);
		lblNewLabel_4.setText("ANOVA Precision:");
		
		text_10 = new Text(composite_1, SWT.BORDER);
		text_10.setBounds(466, 269, 55, 25);
		
		Label lblDecimalPlaces = new Label(composite_1, SWT.NONE);
		lblDecimalPlaces.setText("decimal places");
		lblDecimalPlaces.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblDecimalPlaces.setBounds(527, 269, 161, 25);
		
		Button btnNewButton_3 = new Button(composite_1, SWT.NONE);
		btnNewButton_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnNewButton_3.setBounds(466, 358, 130, 25);
		btnNewButton_3.setText("ANOVA");
		
		Label lblNewLabel_5 = new Label(composite_1, SWT.NONE);
		lblNewLabel_5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNewLabel_5.setBounds(466, 327, 148, 25);
		lblNewLabel_5.setText("Processes to Run:");
		
		TabItem tbtmMachineLearning = new TabItem(tabFolder, SWT.NONE);
		tbtmMachineLearning.setText("Data Mining");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmMachineLearning.setControl(composite_2);
		
		final List list_1 = new List(composite_2, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list_1.setBounds(306, 52, 160, 395);
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ArrayList<Integer> cond_list = new ArrayList<Integer>(); 
				for (Integer item: list_1.getSelectionIndices())
					cond_list.add(item + 1);
				System.out.println(cond_list);
			}
		});
		
		btnNewButton_1.setBounds(340, 453, 84, 25);
		btnNewButton_1.setText("Select");
		for (int i=1; i<40; i++) {
			String strI = "Condition #" + i;
			list_1.add(strI);
		}

		/*
		Button for filtering
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				//rapidDriver.run();
				rapidDriver.filter(new ArrayList(Arrays.asList(2)) , 
						new File("C:\\Users\\jssmith\\Desktop\\Workspace\\subjects\\Work\\Hb"),
						new File("output"));
			}
		});
		btnNewButton_1.setBounds(46, 173, 75, 25);
		btnNewButton_1.setText("Run Process");
		 */
		
		
		Button btnRemove = new Button(shlFnirsDataProcessing, SWT.NONE);
		btnRemove.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
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
		btnRemove.setBounds(10, 507, 226, 25);
		btnRemove.setText("Remove Files");
		
		Label lblSubjectName2 = new Label(composite_4, SWT.NONE);
		lblSubjectName2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblSubjectName2.setBounds(30, 30, 105, 25);
		lblSubjectName2.setText("Subject Name:");
		
		text_subName2 = new Text(composite_4, SWT.BORDER);
		text_subName2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_subName2.setBounds(140, 30, 396, 25);
		
		final Spinner num_channels_H = new Spinner(composite_4, SWT.BORDER);
		num_channels_H.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		num_channels_H.setBounds(422, 75, 47, 25);
		loadHatachi.add(num_channels_H);
		
		Label lblNumberOfChannels = new Label(composite_4, SWT.NONE);
		lblNumberOfChannels.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblNumberOfChannels.setBounds(265, 75, 149, 25);
		lblNumberOfChannels.setText("Number of Channels:");
		loadHatachi.add(lblNumberOfChannels);
		
		final Label label_4 = new Label(composite_4, SWT.NONE);
		label_4.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label_4.setVisible(false);
		label_4.setText("File Does Not Exist");
		label_4.setBounds(306, 339, 105, 15);
		
		final Spinner num_Sessions_H = new Spinner(composite_4, SWT.BORDER);
		num_Sessions_H.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		num_Sessions_H.setMinimum(1);
		num_Sessions_H.setBounds(182, 75, 47, 25);
		loadHatachi.add(num_Sessions_H);
		
		final Label lblOf_H = new Label(composite_4, SWT.NONE);
		lblOf_H.setAlignment(SWT.CENTER);
		lblOf_H.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		lblOf_H.setText("1 of 1");
		lblOf_H.setBounds(330, 355, 74, 20);
		loadHatachi.add(lblOf_H);
		
		Button button_2 = new Button(composite_4, SWT.NONE);
		button_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				subjectName = text_subName2.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					MessageBox messageDialog = new MessageBox(shlFnirsDataProcessing, SWT.ERROR);
				    messageDialog.setText("Warning!");
				    messageDialog.setMessage("Please enter a new name");
				    messageDialog.open();
					return;
				}
				
				for (Control item : loadHatachi) {
					item.setVisible(true);
				}
				
				sessionNumH = 1;
				
				lblOf_H.setText("1 of " + num_Sessions_H.getText());
				
				text_subName.setEnabled(false);
				num_sessions.setEnabled(false);
				btnEnter_1.setEnabled(false);
			}
		});
		button_2.setText("Enter");
		button_2.setBounds(542, 30, 133, 25);

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				File condFile = new File(text_7.getText());
				if (!setExists(condFile,label_4))
					return;
				
				int channels = (Integer.valueOf(num_channels_H.getText())).intValue();
				
				File HbFile = new File(text_4.getText());
				File HbOFile = new File(text_5.getText());
				
				if (!HbFile.exists() && !HbOFile.exists()) {
					
					return;
				}

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
		loadHatachi.add(btnAdd);
		
		TabItem tbtmNewItem_2 = new TabItem(tabFolder_1, SWT.NONE);
		tbtmNewItem_2.setText("Hitachi");
		tbtmNewItem_2.setControl(composite_4);
		
		Label label_3 = new Label(composite_4, SWT.NONE);
		label_3.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label_3.setText("Conditions File:");
		label_3.setBounds(30, 270, 111, 25);
		loadHatachi.add(label_3);
		
		text_7 = new Text(composite_4, SWT.BORDER);
		text_7.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		text_7.setBounds(146, 270, 396, 25);
		loadHatachi.add(text_7);
		
		Button btnBrowse_2 = new Button(composite_4, SWT.NONE);
		btnBrowse_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		btnBrowse_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_7);
			}
		});
		btnBrowse_2.setText("Browse...");
		btnBrowse_2.setBounds(548, 270, 127, 25);
		
		Label label_5 = new Label(composite_4, SWT.NONE);
		label_5.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.NORMAL));
		label_5.setText("Number of Sessions:");
		label_5.setBounds(30, 75, 149, 25);
		
		Label label_2 = new Label(composite_4, SWT.NONE);
		label_2.setText("Preprocessing Options:");
		label_2.setFont(SWTResourceManager.getFont("Segoe UI", 12, SWT.BOLD));
		label_2.setBounds(30, 140, 189, 25);
	}
}

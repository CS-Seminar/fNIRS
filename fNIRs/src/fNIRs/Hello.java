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

			subjectMap = new HashMap<String,Subject>();
			subjectNumber = 1;
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
		tabFolder.setBounds(242, 10, 742, 522);
		
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
		
		final ArrayList<Control> loadItems = new ArrayList<Control>();
		
		Button btnBrowse = new Button(composite_3, SWT.NONE);
		btnBrowse.setBounds(510, 63, 75, 21);
		btnBrowse.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text);
			}
		});
		btnBrowse.setText("Browse");
		loadItems.add(btnBrowse);
		
		final Label lblFileDoesNot = new Label(composite_3, SWT.NONE);
		lblFileDoesNot.setBounds(591, 66, 100, 15);
		lblFileDoesNot.setText("File does not exist");
		lblFileDoesNot.setVisible(false);
		
		final Spinner spinner = new Spinner(composite_3, SWT.BORDER);
		spinner.setEnabled(false);
		spinner.setBounds(461, 181, 47, 22);
		loadItems.add(spinner);
		
		final Label lblPleaseFillIn = new Label(composite_3, SWT.NONE);
		lblPleaseFillIn.setBounds(352, 230, 147, 15);
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
		btnCheckButton.setBounds(352, 183, 103, 16);
		btnCheckButton.setText("Sliding Average");
		loadItems.add(btnCheckButton);
		
		Label lblSubjectName = new Label(composite_3, SWT.NONE);
		lblSubjectName.setBounds(76, 23, 83, 15);
		lblSubjectName.setText("Subject Name:");
		
		final Label lblChooseANew = new Label(composite_3, SWT.NONE);
		lblChooseANew.setBounds(165, 42, 110, 15);
		lblChooseANew.setText("Choose a new name");
		lblChooseANew.setVisible(false);
		
		text_subName = new Text(composite_3, SWT.BORDER);
		text_subName.setBounds(165, 20, 110, 21);
		
		final Label label_2 = new Label(composite_3, SWT.NONE);
		label_2.setVisible(false);
		label_2.setText("File does not exist");
		label_2.setBounds(591, 100, 100, 15);
		
		final Label lblOf = new Label(composite_3, SWT.NONE);
		lblOf.setBounds(370, 348, 55, 15);
		lblOf.setText("1 of 1");
		loadItems.add(lblOf);
		
		final Spinner num_sessions = new Spinner(composite_3, SWT.BORDER);
		num_sessions.setMinimum(1);
		num_sessions.setBounds(418, 20, 47, 22);
		
		final Button btnEnter_1 = new Button(composite_3, SWT.NONE);
		btnEnter_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				subjectName = text_subName.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					lblChooseANew.setVisible(true);
					return;
				}
				lblChooseANew.setVisible(false);
				
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
		btnEnter_1.setBounds(496, 18, 75, 25);
		btnEnter_1.setText("Enter");
		
		Button btnEnter = new Button(composite_3, SWT.NONE);
		btnEnter.setBounds(276, 345, 75, 21);
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
		
		text = new Text(composite_3, SWT.BORDER);
		text.setBounds(107, 63, 397, 21);
		loadItems.add(text);
		
		Label lblDataFile = new Label(composite_3, SWT.NONE);
		lblDataFile.setBounds(46, 66, 55, 15);
		lblDataFile.setText("Data File:");
		loadItems.add(lblDataFile);
		
		text_1 = new Text(composite_3, SWT.BORDER);
		text_1.setText("2");
		text_1.setBounds(256, 181, 33, 21);
		loadItems.add(text_1);
		
		Label lblNewLabel = new Label(composite_3, SWT.NONE);
		lblNewLabel.setBounds(136, 184, 116, 15);
		lblNewLabel.setText("Sampling Frequency:");
		loadItems.add(lblNewLabel);
		
		Label lblHz = new Label(composite_3, SWT.NONE);
		lblHz.setBounds(295, 184, 14, 15);
		lblHz.setText("Hz");
		loadItems.add(lblHz);
		
		Label lblHighPassFilter = new Label(composite_3, SWT.NONE);
		lblHighPassFilter.setBounds(107, 227, 147, 15);
		lblHighPassFilter.setText("High Pass Filter Frequency:");
		loadItems.add(lblHighPassFilter);
		
		text_2 = new Text(composite_3, SWT.BORDER);
		text_2.setText(".1");
		text_2.setBounds(260, 227, 29, 21);
		loadItems.add(text_2);
		
		Label label = new Label(composite_3, SWT.NONE);
		label.setText("Hz");
		label.setBounds(295, 230, 14, 15);
		loadItems.add(label);
		
		Label lblLowPassFilter = new Label(composite_3, SWT.NONE);
		lblLowPassFilter.setBounds(110, 269, 142, 15);
		lblLowPassFilter.setText("Low Pass Filter Frequency:");
		loadItems.add(lblLowPassFilter);
		
		text_3 = new Text(composite_3, SWT.BORDER);
		text_3.setText(".01");
		text_3.setBounds(260, 266, 29, 21);
		loadItems.add(text_3);
		
		Label label_1 = new Label(composite_3, SWT.NONE);
		label_1.setText("Hz");
		label_1.setBounds(295, 269, 14, 15);
		loadItems.add(label_1);
		
		Label lblPreprocessingOptions = new Label(composite_3, SWT.NONE);
		lblPreprocessingOptions.setBounds(64, 134, 130, 15);
		lblPreprocessingOptions.setText("Preprocessing Options:");
		loadItems.add(lblPreprocessingOptions);
		
		Label lblConditionsFile = new Label(composite_3, SWT.NONE);
		lblConditionsFile.setBounds(46, 100, 90, 15);
		lblConditionsFile.setText("Conditions File:");
		loadItems.add(lblConditionsFile);
		
		text_6 = new Text(composite_3, SWT.BORDER);
		text_6.setBounds(142, 97, 361, 21);
		loadItems.add(text_6);
		
		Button button = new Button(composite_3, SWT.NONE);
		button.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_6);
			}
		});
		button.setText("Browse");
		button.setBounds(510, 97, 75, 21);
		loadItems.add(button);
		
		for (Control item : loadItems) {
			item.setVisible(false);
		}
		
		Label lblNumberOfSessions = new Label(composite_3, SWT.NONE);
		lblNumberOfSessions.setBounds(295, 23, 117, 15);
		lblNumberOfSessions.setText("Number of Sessions:");
		
		final Composite composite_4 = new Composite(tabFolder_1, SWT.NONE);
		composite_4.setBounds(10, 96, 694, 254);
		composite_4.setVisible(false);
		
		text_4 = new Text(composite_4, SWT.BORDER);
		text_4.setBounds(237, 122, 173, 21);
		
		text_5 = new Text(composite_4, SWT.BORDER);
		text_5.setBounds(237, 170, 173, 21);
		
		Button btnNewButton = new Button(composite_4, SWT.NONE);
		btnNewButton.setBounds(434, 120, 75, 25);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_4);
			}
		});
		btnNewButton.setText("Browse");
		
		Button btnBrowse_1 = new Button(composite_4, SWT.NONE);
		btnBrowse_1.setBounds(434, 168, 75, 25);
		btnBrowse_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_5);
			}
		});
		btnBrowse_1.setText("Browse");
		
		final ArrayList<Control> loadHatachi = new ArrayList<Control>();
		
		Label lblHboFile = new Label(composite_4, SWT.NONE);
		lblHboFile.setBounds(170, 173, 55, 15);
		lblHboFile.setText("HbO File:");
		loadHatachi.add(lblHboFile);
		
		Label lblHbFile = new Label(composite_4, SWT.NONE);
		lblHbFile.setBounds(170, 125, 44, 15);
		lblHbFile.setText("Hb File:");
		loadHatachi.add(lblHbFile);
		
		Button btnAdd = new Button(composite_4, SWT.NONE);
		btnAdd.setBounds(295, 308, 75, 25);
		btnAdd.setText("Add");
		loadHatachi.add(btnAdd);
		
		TabItem tbtmStats = new TabItem(tabFolder, SWT.NONE);
		tbtmStats.setText("Statistical Analysis");
		
		Composite composite_1 = new Composite(tabFolder, SWT.NONE);
		tbtmStats.setControl(composite_1);
		
		TabItem tbtmMachineLearning = new TabItem(tabFolder, SWT.NONE);
		tbtmMachineLearning.setText("Data Mining");
		
		Composite composite_2 = new Composite(tabFolder, SWT.NONE);
		tbtmMachineLearning.setControl(composite_2);
		
		List list_1 = new List(composite_2, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
		list_1.setBounds(10, 10, 217, 474);
		
		Button btnNewButton_1 = new Button(composite_2, SWT.NONE);
		btnNewButton_1.setBounds(303, 339, 75, 25);
		btnNewButton_1.setText("New Button");
		for (int i=1; i<40; i++) {
			String strI = "" + i;
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
		lblSubjectName2.setBounds(65, 40, 84, 15);
		lblSubjectName2.setText("Subject Name:");
		
		text_subName2 = new Text(composite_4, SWT.BORDER);
		text_subName2.setBounds(155, 37, 144, 21);
		
		final Label lblFileDoesNot_1 = new Label(composite_4, SWT.NONE);
		lblFileDoesNot_1.setBounds(306, 149, 105, 15);
		lblFileDoesNot_1.setText("File Does Not Exist");
		lblFileDoesNot_1.setVisible(false);
		
		final Label lblFileDoesNot_2 = new Label(composite_4, SWT.NONE);
		lblFileDoesNot_2.setText("File Does Not Exist");
		lblFileDoesNot_2.setBounds(305, 197, 105, 15);
		lblFileDoesNot_2.setVisible(false);
		
		final Label lblChooseANew_1 = new Label(composite_4, SWT.NONE);
		lblChooseANew_1.setBounds(155, 64, 155, 15);
		lblChooseANew_1.setText("Choose a new subject name");
		lblChooseANew_1.setVisible(false);
		
		final Spinner num_channels_H = new Spinner(composite_4, SWT.BORDER);
		num_channels_H.setBounds(477, 37, 47, 22);
		loadHatachi.add(num_channels_H);
		
		Label lblNumberOfChannels = new Label(composite_4, SWT.NONE);
		lblNumberOfChannels.setBounds(444, 20, 115, 15);
		lblNumberOfChannels.setText("Number of Channels:");
		loadHatachi.add(lblNumberOfChannels);
		
		final Label label_4 = new Label(composite_4, SWT.NONE);
		label_4.setVisible(false);
		label_4.setText("File Does Not Exist");
		label_4.setBounds(306, 251, 105, 15);
		
		final Spinner num_Sessions_H = new Spinner(composite_4, SWT.BORDER);
		num_Sessions_H.setMinimum(1);
		num_Sessions_H.setBounds(352, 37, 47, 22);
		loadHatachi.add(num_Sessions_H);
		
		final Label lblOf_H = new Label(composite_4, SWT.NONE);
		lblOf_H.setText("1 of 1");
		lblOf_H.setBounds(393, 313, 55, 15);
		loadHatachi.add(lblOf_H);
		
		Button button_2 = new Button(composite_4, SWT.NONE);
		button_2.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				subjectName = text_subName2.getText();
				
				if (subjectName == "" || Arrays.asList(list.getItems()).contains(subjectName)) {
					lblChooseANew_1.setVisible(true);
					return;
				}
				lblChooseANew_1.setVisible(false);
				
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
		button_2.setBounds(576, 30, 75, 25);

		btnAdd.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				
				File condFile = new File(text_7.getText());
				if (!setExists(condFile,label_4))
					return;
				
				int channels = (Integer.valueOf(num_channels_H.getText())).intValue();
				
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
		tbtmNewItem_2.setText("Hatachi");
		tbtmNewItem_2.setControl(composite_4);
		
		Label label_3 = new Label(composite_4, SWT.NONE);
		label_3.setText("Conditions File:");
		label_3.setBounds(159, 227, 90, 15);
		loadHatachi.add(label_3);
		
		text_7 = new Text(composite_4, SWT.BORDER);
		text_7.setBounds(255, 224, 155, 21);
		loadHatachi.add(text_7);
		
		Button button_1 = new Button(composite_4, SWT.NONE);
		button_1.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				browse(text_7);
			}
		});
		button_1.setText("Browse");
		button_1.setBounds(434, 222, 75, 25);
		
		Label label_5 = new Label(composite_4, SWT.NONE);
		label_5.setText("Number of Sessions:");
		label_5.setBounds(321, 20, 117, 15);
	}
}

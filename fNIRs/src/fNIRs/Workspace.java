package fNIRs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Scanner;

import org.eclipse.swt.widgets.List;

import zombie.Preprocess;

import com.mathworks.toolbox.javabuilder.MWException;

public class Workspace {
	// a workspace is a directory containing files representing subjects and results of stats analysis and data mining
	
	File dir;
	File subjects;
	File stats;
	File dmining;
	File templates;
	Preprocess pre; // converted matlab code to be used for preprocessing
	/* REQUIRES MATLAB COMPILER RUNTIME */
	
	Workspace() {}
	
	Workspace(String space, Preprocess prep) {
		// assumes that if space exists, it is a valid workspace
		pre = prep;
		dir = new File(space);
		subjects = new File(dir.getAbsolutePath() + "\\subjects");
		stats = new File(dir.getAbsolutePath() + "\\stats");
		dmining = new File(dir.getAbsolutePath() + "\\dmining");
		templates = new File(dmining.getAbsolutePath() + "\\templates");
		
		// creates a new workspace if space is not already a workspace
		makeDir(dir);
		makeDir(subjects);
		makeDir(stats);
		makeDir(dmining);
		makeDir(templates);
		
		File pTemp = new File(templates.getAbsolutePath()+"\\processTemplate");
		if (!pTemp.exists()) {
			try {
				Files.copy(new File("src\\fNIRs\\processTemplate").toPath().toAbsolutePath(),pTemp.toPath().toAbsolutePath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	void makeDir(File direc) {
		if (!direc.exists())
			direc.mkdir();
	}

	void loadSubjects(List list) {
		// adds the existing subjects to the list in the gui
		for (File subject : subjects.listFiles()) {
			list.add(subject.getName());
		}
		return;
	}
	
	File addConditions(File file, File conditions) {
		// adds a conditions column to the Hb and HbO files
		// the conditions file contains lines of the form:
		//       start stop condition
		// where start to stop is a range of rows in the Hb/HbO file where condition was in effect
		// on each line, start and stop must both be larger than the preceding line
		// any rows not specified in the conditions file will be assigned the default condition 0
		File tempfile = new File("temp");
		try {
			
			BufferedReader creader = new BufferedReader(new FileReader(conditions));
			BufferedReader freader = new BufferedReader(new FileReader(file));
			Scanner scanner = new Scanner(creader);
			BufferedWriter fwriter = new BufferedWriter(new FileWriter(tempfile));
			String line;
			int row = 1;
			int start;
			int stop;
			String condition;
			
			// get rid of max condition
			if (scanner.hasNext())
				scanner.nextInt();
			while (scanner.hasNext()) {
				start = scanner.nextInt();
				stop = scanner.nextInt();
				condition = scanner.next();
				while (row<start) {
					line = freader.readLine();
					fwriter.write(line + "\t0");
					fwriter.newLine();
					row++;
				}
				while (row<=stop) {
					line = freader.readLine();
					fwriter.write(line + "\t" + condition);
					fwriter.newLine();
					row++;
				}
			}
			while ((line = freader.readLine()) != null) {
				fwriter.write(line + "\t0");
				fwriter.newLine();
			}
			
			creader.close();
			freader.close();
			fwriter.close();
			scanner.close();
			
		} catch(IOException ie) {
			ie.printStackTrace();
		}
		return tempfile;
	}
	
	void addSubject(String name, File HbFile, File HbOFile, File condFile) {
		// creates a new directory in subjects called name containing the Hb and/or HbO files
		String path = subjects.getAbsolutePath();
		File subject = new File(path + "\\" + name);
		subject.mkdir();
		File conditions = new File(path + "\\" + name + "\\conditions");
		try {
			Files.copy(condFile.toPath(),conditions.toPath(),StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
			return;
		}
		if (HbFile != null) {
			File tempFile = addConditions(HbFile, condFile);
			HbFile.delete();
			tempFile.renameTo(new File(path + "\\" + name + "\\Hb"));
		}
		if (HbOFile != null) {
			File tempFile = addConditions(HbOFile, condFile);
			HbOFile.delete();
			tempFile.renameTo(new File(path + "\\" + name + "\\HbO"));
		}
		return;
	}

	void preprocess(File origFile, double freq, double hpf, double lpf, char slideavg, int interval) {
		try {
			Object[] in = { origFile.getAbsolutePath(), freq, hpf, lpf, slideavg, interval };
			pre.preprocess_2013(in);
		} catch (MWException e1) {
			e1.printStackTrace();
		}
	}
	
	void addSubject(String name, File origFile, File condFile, double freq, double hpf, double lpf, char slideavg, int interval) {
		// adds an ISS Oxyplex subject which requires preprocessing
		preprocess(origFile, freq, hpf, lpf, slideavg, interval);
		File hbFile = new File("Hb");
		File hboFile = new File("HbO");
		addSubject(name, hbFile, hboFile, condFile);
	}
	
	void concatSession(String name, File origFile, File condFile, double freq, double hpf, double lpf, char slideavg, int interval) {
		preprocess(origFile, freq, hpf, lpf, slideavg, interval);
		File hbFile = new File("Hb");
		File hboFile = new File("HbO");
		File tempFile;
		if (hbFile.exists()) {
			tempFile = addConditions(hbFile, condFile);
			concatFiles(getHb(name),tempFile);
			hbFile.delete();
		}
		if (hboFile.exists()) {
			tempFile = addConditions(hboFile, condFile);
			concatFiles(getHbO(name),tempFile);
			hbFile.delete();
		}
	}
	
	void concatSession(String name, File hbFile, File hboFile, File condFile) {
		File tempFile;
		if (hbFile.exists()) {
			tempFile = addConditions(hbFile, condFile);
			concatFiles(getHb(name),tempFile);
			hbFile.delete();
		}
		if (hboFile.exists()) {
			tempFile = addConditions(hboFile, condFile);
			concatFiles(getHbO(name),tempFile);
			hbFile.delete();
		}
	}
	
	void concatFiles(File file, File other) {
		try {
			BufferedReader oreader = new BufferedReader(new FileReader(other));
			BufferedWriter fwriter = new BufferedWriter(new FileWriter(file,true));
			String line;
			while((line = oreader.readLine()) != null) {
				fwriter.write(line);
				fwriter.newLine();
			}
			fwriter.close();
			oreader.close();
			other.delete();
		}
		catch(IOException ie) {
			ie.printStackTrace();
		}
	}

	File getHb(String name) {
		// returns the Hb file for subject name
		File HbPath = new File(subjects.getAbsolutePath() + "\\" + name + "\\Hb");
		if (HbPath.exists())
			return HbPath;
		else
			return null;
	}
	
	File getHbO(String name) {
		// returns the HbO file for subject name
		File HbOPath = new File(subjects.getAbsolutePath() + "\\" + name + "\\HbO");
		if (HbOPath.exists())
			return HbOPath;
		else
			return null;
	}
	
	boolean deleteDirectory(File dir) {
		if (dir.exists()) {
			if (dir.isDirectory()) {
				for (File file : dir.listFiles()) {
					if (file.isDirectory()) {
						deleteDirectory(file);
					}
					else {
						file.delete();
					}
				}
			}
			return dir.delete();
		}
		else
			return false;
	}
	
	void removeSubject(String name) {
		String path = subjects.getAbsolutePath();
		File subject = new File(path + "\\" + name);
		deleteDirectory(subject);
	}
	
	int getMaxCond(String name) {
		File conditions = new File(subjects.getAbsolutePath() + "\\" + name + "\\conditions");
		try {
			BufferedReader cFile = new BufferedReader(new FileReader(conditions));
			Scanner scanner = new Scanner(cFile);
			int n;
			if (scanner.hasNext())
				n = scanner.nextInt();
			else 
				n = 0;
			scanner.close();
			return n;
		}
		catch (IOException ioe) {
			return 0;
		}
	}
	
	File getHbOutput(String name) {
		String path = subjects.getAbsolutePath();
		File HbOutput = new File(path + "\\" + name + "\\Hb_output.txt");
		return HbOutput;
	}
	
	File getHbOOutput(String name) {
		String path = subjects.getAbsolutePath();
		File HbOOutput = new File(path + "\\" + name + "\\HbO_output.txt");
		return HbOOutput;
	}
	
	File getRMInput(String name) {
		String path = subjects.getAbsolutePath();
		File RMInputFile = new File(path + "\\" + name + "\\rm_input_file.xls");
		return RMInputFile;
	}
	
	String getStatsPath() {
		return stats.getAbsolutePath();
	}
	
	File getTemplate(String name) {
		return new File(templates.getAbsolutePath() + "\\" + name);
	}
	
	public static void main(String[] args) {
		File first = new File("first.txt");
		File second = new File("second.txt");
		
		Workspace w = new Workspace();
		
		w.concatFiles(first,second);
	}
}

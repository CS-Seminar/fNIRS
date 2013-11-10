package fNIRs;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import org.eclipse.swt.widgets.List;

import preprocess_2.Preprocess;

import com.mathworks.toolbox.javabuilder.MWException;

public class Workspace {
	// a workspace is a directory containing files representing subjects and results of stats analysis and data mining
	
	File dir;
	File subjects;
	File stats;
	File dmining;
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
		
		// creates a new workspace if space is not already a workspace
		makeDir(dir);
		makeDir(subjects);
		makeDir(stats);
		makeDir(dmining);
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
		if (HbFile.exists()) {
			File tempFile = addConditions(HbFile, condFile);
			HbFile.delete();
			tempFile.renameTo(new File(path + "\\" + name + "\\Hb"));
		}
		if (HbOFile.exists()) {
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
	
		/*try {
			Object[] in = { origFile.getAbsolutePath(), freq, hpf, lpf, slideavg, interval };
			pre.preprocess_2013(in);
			File hbFile = new File("Hb");
			File hboFile = new File("HbO");
			addSubject(name, hbFile, hboFile, condFile);
		} catch (MWException e1) {
			e1.printStackTrace();
		}*/
			
	}
	
	void concatSession(String name, File origFile, File condFile, double freq, double hpf, double lpf, char slideavg, int interval) {
		preprocess(origFile, freq, hpf, lpf, slideavg, interval);
		File hbFile = new File("Hb");
		File hboFile = new File("HbO");
		File tempFile = addConditions(hbFile, condFile);
		concatFiles(getHb(name),tempFile);
		tempFile = addConditions(hboFile, condFile);
		concatFiles(getHbO(name),tempFile);
		hbFile.delete();
		hboFile.delete();
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
	
	public static void main(String[] args) {
		File first = new File("first.txt");
		File second = new File("second.txt");
		
		Workspace w = new Workspace();
		
		w.concatFiles(first,second);
	}
}

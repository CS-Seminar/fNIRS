package fNIRs;

import java.io.File;
import java.util.HashMap;

public class Workspace {
	
	File dir;
	File subjects;
	File stats;
	File dmining;
	
	Workspace() {}
	
	Workspace(String space) {
		dir = new File(space);
		subjects = new File(dir.getAbsolutePath() + "\\subjects");
		stats = new File(dir.getAbsolutePath() + "\\stats");
		dmining = new File(dir.getAbsolutePath() + "\\dmining");
		
		if (!dir.exists()) {
			dir.mkdir();
			subjects.mkdir();
			stats.mkdir();
			dmining.mkdir();
		}
	}
	
	HashMap<String,Subject> loadSubjects(HashMap<String,Subject> subjectMap) {
		for (File subject : subjects.listFiles()) {
			String name = subject.getName();
			File HbFile = new File(subject.getAbsolutePath() + "\\Hb");
			File HbOFile = new File(subject.getAbsolutePath() + "\\HbO");
			Subject newSubject = new Subject(name, HbFile, HbOFile, this);
			subjectMap.put(name, newSubject);
		}
		return subjectMap;
	}
	
	void addSubject(String name, File HbFile, File HbOFile) {
		String path = subjects.getAbsolutePath();
		File subject = new File(path + "\\" + name);
		subject.mkdir();
		HbFile.renameTo(new File(path + "\\" + name + "\\Hb"));
		HbOFile.renameTo(new File(path + "\\" + name + "\\HbO"));
	}

	File getHb(String name) {
		return new File(subjects.getAbsolutePath() + "\\" + name + "\\Hb"); 
	}
	
	File getHbO(String name) {
		return new File(subjects.getAbsolutePath() + "\\" + name + "\\HbO"); 
	}
	
}
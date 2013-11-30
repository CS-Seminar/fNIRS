package fNIRs;

import java.io.File;

import com.mathworks.toolbox.javabuilder.MWException;

import zombie.Preprocess;

public class Subject {
	
	String name;
	File origFile;
	File HbFile;
	File HbOFile;
	Workspace workspace;
	Boolean preprocessed;
	
	Subject(){}
	
	Subject(String name, File orig, Workspace space) {
		this.name = name;
		origFile = orig;
		workspace = space;
	}
	
	Subject(File orig, File Hb, File HbO) {
		origFile = orig;
		HbFile = Hb;	
		HbOFile = HbO;
	}
	
	Subject(String filename, File Hb, File HbO, Workspace space) {
		name = filename;
		workspace = space;
		//workspace.addSubject(name, Hb, HbO);
		origFile = null;
		HbFile = workspace.getHb(name);
		HbOFile = workspace.getHbO(name);
	}
	
	void setName(String name) {
		this.name = name;
	}

	void setOrigFile(File origFile) {
		this.origFile = origFile;
	}
	
	void setHbFile(File HbFile) {
		this.HbFile = HbFile;
	}
	
	void setHbOFile(File HbOFile) {
		this.HbOFile = HbOFile;
	}
	
	String getName() {
		return name;
	}

	 File getOrigFile() {
		return origFile;
	}
	
	File getHbFile() {
		return HbFile;
	}
	
	File getHbOFile() {
		return HbOFile;
	}
	
	void preprocess(Preprocess pre, double freq, double hpf, double lpf, char slideavg, int interval) {
		
		try {
			Object[] in = { origFile.toString(), freq, hpf, lpf, slideavg, interval };
			pre.preprocess_2013(in);
			File hbFile = new File("Hb");
			File hboFile = new File("HbO");
			//workspace.addSubject(name, hbFile, hboFile);
			this.HbFile = workspace.getHb(name);
			this.HbOFile = workspace.getHbO(name);
		} catch (MWException e1) {
			e1.printStackTrace();
		}
			
	}

}

package fNIRs;

import java.io.File;

import com.mathworks.toolbox.javabuilder.MWException;

import preprocess_2013.Preprocess;

public class Subject {
	
	String name;
	File origFile;
	File HbFile;
	File HbOFile;
	Boolean preprocessed;
	
	Subject(){}
	
	Subject(String name, File orig) {
		this.name = name;
		File dir = new File(System.getProperty("user.dir") + "\\" + name);
		dir.mkdir();
		origFile = orig;
	}
	
	Subject(File orig, File Hb, File HbO) {
		origFile = orig;
		HbFile = Hb;	
		HbOFile = HbO;
	}
	
	Subject(String filename, File Hb, File HbO) {
		name = filename;
		String path = System.getProperty("user.dir") + "\\" + name;
		File dir = new File(path);
		dir.mkdir();
		origFile = null;
		Hb.renameTo(new File(path + "\\Hb"));
		HbO.renameTo(new File(path + "\\HbO"));
		HbFile = Hb;
		HbOFile = HbO;
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
	
	void preprocess( Preprocess pre, double freq, double hpf, double lpf, char slideavg, int interval) {
		
		try {
			Object[] in = { origFile.toString(), freq, hpf, lpf, slideavg, interval };
			pre.preprocess_2013(in);
			File hbFile = new File("Hb");
			File hboFile = new File("HbO");
			String path = hbFile.getAbsolutePath().substring(0,hbFile.getAbsolutePath().length()-2);
			hbFile.renameTo(new File(path + "\\" + name + "\\Hb"));
			hboFile.renameTo(new File(path + "\\" + name + "\\HbO"));
			this.HbFile = hbFile;
			this.HbOFile = hboFile;
		} catch (MWException e1) {
			e1.printStackTrace();
		}
			
	}

}

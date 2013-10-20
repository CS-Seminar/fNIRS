package fNIRs;

import java.io.File;

import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

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
		origFile = orig;
	}
	
	Subject(File orig, File Hb, File HbO) {
		origFile = orig;
		HbFile = Hb;	
		HbOFile = HbO;
	}
	
	Subject(String filename, File Hb, File HbO) {
		name = filename;
		origFile = null;
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
			Object[] result = null;
			result = pre.preprocess_2013(2,in);
			MWNumericArray HbInfo = (MWNumericArray)result[0];
			this.HbFile = new File("Hb");
			this.HbOFile = new File("HbO");
		} catch (MWException e1) {
			e1.printStackTrace();
		}
			
	}

}

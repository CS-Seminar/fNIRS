package fNIRs;

import java.io.File;

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
	
	Subject(File Hb, File HbO) {
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

}

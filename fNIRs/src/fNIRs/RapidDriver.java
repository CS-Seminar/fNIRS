package fNIRs;

import java.io.*;

import com.rapidminer.RapidMiner.ExecutionMode;
import com.rapidminer.RapidMiner;
import com.rapidminer.Process;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.tools.XMLException;


public class RapidDriver {

	//Workspace Dir
	Process process;
	
	RapidDriver() throws IOException, XMLException{
		//Initialize Rapid Miner and create process specified in process file 
		//Running within another program		
		RapidMiner.setExecutionMode(ExecutionMode.EMBEDDED_WITHOUT_UI);	
		File myFile = new File("./src/fNirs/process");

		// MUST BE INVOKED BEFORE ANYTHING ELSE !!!
		RapidMiner.init();
	    // create the process from the process file
		this.process = new Process(myFile);

	}
	void run() throws OperatorException{
		this.process.run();
	}
/*	public static void main(String[] argv) throws Exception {
		//		String rapidMinerHome = "C:\\Program Files\\Rapid-I\\RapidMiner5\\lib";
		//System.out.println(System.getProperty("user.dir"));
		
		//		String rapidMinerHome = "/fNIRs";
//		System.setProperty("rapidminer.home", rapidMinerHome);
		
	
		File myFile = new File("./src/fNirs/process");
		System.out.print(myFile.getCanonicalPath());
		// MUST BE INVOKED BEFORE ANYTHING ELSE !!!
		RapidMiner.init();

		
	    // create the process from the command line argument file
		Process process = new Process(myFile);

	    // run the process on the input
	    process.run();
	}
*/	
}

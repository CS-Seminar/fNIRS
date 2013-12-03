package fNIRs;

import java.io.*;
import java.util.*;

import com.rapidminer.RapidMiner.ExecutionMode;
import com.rapidminer.RapidMiner;
import com.rapidminer.Process;
import com.rapidminer.operator.Operator;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.operator.IOContainer;
import com.rapidminer.operator.io.ExcelExampleSource;
import com.rapidminer.tools.XMLException;



import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.poifs.filesystem.POIFSFileSystem; 

import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;

public class RapidDriver {

	RapidDriver() throws IOException, XMLException{
		//Initialize Rapid Miner and create process specified in process file 
		//Running within another program		
		RapidMiner.setExecutionMode(ExecutionMode.EMBEDDED_WITHOUT_UI);

		// MUST BE INVOKED BEFORE ANYTHING ELSE !!!
		RapidMiner.init();
	    
	}

	private void writeRow(BufferedWriter fwriter, String[] entries){
		try {
			for(int i = 0; i < (entries.length - 1); i++){
				fwriter.write(entries[i]); 
				fwriter.write(", ");
			}
			fwriter.write(entries[entries.length -1]);
		}	
		catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	void empty(File output){
		try {
			BufferedWriter fwriter;
			fwriter = new BufferedWriter(new FileWriter(output));
			fwriter.write("0");
			fwriter.newLine();	
			fwriter.write("0");
			fwriter.newLine();
			fwriter.close();
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
		
	}
	
	void filter(ArrayList<Integer> conditions, File input, File output){
		//Filter a Hb/Hbo file with conditions listed in the last col
		//Result only contains rows with conditions in conditions array
		BufferedReader freader;
		BufferedWriter fwriter;
		try {
			freader = new BufferedReader(new FileReader(input));
			fwriter = new BufferedWriter(new FileWriter(output));
			String line = freader.readLine();
			//Remove leading and trailing ws
			line = line.trim();
			
			String [] entries = line.split("(,|\\s)+");
			
			//Write Col Headers
			for(int i = 1; i < (entries.length); i++){
				fwriter.write("Ch" + i);
				fwriter.write(", ");	
			}
			fwriter.write("Label");
			fwriter.newLine();
			//Check to see if the condition is in conditions
			if (conditions.contains(Integer.parseInt(entries[(entries.length)-1]))){
				//Write First Row
				writeRow(fwriter, entries);
				fwriter.newLine();
			}
			while ((line = freader.readLine()) != null){
				//Remove leading and trailing ws
				line = line.trim();
				entries = line.split("(,|\\s)+");
				//Check to see if the condition is in conditions
				if (conditions.contains(Integer.parseInt(entries[(entries.length)-1]))){
					writeRow(fwriter, entries);
					fwriter.newLine();
				}
			}
			
			fwriter.close();
			freader.close();
			
		} catch (IOException ie) {
			// TODO Auto-generated catch block
			ie.printStackTrace();
		}
		
	}
	
	File generateProcess(File input, File template) throws FileNotFoundException, IOException{
	//Input: input file and process template
	//Count the number of parameter cols in xls input file.
	//Generate and return the correct process file
		POIFSFileSystem fs = new POIFSFileSystem(new FileInputStream(input));
	    HSSFWorkbook wb = new HSSFWorkbook(fs);
	    HSSFSheet sheet = wb.getSheetAt(0);
	    int noOfColumns = sheet.getRow(0).getLastCellNum();

	    String paramList = "<list key=\"data_set_meta_data_information\">\n";
	    
		for(int i = 0; i < (noOfColumns - 1); i++){
			String nextLine = String.format("\t<parameter key=\"%d\" value=\"%d.true.real.attribute\"/>\n", i, i + 1);
			paramList = paramList + nextLine;
		}
		
		paramList = paramList + String.format("\t<parameter key=\"%d\" value=\"label.true.polynominal.label\"/>\n", noOfColumns - 1);
		paramList = paramList + "\t</list>";
		
		String content = new Scanner(template).useDelimiter("\\Z").next();
		content = content.replace("#!#TEMPLATE#!#", paramList);
		File process = new File("process");
		
		BufferedWriter fwriter;
		fwriter = new BufferedWriter(new FileWriter(process));
		fwriter.write(content);
		fwriter.newLine();
		fwriter.close();
		return process;

	}
	
	void run(File input, File processFile, File output) throws OperatorException{
		
		
		try {
			Process process = new Process(processFile);
			//ExampleSet resultSet1 = null;
			
			Operator op = process.getOperator("Read Excel");
			op.setParameter(ExcelExampleSource.PARAMETER_EXCEL_FILE, input.getAbsolutePath());
			
			op = process.getOperator("Write Excel");
			op.setParameter(ExcelExampleSource.PARAMETER_EXCEL_FILE, output.getAbsolutePath());
		
			process.run();
			//IOContainer ioResult = process.run();
			//processFile.delete();
			
			/* Outputting example set just use RM write file.		
			if (ioResult.getElementAt(0) instanceof ExampleSet) {
				resultSet1 = (ExampleSet)ioResult.getElementAt(0);
				for (Example example : resultSet1) {
					Iterator<Attribute> allAtts = example.getAttributes().allAttributes();
					while(allAtts.hasNext()) {
						Attribute a = allAtts.next();
						if (a.isNumerical()) {
							double value = example.getValue(a);
							System.out.println(value);
                       	} 	
						else {
                            		String value = example.getValueAsString(a);
                                    System.out.println(value);
                        }
                     }
				}
            }*/
		
		} catch (IOException | XMLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	/*
	public static void main(String[] argv) throws Exception {
		RapidMiner.setExecutionMode(ExecutionMode.EMBEDDED_WITHOUT_UI);

		// MUST BE INVOKED BEFORE ANYTHING ELSE !!!
		RapidMiner.init();
		File myFile = new File("C:\\Users\\jssmith\\Desktop\\Workspace\\subjects\\yo mamma\\rm_input_file.xls");
		File tmpl = new File("./src/fNirs/processTemplate");
		File process = generateProcess(myFile, tmpl);
		//run(myFile, process);
	}
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

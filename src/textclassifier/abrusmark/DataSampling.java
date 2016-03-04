/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
 */

package textclassifier.abrusmark;

import java.io.*;

import java.util.*;
import java.util.Map.Entry;

public class DataSampling {

	private final double TRAINING = 0.8;
	private final double TEST = 0.2;
	private int cutOff = 0;
	
	public String trainingSet;
	public String testSet;
	
	
	private int rowsOfData;
	private int trainingExamples;
	private int testExamples;
	private int testSampleLines;
	
	private String line;
	public List<String> dataSet;
	private List<String> shuffledList;
	private String textData;
	
	
	public Map<String, List<String>> tempTrainingSet;
	public Map<String, List<String>> tempTestSet;
	public List<String> trainingSetList;
	public List<String> testSetList;
	String[] cols;
	List<String> listLineTemp;
	Map<String, List<String>> linesByClass = new HashMap<String, List<String>>();
	public DataSampling(String option, String freq, String csvFile) {
		textData = csvFile;
		cutOff = Integer.valueOf(freq);
		splitData();
	}
	
	public void splitData() {
		rowsOfData = 0;
		BufferedReader fileRead = null;
		try {
			fileRead = new BufferedReader(new FileReader(textData));
			fileRead.readLine();
			
			dataSet = new ArrayList<String>();
			
			while ((line = fileRead.readLine()) != null) {
				listLineTemp = new ArrayList<String>();
				rowsOfData++;
				dataSet.add(line);
				listLineTemp.add(line);
				
				cols = line.split(",");
				
				if (!linesByClass.containsKey(cols[3])) {
					
					linesByClass.put(cols[3], listLineTemp);
				}
				else {
					linesByClass.get(cols[3]).add(line);
				}
				
			
			}
			
			for (Map.Entry<String, List<String>> entry : linesByClass.entrySet()) {
				 shuffledList = entry.getValue();
				 Collections.shuffle(shuffledList);
				 entry.setValue(shuffledList);
			}
			
			trainingExamples = (int) Math.round(TRAINING * rowsOfData);
			testExamples = (int) Math.round(TEST * rowsOfData);
			
			Collections.shuffle(dataSet);
			testSetList = new ArrayList<String>();
			trainingSetList = new ArrayList<String>();
			List<String> temp;
			
			
			for (Map.Entry<String, List<String>> entry : linesByClass.entrySet()) {
				temp = new ArrayList<String>();
				
				if (entry.getValue().size() > cutOff) {
					testSampleLines = (int) Math.round(entry.getValue().size() * TEST);
					
					temp = entry.getValue().subList(0, testSampleLines);
					for (String string : temp) {
						testSetList.add(string);
					}
					
					temp = new ArrayList<String>();
					
					temp = entry.getValue().subList(testSampleLines+1, entry.getValue().size());
					for (String string : temp) {
						trainingSetList.add(string);
					}
					
				}
				

			}
			
			
//			trainingSetList = dataSet.subList(0, trainingExamples);
//			testSetList = dataSet.subList(trainingExamples, dataSet.size());
			
//			for (String string : testSetList) {
//				System.out.println(string);
//			}

			trainingSet = "";
			testSet = "";
			

			for (String string : trainingSetList) {
				trainingSet += string + "\n";
			}
			
			for (String string : testSetList) {
				testSet += string + "\n";		
			}
		
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e2) {
			e2.printStackTrace();
		} finally {
			if (fileRead != null) {
				try {
					fileRead.close();
				} catch (IOException e3) {
					e3.printStackTrace();
				}
			}
		}
		if (fileRead != null) {
			try {
				fileRead.close();
			} catch (IOException e3) {
				e3.printStackTrace();
			}
		}
	}
}
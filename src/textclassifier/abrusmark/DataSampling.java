/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.io.*;

import java.util.*;
import java.util.Map.Entry;

public class DataSampling {

	private final double TRAINING = 0.8;
	private final double TEST = 0.2;
	private int cutOfflow = 0;
	private int cutOffhigh = Integer.MAX_VALUE;

	public String trainingSet;
	public String testSet;


	private int rowsOfData;
	private int trainingExamples;
	private int testExamples;
	private int testSampleLines;

	private String line;
	private String option;
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
	public DataSampling(String option, String lowfreq, String highfreq, String csvFile) {
		this.option = option;
		textData = csvFile;
		cutOfflow = Integer.valueOf(lowfreq);
		cutOffhigh = Integer.valueOf(highfreq);
		System.out.println(cutOfflow);
		System.out.println(cutOffhigh);
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

			if (option.equalsIgnoreCase("traintest")) {
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

					if (entry.getValue().size() >= cutOfflow && entry.getValue().size() <= cutOffhigh) {
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
						System.out.println(entry.getKey() + ": "+temp.size());

					}



				}
				System.out.println("\n");

				trainingSet = "";
				testSet = "";


				for (String string : trainingSetList) {
					trainingSet += string + "\n";
				}

				for (String string : testSetList) {
					testSet += string + "\n";		
				}
			}

			else if (option.equalsIgnoreCase("train")) {
				trainingSetList = new ArrayList<String>();
				List<String> temp;

				for (Map.Entry<String, List<String>> entry : linesByClass.entrySet()) {
					temp = new ArrayList<String>();
					temp = entry.getValue();
					
					if (entry.getValue().size() >= cutOfflow && entry.getValue().size() <= cutOffhigh) {
						
						for (String string : temp) {
							trainingSetList.add(string);
						}
						System.out.println(entry.getKey() + ": "+temp.size());
					}
				}
				
				trainingSet = "";
				
				for (String string : trainingSetList) {
					trainingSet += string + "\n";
				}
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
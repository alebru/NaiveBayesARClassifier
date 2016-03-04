/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
 */

package textclassifier.abrusmark;

import java.io.IOException;
import java.util.*;

public class NB_Classifier {
	Features ClassifierFeatures;
	private Map<Integer, String> goldStandard;
	Map<Map<String, Double>, List<Double>> tempValues;
	Map<Integer, List<String>> testData;
	Map<Integer, String> results;
	Map<Integer, String> compare;
	List<Double> listProbValues;
	Double tempProbValue;
	Double priorProbClass;
	Double probabilityCalc;
	String bestClass;
	double maxProbability;
	
	NB_Model ClassifierModel;
	
	String[] cmds;

	
	public NB_Classifier(String[] commands, FeatureSelector featureSet) {
		ClassifierFeatures = featureSet.returnFeatures();
		cmds = commands;
	}

	public NB_Classifier(String[] commands, TextProcessor documentsProcessed) {
		testData = documentsProcessed.getTermsByDocuments();
		goldStandard = documentsProcessed.getGoldstandard();
		cmds = commands;
	}
	
	public NB_Classifier(String[] commands, TextProcessorStringdata documentsProcessed) {
		testData = documentsProcessed.getTermsByDocuments();
		goldStandard = documentsProcessed.getGoldstandard();
		cmds = commands;
	}

	public void run() {
		if (cmds[0].equalsIgnoreCase("train")) {
			train();
		}

		if (cmds[0].equalsIgnoreCase("test")) {
			test();
		}
	}

	public void train() {
		ClassifierModel = new NB_Model(ClassifierFeatures);
		System.out.println("Training classifier...");
		try {
			ClassifierModel.saveToDisk();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void test() {
		try {
			ClassifierModel = new NB_Model();
			ClassifierModel.loadFromDisk();
		} catch (ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 

		System.out.println("Running test...");
		
		results = new HashMap<Integer, String>();
		compare = new HashMap<Integer, String>();
		for (Map.Entry<Integer, List<String>> entry : testData.entrySet()) {
			
			tempValues = new HashMap<Map<String, Double>, List<Double>>();
//			System.out.println("\n");
//			System.out.println(entry.getKey());
			maxProbability = 0.0;
			
			for (Map.Entry<Map<String, Double>, Map<String, Double>> entry2 : ClassifierModel.trainingModel.featureFreqByClass.entrySet()) {
				listProbValues = new ArrayList<Double>();
//				System.out.println(entry2.getKey().values().toArray()[0] + " - WHATYTOTTY!?!?");
				priorProbClass = Double.valueOf(entry2.getKey().values().toArray()[0].toString());
//				System.out.println("PriorProbabilityOfClass: " + priorProbClass);
				
				for (String wordTest : entry.getValue()) {
					tempProbValue = 0.0;
					
					for (String wordModel : entry2.getValue().keySet()) {

						if (wordTest.equalsIgnoreCase(wordModel)) {
							tempProbValue = entry2.getValue().get(wordModel);
							listProbValues.add(tempProbValue);
						}
					}
					
				}
				
//				System.out.println(entry2.getKey().keySet().toString() + listProbValues.size());
				probabilityCalc = 0.0;
				//Spara värden från test mot alla klasser
				if (listProbValues.size() > 0) {
					
					probabilityCalc = priorProbClass;
//					System.out.println(listProbValues.get(0));
					for (int i = 0; i < listProbValues.size(); i++) {
						probabilityCalc += listProbValues.get(i);
					}
//					probabilityCalc *= priorProbClass;
//					System.out.println(entry2.getKey().keySet().toString() + " :  " + probabilityCalc);
					

					
//					tempValues.put(entry2.getKey(), probabilityCalc);
//					System.out.println(listProbValues);
//					System.out.println(listProbValues.size());
//					System.out.println(Collections.max(listProbValues));
				}
//				bestClass = entry2.getKey().keySet().toArray()[0].toString();
				
				if (probabilityCalc < maxProbability) {
					maxProbability = probabilityCalc;
					bestClass = entry2.getKey().keySet().toArray()[0].toString();
				}

				//Hämta ut högsta värdet i listan - klassifikation klar

				//				for (Map.Entry<String, List<Double>> entry3 : tempValues.entrySet()) {
				//					
//					System.out.println(entry3.getKey() + " : " + entry3.getValue());
//					}
				}
			results.put(entry.getKey(), bestClass);
//				System.out.println(entry.getKey() + ": " + bestClass);
//				results.put(entry.getKey(), Collections.max(tempValues));
			}
		Double correct = 0.0;
		Double accuracy = 0.0;
		
		for (Map.Entry<Integer, String> entry : results.entrySet()) {
			
			for (Map.Entry<Integer, String> entry2: goldStandard.entrySet()) {
				
				if ((entry.getKey().equals(entry2.getKey()) && (entry.getValue().equals(entry2.getValue())))) {
					correct++;
				}
			}
		}
		System.out.println("Accuracy: " + correct/results.size());
			
			
//			System.out.println(entry.getKey() + ": "+entry.getValue());
		}
}

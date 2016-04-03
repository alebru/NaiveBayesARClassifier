/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
 */

package textclassifier.abrusmark;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class NB_Classifier {
	Features ClassifierFeatures;
	private Goldstandard goldstandard;
	private ResultSet classifiedreports;
	Map<Map<String, Double>, List<Double>> tempValues;
	Map<Integer, List<String>> testData;
	Map<String, Integer> sortedClasses;
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

	public NB_Classifier(String[] commands, FeatureSelectorTfidf featureSet) {
		ClassifierFeatures = featureSet.returnFeatures();
		cmds = commands;
	}

	public NB_Classifier(String[] commands, TextProcessor documentsProcessed) {
		testData = documentsProcessed.getTermsByDocuments();
		goldstandard = documentsProcessed.getGoldstandard();
		classifiedreports = documentsProcessed.getClassifiedreports();
		cmds = commands;
	}

	public NB_Classifier(String[] commands, TextProcessorStringdata documentsProcessed) {
		testData = documentsProcessed.getTermsByDocuments();
		classifiedreports = documentsProcessed.getClassifiedreports();
		sortedClasses = documentsProcessed.getSortedClasses();
		goldstandard = documentsProcessed.getGoldstandard();
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
		System.out.println("\n");
		System.out.println("Training classifier...");
		try {
			ClassifierModel.saveToDisk();
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void test() {

		try {
			ClassifierModel = new NB_Model();
			ClassifierModel.loadFromDisk();
		} catch (ClassNotFoundException | IOException e) {
			e.getMessage();
		} 
		System.out.println("\n");
		System.out.println("Running classifier...");

		results = new HashMap<Integer, String>();
		compare = new HashMap<Integer, String>();
		for (Map.Entry<Integer, List<String>> entry : testData.entrySet()) {

			tempValues = new HashMap<Map<String, Double>, List<Double>>();
			maxProbability = 0.0;

			for (Map.Entry<Map<String, Double>, Map<String, Double>> entry2 : ClassifierModel.trainingModel.featureFreqByClass.entrySet()) {
				listProbValues = new ArrayList<Double>();

				priorProbClass = Double.valueOf(entry2.getKey().values().toArray()[0].toString());


				for (String wordTest : entry.getValue()) {
					tempProbValue = 0.0;

					for (String wordModel : entry2.getValue().keySet()) {

						if (wordTest.equalsIgnoreCase(wordModel)) {
							tempProbValue = entry2.getValue().get(wordModel);
							listProbValues.add(tempProbValue);
						}
					}

				}

				probabilityCalc = 0.0;
				
				/* The results of the probability calculations for each class are stored in listProbValues */
				
				if (listProbValues.size() > 0) {

					probabilityCalc = priorProbClass;

					for (int i = 0; i < listProbValues.size(); i++) {
						probabilityCalc += listProbValues.get(i);
					}

				}

				/* Checks to see if new probability value > previous probability, updates maxProbability accordingly*/

				if (ClassifierModel.trainingModel.termWeightMethod.equalsIgnoreCase("tfidf")) {
					if (probabilityCalc > maxProbability) {
						maxProbability = probabilityCalc;
						bestClass = entry2.getKey().keySet().toArray()[0].toString();
					}
				}
				else {
					if (probabilityCalc < maxProbability) {
						maxProbability = probabilityCalc;
						bestClass = entry2.getKey().keySet().toArray()[0].toString();
					}
				}
			}
			results.put(entry.getKey(), bestClass);
		}
		Double correct = 0.0;
		Double accuracy = 0.0;

		for (Map.Entry<Integer, String> entry : results.entrySet()) {

			for (BugReport bug : goldstandard.ClassedReports) {
				if (entry.getKey().equals(bug.bug_id) && (entry.getValue().equals(bug.bug_developer))) {
					classifiedreports.AddReport(bug);
					correct++;
					}
				}
			}

		accuracy = correct/results.size();
		double percent = Math.round((100.0 * accuracy));

		
		System.out.println("\n");
		System.out.println("Input: " + cmds[1]);
		System.out.println("\n");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("<<<" +"\t" + "Model settings" +"\t" + ">>>");
		System.out.println("\n");
		System.out.println("#Documents in training data: " + "\t" + ClassifierModel.trainingModel.featureFreqAllClasses);
		System.out.println("#Classes in training data: " + "\t" + ClassifierModel.trainingModel.getAllClassesFreq().size());
		System.out.println("\n");
		System.out.println("Terms/document in training data (avg.): " + "\t" + (ClassifierModel.trainingModel.featureLengthOfDocs/ClassifierModel.trainingModel.featureFreqAllClasses));
		System.out.println("\n");
		System.out.println("Cut-off (lower - upper):" +"\t" + ClassifierModel.trainingModel.cutOfflow+" - " +ClassifierModel.trainingModel.cutOffhigh);
		System.out.println("Term-weighting:" +"\t" + "\t" +ClassifierModel.trainingModel.termWeightMethod);
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("<<<" +"\t" + "Test data" +"\t" + ">>>");
		System.out.println("\n");
		System.out.println("#Documents in test data: " +"\t" + testData.size());
		System.out.println("#Classes in test data: " +"\t" + ClassifierModel.trainingModel.getAllClassesFreq().size());
		System.out.println("\n");
		System.out.println("\n");
		System.out.println("<<<" +"\t" + "Results" +"\t" + ">>>");
		System.out.println("\n");
		System.out.println("Accuracy:" +"\t" + "\t" + String.format("%.0f %%",percent)+ "  (" + accuracy+")");
		System.out.println("--------------------------------------------------------------------------");
		System.out.println("\n");
		
		try(PrintWriter out = new PrintWriter("classified_bugreports.csv")  ){
		    out.println(classifiedreports.toString());
		} catch (FileNotFoundException e) {
			e.getLocalizedMessage();
		}
	}
}

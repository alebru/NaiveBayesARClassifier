/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

public class FeatureSelector
{
	private static List<String> allDocuments;
	private static Map<String, List<String>> documentsByClass;
	private static HashSet<String> uniqueDocuments;
	private static Map<Integer, List<String>> termsByDocuments;
	private static Goldstandard goldstandard;
	private Features featureSet;

	private Map<String, Integer> allDocumentsFreq;
	private Map<String, Integer> allClassesFreq;
	private Map<String, Double> classProb;
	private Map<String, Integer> documentsByClassFreq;
	private Map<String, Integer> termsByAllDocumentsCount;
	private Map<String, Double> termsInDocumentsCount;
	private ArrayList<List<String>> fullDocuments;
	
	int termCount;
	double docLengthSum;
	
	String classLabel;
	double tempCountWordsInClass;
	double classProbabilityValue;

	double classProbability;
	double termProbability;
	
	String termWeightMethod;
	
	static {
		FeatureSelector.allDocuments = null;
		FeatureSelector.documentsByClass = null;
		FeatureSelector.uniqueDocuments = null;
	}
	public FeatureSelector(String termWeights, String limitLow, String limitHigh, TextProcessor processedDocuments) {
		allDocumentsFreq = new HashMap<String, Integer>();
		documentsByClassFreq = new HashMap<String, Integer>();
		termsByAllDocumentsCount = new HashMap<String, Integer>();
		termWeightMethod = termWeights;


		featureSet = new Features(termWeightMethod, limitLow, limitHigh);
		allClassesFreq = processedDocuments.getSortedClasses();
		featureSet.setAllClassesFreq(allClassesFreq);
//		FeatureSelector.goldstandard = processedDocuments.getGoldstandard();
		FeatureSelector.allDocuments = processedDocuments.getAllDocuments();
		FeatureSelector.documentsByClass = processedDocuments.getDocumentsByClass();
		FeatureSelector.uniqueDocuments = processedDocuments.getUniqueDocuments();
		FeatureSelector.termsByDocuments = processedDocuments.getTermsByDocuments();
		fullDocuments = processedDocuments.getFullDocuments();
		
		docLengths();
		freqClasses();
		featuresByClass(); 
		featuresProbPerClass();
	}
	public FeatureSelector(String termWeights,  String limitLow, String limitHigh, TextProcessorStringdata processedDocuments) {
		allDocumentsFreq = new HashMap<String, Integer>();
		documentsByClassFreq = new HashMap<String, Integer>();
		termsByAllDocumentsCount = new HashMap<String, Integer>();
		goldstandard = processedDocuments.getGoldstandard();
		termWeightMethod = termWeights;
		
		featureSet = new Features(termWeightMethod, limitLow, limitHigh);
		allClassesFreq = processedDocuments.getSortedClasses();
		featureSet.setAllClassesFreq(allClassesFreq);
		FeatureSelector.allDocuments = processedDocuments.getAllDocuments();
		FeatureSelector.documentsByClass = processedDocuments.getDocumentsByClass();
		FeatureSelector.uniqueDocuments = processedDocuments.getUniqueDocuments();
		FeatureSelector.termsByDocuments = processedDocuments.getTermsByDocuments();
		fullDocuments = processedDocuments.getFullDocuments();
		
		docLengths();
		freqClasses();
		featuresByClass(); 
		featuresProbPerClass();
	}

	public Features returnFeatures() {
		return featureSet;
	}
	
	public void docLengths() {
		docLengthSum = 0.0;
		for (List<String> document : fullDocuments) {
			docLengthSum += document.size();
		}
		featureSet.setLengthOfDocs(docLengthSum);
	}

	public void freqClasses() {
		for (Map.Entry<String, Integer> entry : allClassesFreq.entrySet()) {
			featureSet.featureFreqAllClasses += entry.getValue();
			featureSet.freqOfClass.put(entry.getKey(), entry.getValue().doubleValue());
		}

		for (Map.Entry<String, Double> entry : featureSet.freqOfClass.entrySet()) {
			classProbability = Math.log(entry.getValue()/featureSet.featureFreqAllClasses);
			entry.setValue(classProbability);
		}


	}

	public void featuresByClass() {
		for (Map.Entry<String,List<String>> entry : documentsByClass.entrySet()) {
			termsInDocumentsCount = new HashMap<String, Double>();
			classProbabilityValue = 0.0;
			classProb = new HashMap<String, Double>();
			classLabel = entry.getKey();

			for (String string : entry.getValue()) {
				Integer count = Collections.frequency(entry.getValue(), string);

				termsInDocumentsCount.put(string, count.doubleValue() );
			}
			classProbabilityValue = featureSet.freqOfClass.get(classLabel).doubleValue();
			classProb.put(classLabel, classProbabilityValue);
			featureSet.featureFreqByClass.put(classProb, termsInDocumentsCount);
		}
	}

	public void featuresProbPerClass() {
		for (Map.Entry<Map<String, Double> , Map<String, Double>> entry : featureSet.featureFreqByClass.entrySet()) {

			for (Map.Entry<String, Double> innerEntry : entry.getValue().entrySet()) {
				tempCountWordsInClass = 0;
				for (Map.Entry<String, Double> wordFreqInClass: entry.getValue().entrySet()) {
					tempCountWordsInClass += wordFreqInClass.getValue();
				}
				termProbability = Math.log((innerEntry.getValue()+1)/((tempCountWordsInClass)+(uniqueDocuments.size())));
				innerEntry.setValue(termProbability);   			
			}
		}

	}
}

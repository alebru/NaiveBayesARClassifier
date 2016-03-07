/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.List;

public class FeatureSelectorTfidf
{
	private static List<String> allDocuments;
	private static Map<String, List<String>> documentsByClass;
	private static HashSet<String> uniqueDocuments;
	private static Map<Integer, List<String>> termsByDocuments;
	private static Map<Integer, String> goldstandard;
	private Map<String, Integer> termFreqAllDocuments;
	private Features featureSet;

	private Map<String, Integer> allDocumentsFreq;
	private Map<String, Integer> allClassesFreq;
	private Map<String, Double> classProb;
	private Map<String, Integer> documentsByClassFreq;
	private Map<String, Integer> termsByAllDocumentsCount;
	private Map<String, Double> termsInDocumentsCount;
	
	int termCount;

	String classLabel;
	double tempCountWordsInClass;
	double classProbabilityValue;
	
	double classProbability;
	double termProbability;

	static {
		FeatureSelectorTfidf.allDocuments = null;
		FeatureSelectorTfidf.documentsByClass = null;
		FeatureSelectorTfidf.uniqueDocuments = null;
	}
	public FeatureSelectorTfidf(TextProcessor processedDocuments) {
		allDocumentsFreq = new HashMap<String, Integer>();
		documentsByClassFreq = new HashMap<String, Integer>();
		termsByAllDocumentsCount = new HashMap<String, Integer>();
		
		
		featureSet = new Features();
		allClassesFreq = processedDocuments.getSortedClasses();
		
		FeatureSelectorTfidf.goldstandard = processedDocuments.getGoldstandard();
		FeatureSelectorTfidf.allDocuments = processedDocuments.getAllDocuments();
		FeatureSelectorTfidf.documentsByClass = processedDocuments.getDocumentsByClass();
		FeatureSelectorTfidf.uniqueDocuments = processedDocuments.getUniqueDocuments();
		FeatureSelectorTfidf.termsByDocuments = processedDocuments.getTermsByDocuments();
		freqClasses();
		featuresByClass(); 
		featuresProbPerClass();
	}
	public FeatureSelectorTfidf(TextProcessorStringdata processedDocuments) {
		allDocumentsFreq = new HashMap<String, Integer>();
		documentsByClassFreq = new HashMap<String, Integer>();
		termsByAllDocumentsCount = new HashMap<String, Integer>();
		goldstandard = processedDocuments.getGoldstandard();
		
		featureSet = new Features();
		allClassesFreq = processedDocuments.getSortedClasses();
		featureSet.setAllClassesFreq(allClassesFreq);
		termFreqAllDocuments = processedDocuments.getTermFreqAllDocuments();

		FeatureSelectorTfidf.allDocuments = processedDocuments.getAllDocuments();
		FeatureSelectorTfidf.documentsByClass = processedDocuments.getDocumentsByClass();
		FeatureSelectorTfidf.uniqueDocuments = processedDocuments.getUniqueDocuments();
		FeatureSelectorTfidf.termsByDocuments = processedDocuments.getTermsByDocuments();

		freqClasses();
		featuresByClass(); 
		featuresProbPerClass();
	}
	
	public Features returnFeatures() {
		return featureSet;
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
				
				termsInDocumentsCount.put(string, count.doubleValue());
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
			
				termProbability = (innerEntry.getValue() * Math.log((uniqueDocuments.size()/termFreqAllDocuments.get(innerEntry.getKey()))));
				innerEntry.setValue(termProbability);   			
			}	
		}

	}

	public void printstuff() {
		System.out.println(featureSet.featureFreqByClass);

	}
}

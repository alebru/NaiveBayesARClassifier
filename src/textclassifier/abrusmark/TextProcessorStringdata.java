/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Scanner;
import java.util.HashSet;
import java.util.List;

public class TextProcessorStringdata {
	private int numDocuments;
	private int numClasses;
	private int countDocs;
	private int uniqueClassesCount;
	private int removedTokens;
	private int newFreqValue;
	
	private final String splitter = ",";
	private String line;
	private String textData;


	private String[] tempDoc;
	private Map<Integer, String> goldstandard = new HashMap<Integer, String>();

	private List<String> allClasses;
	private List<String> uniqueClasses;

	private HashSet<String> uniqueDocuments;
	private Map<String, Integer> sortedClasses;
	private Map<String, List<String>> documentsByClass;
	private Map<Integer, List<String>> termsByDocuments;
	private Map<String, Integer> termFreqAllDocuments;
	private ArrayList<String> allDocuments;
	private ArrayList<String> scrappedTokens;

	List<String> stopWords = new ArrayList<>(Arrays.asList("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));
	private List<String> tempTermList = null;

	public TextProcessorStringdata(String option, String weightMethod, final String stringData) {
		countDocs = 0;
		uniqueClassesCount = 0;
		allClasses = new ArrayList<String>();
		uniqueClasses = new ArrayList<String>();
		termsByDocuments = new HashMap<Integer, List<String>>();
		termFreqAllDocuments = new HashMap<String, Integer>();
		uniqueDocuments = null;
		sortedClasses = new HashMap<String, Integer>();
		documentsByClass = new HashMap<String, List<String>>();
		allDocuments = new ArrayList<String>();
		scrappedTokens = new ArrayList<String>();
		textData = stringData;
		tokenize(option, weightMethod);
		returnDocuments();
	}

	/* Method for tokenizing lines in the String passed to the constructor and stored
	 * in 'textData'
	 */
	
	public void tokenize(String option, String weightMethod) {
		removedTokens = 0;
		Scanner dataSc = null;
		dataSc = new Scanner(textData);
		
		/* While (data.Sc.hasNextLine == true) */
		
		while ((dataSc.hasNextLine())) {
			tempTermList = new ArrayList<String>();
			++countDocs;
			line = dataSc.nextLine();
			
			/* Split the line into columns, use "," as delimiter */
			
			final String[] cols = line.split(",");

			for (int i = 0; i < 1; ++i) {
				allClasses.add(cols[3]);
				tempDoc = cols[6].split(" ");
				
				/* If documentsByClass does not contain the class (String),
				 * the class is added to  'documentsByClass with the corresponding 
				 * document in the training/test dataset. The terms (strings) in the document
				 * are added to 'allDocuments' (ArrayList<String>) as string values.
				 */
				
				if (!documentsByClass.containsKey(cols[3])) {
					documentsByClass.put(cols[3], new ArrayList<String>());
					for (String string : tempDoc) {
						string = string.replaceAll(
								"^((https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|;]])|[^0-9a-zA-Z\\/\\.\\-\\:]",
								"");
						string = string.replaceAll("\"", "");
						string = string.toLowerCase();

						/* Stop word removal - if option 'freq'
						 * has been passed as argument (no TF-IDF, only frequencies)*/

						if(!stopWords.contains(string) && (weightMethod.equalsIgnoreCase("freq") || option.equalsIgnoreCase("test"))) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
						}

						/* TF-IDF weight calculation (if option 'tfidf' passed as argument)
						 * The sum of documents containing words in vocabulary */

						if (weightMethod.equalsIgnoreCase("tfidf") && option.equalsIgnoreCase("train")) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
							
							
							if (termFreqAllDocuments.containsKey(string)) {
								newFreqValue = 0;
								newFreqValue = termFreqAllDocuments.get(string);
								newFreqValue += 1;
								termFreqAllDocuments.put(string, newFreqValue);
							}

							else {
								termFreqAllDocuments.put(string, 1);
							}
							

						}

					}
					
					/* The id of the current document and the class
					 * of corresponding to the document are added to 
					 * goldstandard (HashMap<Integer, String>)
					 */
					
					String idOfDoc = cols[0].replaceAll("\"", "");
					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
				} 
				
				/* If documentsByClass does contain the class (String),
				 * The terms (strings) in the document are added to 
				 * 'allDocuments' (ArrayList<String>) as string values.
				 */
				
				else {
					for (String string : tempDoc) {
						string = string.replaceAll(
								"^((https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|;]])|[^0-9a-zA-Z\\/\\.\\-\\:]",
								"");
						string = string.replaceAll("\"", "");
						string = string.toLowerCase();
						if(!stopWords.contains(string) && (weightMethod.equalsIgnoreCase("freq") || option.equalsIgnoreCase("test"))) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
						}

						/* TF-IDF weight calculation (if option 'tfidf' passed as argument)
						 * The sum of documents containing words in vocabulary */

						if (weightMethod.equalsIgnoreCase("tfidf") && option.equalsIgnoreCase("train")) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
							if (termFreqAllDocuments.containsKey(string)) {
								newFreqValue = 0;
								newFreqValue = termFreqAllDocuments.get(string);
								newFreqValue += 1;
								termFreqAllDocuments.put(string, newFreqValue);
							}
							else {
								termFreqAllDocuments.put(string, 1);
							}
						}
					}
					
					/* The id of the current document and the class
					 * of corresponding to the document are added to 
					 * goldstandard (HashMap<Integer, String>)
					 */
					
					String idOfDoc = cols[0].replaceAll("\"", "");
					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
				}

				if (option.equalsIgnoreCase("test")) {
					Collections.sort(tempTermList);
					//					String idOfDoc = cols[0].replaceAll("\"", "");
					termsByDocuments.put(Integer.valueOf(cols[0]), tempTermList);
				}

			}
			
			/* For uniqueClasses (List<String>) - if the current documents corresponding class
			 * is not stored in uniqueClasses, the value of 'cols[3]' (the class name) is added
			 */
			
			if (!uniqueClasses.contains(cols[3])) {
				uniqueClasses.add(cols[3]);
			}
		}

		for (final String string2 : uniqueClasses) {
			sortedClasses.put(string2, Collections.frequency(allClasses, string2));
			sortedClasses = MapUtil.sortByValue(sortedClasses);
		}
		uniqueDocuments = new HashSet<String>(allDocuments);
		dataSc.close();
	}

	public List<String> getAllClasses() {
		return allClasses;
	}

	public void setAllClasses(List<String> allClasses) {
		this.allClasses = allClasses;
	}

	public Map<Integer, String> getGoldstandard() {
		return goldstandard;
	}

	public List<String> getUniqueClasses() {
		return uniqueClasses;
	}

	public void setUniqueClasses(List<String> uniqueClasses) {
		this.uniqueClasses = uniqueClasses;
	}

	public HashSet<String> getUniqueDocuments() {
		return uniqueDocuments;
	}

	public void setUniqueDocuments(HashSet<String> uniqueDocuments) {
		this.uniqueDocuments = uniqueDocuments;
	}

	public Map<String, Integer> getSortedClasses() {
		return sortedClasses;
	}

	public void setSortedClasses(Map<String, Integer> sortedClasses) {
		this.sortedClasses = sortedClasses;
	}

	public Map<String, List<String>> getDocumentsByClass() {
		return documentsByClass;
	}

	public void setDocumentsByClass(Map<String, List<String>> documentsByClass) {
		this.documentsByClass = documentsByClass;
	}

	public Map<Integer, List<String>> getTermsByDocuments() {
		return termsByDocuments;
	}

	public Map<String, Integer> getTermFreqAllDocuments() {
		return termFreqAllDocuments;
	}

	public void setTermsByDocuments(Map<Integer, List<String>> termsByDocuments) {
		this.termsByDocuments = termsByDocuments;
	}

	public ArrayList<String> getAllDocuments() {
		return allDocuments;
	}

	public void setAllDocuments(ArrayList<String> allDocuments) {
		this.allDocuments = allDocuments;
	}

	public TextProcessorStringdata returnDocuments() {
		return this;
	}
}

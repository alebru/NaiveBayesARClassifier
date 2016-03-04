/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
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
	private ArrayList<String> allDocuments;
	private ArrayList<String> scrappedTokens;

	List<String> stopWords = new ArrayList<>(Arrays.asList("a", "about", "above", "above", "across", "after", "afterwards", "again", "against", "all", "almost", "alone", "along", "already", "also","although","always","am","among", "amongst", "amoungst", "amount",  "an", "and", "another", "any","anyhow","anyone","anything","anyway", "anywhere", "are", "around", "as",  "at", "back","be","became", "because","become","becomes", "becoming", "been", "before", "beforehand", "behind", "being", "below", "beside", "besides", "between", "beyond", "bill", "both", "bottom","but", "by", "call", "can", "cannot", "cant", "co", "con", "could", "couldnt", "cry", "de", "describe", "detail", "do", "done", "down", "due", "during", "each", "eg", "eight", "either", "eleven","else", "elsewhere", "empty", "enough", "etc", "even", "ever", "every", "everyone", "everything", "everywhere", "except", "few", "fifteen", "fify", "fill", "find", "fire", "first", "five", "for", "former", "formerly", "forty", "found", "four", "from", "front", "full", "further", "get", "give", "go", "had", "has", "hasnt", "have", "he", "hence", "her", "here", "hereafter", "hereby", "herein", "hereupon", "hers", "herself", "him", "himself", "his", "how", "however", "hundred", "ie", "if", "in", "inc", "indeed", "interest", "into", "is", "it", "its", "itself", "keep", "last", "latter", "latterly", "least", "less", "ltd", "made", "many", "may", "me", "meanwhile", "might", "mill", "mine", "more", "moreover", "most", "mostly", "move", "much", "must", "my", "myself", "name", "namely", "neither", "never", "nevertheless", "next", "nine", "no", "nobody", "none", "noone", "nor", "not", "nothing", "now", "nowhere", "of", "off", "often", "on", "once", "one", "only", "onto", "or", "other", "others", "otherwise", "our", "ours", "ourselves", "out", "over", "own","part", "per", "perhaps", "please", "put", "rather", "re", "same", "see", "seem", "seemed", "seeming", "seems", "serious", "several", "she", "should", "show", "side", "since", "sincere", "six", "sixty", "so", "some", "somehow", "someone", "something", "sometime", "sometimes", "somewhere", "still", "such", "system", "take", "ten", "than", "that", "the", "their", "them", "themselves", "then", "thence", "there", "thereafter", "thereby", "therefore", "therein", "thereupon", "these", "they", "thickv", "thin", "third", "this", "those", "though", "three", "through", "throughout", "thru", "thus", "to", "together", "too", "top", "toward", "towards", "twelve", "twenty", "two", "un", "under", "until", "up", "upon", "us", "very", "via", "was", "we", "well", "were", "what", "whatever", "when", "whence", "whenever", "where", "whereafter", "whereas", "whereby", "wherein", "whereupon", "wherever", "whether", "which", "while", "whither", "who", "whoever", "whole", "whom", "whose", "why", "will", "with", "within", "without", "would", "yet", "you", "your", "yours", "yourself", "yourselves", "the"));
	private List<String> tempTermList = null;
	
	public TextProcessorStringdata(String option, final String stringData) {
		countDocs = 0;
		uniqueClassesCount = 0;
		allClasses = new ArrayList<String>();
		uniqueClasses = new ArrayList<String>();
		termsByDocuments = new HashMap<Integer, List<String>>();
		uniqueDocuments = null;
		sortedClasses = new HashMap<String, Integer>();
		documentsByClass = new HashMap<String, List<String>>();
		allDocuments = new ArrayList<String>();
		scrappedTokens = new ArrayList<String>();
		textData = stringData;
		tokenize(option);
		System.out.println(getUniqueClasses().size());
		returnDocuments();
	}
	

	public void tokenize(String option) {
		removedTokens = 0;
		Scanner dataSc = null;
		dataSc = new Scanner(textData);

		while ((dataSc.hasNextLine())) {
			tempTermList = new ArrayList<String>();
			++countDocs;
			line = dataSc.nextLine();

			final String[] cols = line.split(",");
			
			for (int i = 0; i < 1; ++i) {
				allClasses.add(cols[3]);
				tempDoc = cols[6].split(" ");
				
				if (!documentsByClass.containsKey(cols[3])) {
					documentsByClass.put(cols[3], new ArrayList<String>());
					for (String string : tempDoc) {
						string = string.replaceAll(
								"^((https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|;]])|[^0-9a-zA-Z\\/\\.\\-\\:]",
								"");
						string = string.replaceAll("\"", "");
						string = string.toLowerCase();
						if(!stopWords.contains(string)) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
						}
					}
					String idOfDoc = cols[0].replaceAll("\"", "");
					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
				} else {
					for (String string : tempDoc) {
						string = string.replaceAll(
								"^((https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|;]])|[^0-9a-zA-Z\\/\\.\\-\\:]",
								"");
						string = string.replaceAll("\"", "");
						string = string.toLowerCase();
						if(!stopWords.contains(string)) {
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
							}
						}
					String idOfDoc = cols[0].replaceAll("\"", "");
					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
				}

				if (option.equalsIgnoreCase("test")) {
					Collections.sort(tempTermList);
//					String idOfDoc = cols[0].replaceAll("\"", "");
					termsByDocuments.put(Integer.valueOf(cols[0]), tempTermList);
				}

			}
			
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
		
//		if (option.equalsIgnoreCase("train")) {
//			for (Map.Entry<String, Integer> entry : sortedClasses.entrySet()) {
//				System.out.println(entry.getKey() + " " + entry.getValue());
//			}
//		}
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

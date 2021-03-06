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
	
	private List<String> allClasses;
	private List<String> uniqueClasses;

	private HashSet<String> uniqueDocuments;
	private Map<String, Integer> sortedClasses;
	private Map<String, List<String>> documentsByClass;
	private Map<Integer, List<String>> termsByDocuments;
	private Map<String, Integer> termFreqAllDocuments;
	private ArrayList<List<String>> fullDocuments;
	private ArrayList<String> allDocuments;
	private ArrayList<String> scrappedTokens;
	
	private Goldstandard goldstandard = new Goldstandard();
	private ResultSet classifiedreports = new ResultSet();
	BugReport bug;
	
	List<String> stopWords = new ArrayList<>(Arrays.asList("about","above","across","after","again","against","all","almost","alone","along","already","also","although","always","among","an","and","another","any","anybody","anyone","anything","anywhere","are","area","areas","around","as","ask","asked","asking","asks","at","away","back","backed","backing","backs","be","became","because","become","becomes","been","before","began","behind","being","beings","best","better","between","big","both","but","by","came","can","cannot","case","cases","certain","certainly","clear","clearly","come","could","did","differ","different","differently","do","does","done","down","down","downed","downing","downs","during","each","early","either","end","ended","ending","ends","enough","even","evenly","ever","every","everybody","everyone","everything","everywhere","face","faces","fact","facts","far","felt","few","find","finds","first","for","four","from","full","fully","further","furthered","furthering","furthers","gave","general","generally","get","gets","give","given","gives","go","going","good","goods","got","great","greater","greatest","group","grouped","grouping","groups","had","has","have","having","he","her","here","herself","high","high","high","higher","highest","him","himself","his","how","however","if","important","in","interest","interested","interesting","interests","into","is","it","its","itself","just","keep","keeps","kind","knew","know","known","knows","large","largely","last","later","latest","least","less","let","lets","like","likely","long","longer","longest","made","make","making","man","many","may","me","member","members","men","might","more","most","mostly","mr","mrs","much","must","my","myself","necessary","need","needed","needing","needs","never","new","new","newer","newest","next","no","nobody","non","noone","not","nothing","now","nowhere","number","numbers","of","off","often","old","older","oldest","on","once","one","only","open","opened","opening","opens","or","order","ordered","ordering","orders","other","others","our","out","over","part","parted","parting","parts","per","perhaps","place","places","point","pointed","pointing","points","possible","present","presented","presenting","presents","problem","problems","put","puts","quite","rather","really","right","right","room","rooms","said","same","saw","say","says","second","seconds","see","seem","seemed","seeming","seems","sees","several","shall","she","should","show","showed","showing","shows","side","sides","since","small","smaller","smallest","so","some","somebody","someone","something","somewhere","state","states","still","still","such","sure","take","taken","than","that","the","their","them","then","there","therefore","these","they","thing","things","think","thinks","this","those","though","thought","thoughts","three","through","thus","to","today","together","too","took","toward","turn","turned","turning","turns","two","under","until","up","upon","us","use","used","uses","very","want","wanted","wanting","wants","was","way","ways","we","well","wells","went","were","what","when","where","whether","which","while","who","whole","whose","why","will","with","within","without","work","worked","working","works","would","y","year","years","yet","you","young","younger","youngest","your","yours"));
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
		fullDocuments = new ArrayList<List<String>>();
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
			
			bug = new BugReport(Integer.parseInt(cols[0]), cols[6], cols[3], cols[1], cols[2]);
			goldstandard.AddReport(bug);
			
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
						
						if (!stopWords.contains(string) && weightMethod.equalsIgnoreCase("tfidf") && option.equalsIgnoreCase("train")) {
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
					
//					String idOfDoc = cols[0].replaceAll("\"", "");
//					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
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

						if (!stopWords.contains(string) && weightMethod.equalsIgnoreCase("tfidf") && option.equalsIgnoreCase("train")) {
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
					
//					String idOfDoc = cols[0].replaceAll("\"", "");
//					goldstandard.put(Integer.valueOf(idOfDoc), cols[3]);
				}

				if (option.equalsIgnoreCase("test") || option.equalsIgnoreCase("train")) {
					Collections.sort(tempTermList);
					termsByDocuments.put(Integer.valueOf(cols[0]), tempTermList);
					fullDocuments.add(tempTermList);
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

	public Goldstandard getGoldstandard() {
		return goldstandard;
	}

	public ResultSet getClassifiedreports() {
		return classifiedreports;
	}

	public void setClassifiedreports(ResultSet classifiedreports) {
		this.classifiedreports = classifiedreports;
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
	
	public ArrayList<List<String>> getFullDocuments() {
		return fullDocuments;
	}

	public TextProcessorStringdata returnDocuments() {
		return this;
	}
}

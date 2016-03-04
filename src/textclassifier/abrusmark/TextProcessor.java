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
import java.util.Map;
import java.util.HashSet;
import java.util.List;

public class TextProcessor {
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

	private List<String> tempTermList = null;

	public TextProcessor(String option, final String csvFile) {
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
		textData = csvFile;

		tokenize(option);
		System.out.println(getUniqueClasses().size());
		returnDocuments();
	}

	public void tokenize(String option) {
		removedTokens = 0;
		BufferedReader fileRead = null;
		try {
			fileRead = new BufferedReader(new FileReader(textData));
			fileRead.readLine();

			while ((line = fileRead.readLine()) != null) {
				tempTermList = new ArrayList<String>();
				++countDocs;

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
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
						}
						goldstandard.put(Integer.valueOf(cols[0]), cols[3]);
					} else {
						for (String string : tempDoc) {
							string = string.replaceAll(
									"^((https?|ftp|file)://[-A-Z0-9+&@#/%?=~_|!:,.;]*[-A-Z0-9+&@#/%=~_|;]])|[^0-9a-zA-Z\\/\\.\\-\\:]",
									"");
							string = string.replaceAll("\"", "");
							string = string.toLowerCase();
							allDocuments.add(string);
							documentsByClass.get(cols[3]).add(string);
							tempTermList.add(string);
						}
						goldstandard.put(Integer.valueOf(cols[0]), cols[3]);
					}

					if (option.equalsIgnoreCase("test")) {
						Collections.sort(tempTermList);
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
			
//			for (Map.Entry<Integer, String> entry : goldstandard.entrySet()) {
//				System.out.println(entry.getKey()+ " :  " +entry.getValue());
//			}
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

	public TextProcessor returnDocuments() {
		return this;
	}
}

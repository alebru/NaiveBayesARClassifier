/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.util.HashMap;
import java.util.Map;

public class Features  implements java.io.Serializable{
	public int featureNumberOfDocs; 
	public int featureFreqAllClasses;
	private Map<String, Integer> allClassesFreq;
	public Map<String, Double> freqOfClass;
	
	public Map<Map<String, Double>, Map<String, Double>> featureFreqByClass;
	
	public Features() {
		featureNumberOfDocs = 0;
		featureFreqAllClasses = 0;
		
		featureFreqByClass = new HashMap<>();
		freqOfClass = new HashMap<>();
	}
	
	public Map<String, Integer> getAllClassesFreq() {
		return allClassesFreq;
	}

	public void setAllClassesFreq(Map<String, Integer> allClassesFreq) {
		this.allClassesFreq = allClassesFreq;
	}
	
}

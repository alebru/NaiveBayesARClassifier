/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Features  implements java.io.Serializable{
	public int featureNumberOfDocs;
	public double featureLengthOfDocs;
	public int featureFreqAllClasses;
	private Map<String, Integer> allClassesFreq;
	public Map<String, Double> freqOfClass;
	
	String termWeightMethod;
	String cutOfflow;
	String cutOffhigh; 
	
	public Map<Map<String, Double>, Map<String, Double>> featureFreqByClass;

	public Features(String termWeights, String limitLow, String limitHigh) {
		featureNumberOfDocs = 0;
		featureFreqAllClasses = 0;
		featureFreqByClass = new HashMap<>();
		freqOfClass = new HashMap<>();
		
		cutOfflow = limitLow;
		cutOffhigh = limitHigh;
		termWeightMethod = termWeights;
	}

	public Map<String, Integer> getAllClassesFreq() {
		return allClassesFreq;
	}
	public double getLengthOfDocs() {
		return featureLengthOfDocs;
	}
	
	public void setLengthOfDocs(double lengthCalc) {
		featureLengthOfDocs = lengthCalc;
	}
	public void setAllClassesFreq(Map<String, Integer> allClassesFreq) {
		this.allClassesFreq = allClassesFreq;
	}

}

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
<<<<<<< HEAD

=======
	
>>>>>>> 0603d267cee85045b1710802b6e777567f0affe8
	public Map<Map<String, Double>, Map<String, Double>> featureFreqByClass;

	public Features() {
		featureNumberOfDocs = 0;
		featureFreqAllClasses = 0;

		featureFreqByClass = new HashMap<>();
		freqOfClass = new HashMap<>();
	}
<<<<<<< HEAD

=======
	
>>>>>>> 0603d267cee85045b1710802b6e777567f0affe8
	public Map<String, Integer> getAllClassesFreq() {
		return allClassesFreq;
	}

	public void setAllClassesFreq(Map<String, Integer> allClassesFreq) {
		this.allClassesFreq = allClassesFreq;
	}
<<<<<<< HEAD

=======
	
>>>>>>> 0603d267cee85045b1710802b6e777567f0affe8
}

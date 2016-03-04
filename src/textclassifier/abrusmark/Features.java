/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
 */
package textclassifier.abrusmark;

import java.util.HashMap;
import java.util.Map;

public class Features  implements java.io.Serializable{
	public int featureNumberOfDocs; 
	public int featureFreqAllClasses;
	public Map<String, Double> freqOfClass;
	public Map<Map<String, Double>, Map<String, Double>> featureFreqByClass;
	
	public Features() {
		featureNumberOfDocs = 0;
		featureFreqAllClasses = 0;
		
		featureFreqByClass = new HashMap<>();
		freqOfClass = new HashMap<>();
	}
	
}

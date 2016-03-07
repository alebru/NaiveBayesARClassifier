/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */

package textclassifier.abrusmark;

import java.util.*;
import java.io.*;

public class NB_Model {
	
	Features trainingModel;
	Map<Integer, List<String>> testData;
	
	public NB_Model(Features featureSet) {
		trainingModel = featureSet;
	}
	
	public NB_Model(TextProcessor documentsProcessed) {
		testData = documentsProcessed.getTermsByDocuments();
	}
	
	public NB_Model() {
	}
	
	public void saveToDisk() throws IOException {
		FileOutputStream f_out = null;
		try {
			f_out = new FileOutputStream("model.dat");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Write object with ObjectOutputStream
		ObjectOutputStream obj_out = new ObjectOutputStream (f_out);
		
		// Write object out to disk
		System.out.println("Saved model");
		obj_out.writeObject(trainingModel);
		
		f_out.close();
		obj_out.close();
	}
	
	public void loadFromDisk() throws ClassNotFoundException, IOException {
		// Read from disk using FileInputStream
		FileInputStream f_in = new FileInputStream("model.dat");

		// Read object using ObjectInputStream
		ObjectInputStream obj_in = new ObjectInputStream (f_in);

		// Read an object
		Object obj = obj_in.readObject();
		
		if (obj instanceof Features) {
			// Cast object to a Vector
//			Vector vec = (Features) obj;
			// Do something with vector....
			Features mod = (Features) obj;
			trainingModel = mod;
			System.out.println("Loaded model");
		}
	}
}
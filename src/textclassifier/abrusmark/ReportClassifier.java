/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com / alebr310 at student.liu.se)
 * 
 */

package textclassifier.abrusmark;

public class ReportClassifier
{
	//Lista med resultat
	public static String[] commands;

	public static void main(final String[] args) {

		commands = new String[3];

		if (args[0].equalsIgnoreCase("traintest")) {
			final DataSampling sampleAndSplit = new DataSampling(args[0], args[2], args[1]);
			
			final TextProcessorStringdata trainingSet = new TextProcessorStringdata("train", sampleAndSplit.trainingSet);
			final TextProcessorStringdata trainingSetProcessed = trainingSet.returnDocuments();
			
			final FeatureSelector featureSet = new FeatureSelector(trainingSetProcessed);
			final NB_Classifier classifierTrainInstance = new NB_Classifier(commands, featureSet);
			classifierTrainInstance.train();
		
			final TextProcessorStringdata testSet = new TextProcessorStringdata("test", sampleAndSplit.testSet);
			final NB_Classifier classifierTestInstance = new NB_Classifier(commands, testSet);
			classifierTestInstance.test();
			
		}

		if (args[0].equalsIgnoreCase("train")) {
			commands[0] = args[0];
			final TextProcessor documentsInstance = new TextProcessor(args[0], args[1]);
			final TextProcessor documentsProcessed = documentsInstance.returnDocuments();
			final FeatureSelector featureSet = new FeatureSelector(documentsProcessed);
			final NB_Classifier classifierInstance = new NB_Classifier(commands, featureSet);

			classifierInstance.train();
		}

		if (args[0].equalsIgnoreCase("test")) {
			commands[0] = args[0];
			commands[1] = args[1];
			//        	commands[2] = args[2];
			final TextProcessor documentsInstance = new TextProcessor(args[0], args[1]);
			final TextProcessor documentsProcessed = documentsInstance.returnDocuments();
			final NB_Classifier classifierInstance = new NB_Classifier(commands, documentsProcessed);

			classifierInstance.test();
		}

		else if (!(args[0].equalsIgnoreCase("train") || args[0].equalsIgnoreCase("test") || args[0].equalsIgnoreCase("traintest"))){
			System.out.println("Supply classifier with command 'traintest', 'train' or 'test' followed by dataset in csv-format (Example: 'java -jar classify.jar train dataset.csv')");
		}

		//Passa resultatet från textprocessor (och count) i form av träningsdata (tf-idf-vikter etc) till NB-klassificeraren
		//Spara output från klassificeraren i modell-klass

		//Kör NB-klassificeraren med ny data (testdata)
		//Spara output i "lista med resultat"
		//Spara output i textfil samt skriv ut den
	}
}

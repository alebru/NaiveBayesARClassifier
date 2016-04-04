/* 
 * Author: Alexander Orhagen Brusmark (brusmark at gmail.com)
 */


/* How to use:
 * 
 * As jar file:
 * 
 * java -jar [nameofjar].jar [traintest|train|test] [dataset.csv] [lower limit for examples/class (integer value)] [max limit of examples/class (integer value)] [freq|tfidf]
 * 
 * Example:
 * 
 * java -jar classifier.jar traintest bug_reports.csv 35 100 tfidf
 * 
 */

package textclassifier.abrusmark;

public class ReportClassifier
{
	public static String[] commands;

	public static void main(final String[] args) {
		commands = new String[5];
		commands[1] = args[1];
		
		if (args[0].equalsIgnoreCase("train") || args[0].equalsIgnoreCase("traintest")) {
			commands[2] = args[2];
			commands[3] = args[3];
			commands[4] = args[4];
		}
		
		if (args[0].equalsIgnoreCase("traintest")) {

			final DataSampling sampleAndSplit = new DataSampling(args[0], args[2], args[3], args[1]);
			
			final TextProcessorStringdata trainingSet = new TextProcessorStringdata("train", args[4], sampleAndSplit.trainingSet);
			final TextProcessorStringdata trainingSetProcessed = trainingSet.returnDocuments();
			
			if (args[4].equalsIgnoreCase("tfidf")) {
				final FeatureSelectorTfidf featureSet = new FeatureSelectorTfidf(args[4], args[2], args[3], trainingSetProcessed);
				final NB_Classifier classifierTrainInstance = new NB_Classifier(commands, featureSet);
				classifierTrainInstance.train();
			}
			if ((args[4].equalsIgnoreCase("freq"))){
				final FeatureSelector featureSet = new FeatureSelector(args[4], args[2], args[3], trainingSetProcessed);
				final NB_Classifier classifierTrainInstance = new NB_Classifier(commands, featureSet);
				classifierTrainInstance.train();
			}
			
			final TextProcessorStringdata testSet = new TextProcessorStringdata("test", args[4], sampleAndSplit.testSet);
			
			/* If argument "tfidf" is passed, NB_Classifier runs tfidf-evaluation */
			commands[0] = "test";
			
			final NB_Classifier classifierTestInstance = new NB_Classifier(commands, testSet);
			classifierTestInstance.test();
		}

		if (args[0].equalsIgnoreCase("train")) {
			commands[0] = args[0];
			
			final DataSampling sample = new DataSampling(args[0], args[2], args[3], args[1]);
			
			final TextProcessorStringdata trainingSet = new TextProcessorStringdata("train", args[4], sample.trainingSet);
			final TextProcessorStringdata trainingSetProcessed = trainingSet.returnDocuments();
			
			if (args[4].equalsIgnoreCase("tfidf")) {
				final FeatureSelectorTfidf featureSet = new FeatureSelectorTfidf(args[4], args[2], args[3], trainingSetProcessed);
				final NB_Classifier classifierTrainInstance = new NB_Classifier(commands, featureSet);
				classifierTrainInstance.train();
			}
			if ((args[4].equalsIgnoreCase("freq"))){
				final FeatureSelector featureSet = new FeatureSelector(args[4], args[2], args[3], trainingSetProcessed);
				final NB_Classifier classifierTrainInstance = new NB_Classifier(commands, featureSet);
				classifierTrainInstance.train();
			}
		}

		if (args[0].equalsIgnoreCase("classify")) {
			commands[0] = args[0];
			commands[1] = args[1];
		
			final TextProcessor documentsInstance = new TextProcessor(args[0], args[1]);
			final TextProcessor documentsProcessed = documentsInstance.returnDocuments();
			
			final NB_Classifier classifierInstance = new NB_Classifier(commands, documentsProcessed);
			classifierInstance.test();
		}

		else if (!(args[0].equalsIgnoreCase("train") || args[0].equalsIgnoreCase("classify") || args[0].equalsIgnoreCase("traintest"))){
			System.out.println("Supply classifier with command 'traintest', 'train' or 'test' followed by dataset in csv-format  (+lower and upper limits for examples/class, if 'train' or 'traintest' is used)" + "\n" + "(Example: 'java -jar classify.jar traintest dataset.csv 20 80 tfidf') ");
		}
	}
}

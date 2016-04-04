# NaiveBayesARClassifier 

**Alexander Brusmark** (brusmark@gmail.com)

> *A classifier for bug reports built on an implementation of the Multinomial [Naive Bayes](https://en.wikipedia.org/wiki/Naive_Bayes_classifier) classification method (for use with [Bugzilla](https://www.bugzilla.org/) CSV-format bug report data)*

*Tf-idf term weighting or normalized log-transformed simple frequencies*

Built using [Eclipse Mars (4.5.0)](http://www.eclipse.org/downloads/) & [Java development kit 1.8.0 update 60](http://www.oracle.com/technetwork/java/javase/downloads/index.html) 

---

### How to use
__Train__ - creates a model from an input .csv dataset

__Traintest__ - Splits input dataset (80%/20%) into a training set and a test set, reports classification accuracy

__Classify__ - Classifies an input .csv file with bug reports on the form *["bug product", "bug component", "bug summary"]* and outputs .csv file on the form *["bug product", "bug component", "bug summary", "__suggested developer__"]*

* Lower limit - choose the lower limit of examples/class required for the class to be included in the model (set to 1 to include all examples)
* Upper limit - choose the upper limit of examples/class required for the class to be included in the model (set to value above highest value in dataset to include all examples)
* freq - use normalized simple term frequencies as term weights
* tfidf - use the term weighting scheme [tf-idf](https://en.wikipedia.org/wiki/Tf–idf)

#### From command-prompt

java -jar [jar file].jar [|train|traintest|classify] [inputfile.csv] [lower limit (integer)] [upper limit (integer)] [freq|tfidf]

__Example:__

*java -jar NB_ReportClassifier.jar traintest loadsofbugreports.csv 30 150 tfidf*


#### File output

The model is saved in the file *model.dat* (in the same directory as the classifier)

The results of the classification is saved in *classified_bugreports.csv*
* Traintest - all _correct_ predictions are included in the output file
* Classify - all predictions are included in the output file




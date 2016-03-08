# NaiveBayesARClassifier 

**Alexander Brusmark** (brusmark@gmail.com)

*A classifier for bug reports based on the Naive Bayes method (for use with bugzilla CSV-format bug report data)*

*Tf-idf term weighting or normalized log-transformed simple frequencies*

Built using [Eclipse Mars (4.5.0)](https://www.eclipse.org) & [Java development kit 1.8.0 update 60](https://www.oracle.com/java/) 

---

### How to use

* cutoffvalue - choose the number of examples/class required for the class to be included in the model (set 1 to include all examples)
* freq - use normalized term frequencies based on counts
* tfidf - use the term weighting scheme [tf-idf](https://en.wikipedia.org/wiki/Tf–idf)

#### From command-prompt

java -jar [jar file].jar [test|train|traintest] [inputfile.csv] [cutoffvalue (integer)] [freq|tfidf]


__Example:__

*java -jar classifier.jar traintest loadsofbugreports.csv 30 tfidf*






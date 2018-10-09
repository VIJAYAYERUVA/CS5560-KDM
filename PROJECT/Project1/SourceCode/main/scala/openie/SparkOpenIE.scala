package openie

import java.io.PrintStream

import org.apache.log4j.{Level, Logger}
import org.apache.spark.ml.feature.{NGram, StopWordsRemover, Tokenizer, Word2Vec}
import org.apache.spark.sql.SparkSession
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 27-Jun-16.
  */
object SparkOpenIE {
  def main(args: Array[String]) {
    // Configuration
    val conf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")
    val sparkContext = new SparkContext(conf)

    val spark = SparkSession
      .builder
      .appName("SparkW2VML")
      .master("local[*]")
      .getOrCreate()

    // Turn off Info Logger for Console
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    // Read the files from disk
    val filename = "C:\\Users\\VIJAYA\\Desktop\\Course_Work\\2018\\3. Fall\\1. KDM\\LABS\\LAB2\\SparkOpenIE_WordNET_LDA\\data\\PubMed\\abstracts\\*" //"data/PubMed/Diabetes"


    // Read the file into RDD[String]
    val input = sparkContext.wholeTextFiles(filename)

    val lemmainput = input.map(line => {
      //Getting Lemmatized Form of the word using CoreNLP
      val lemma = CoreNLP.returnLemma(line._2)
      (line._1, lemma)
    })

    //println(lemmainput.collect().mkString("\n"))

    val output_lemma = new PrintStream("data/lemma")
    output_lemma.println(lemmainput.collect().mkString("\n"))

    //Creating DataFrame from RDD
    val sentenceData = spark.createDataFrame(lemmainput).toDF("labels", "sentence")
    sentenceData.toString()

    //Tokenizer
    val tokenizer = new Tokenizer().setInputCol("sentence").setOutputCol("words")
    val wordsData = tokenizer.transform(sentenceData)

    //Stop Word Remover
    val remover = new StopWordsRemover()
      .setInputCol("words")
      .setOutputCol("filteredWords")
    val processedWordData = remover.transform(wordsData)

    //NGram
    val ngram = new NGram().setInputCol("filteredWords").setOutputCol("ngrams")
    val ngramDataFrame = ngram.transform(processedWordData)

    //TFIDF TopWords
    val topWords = TFIDF.getTopTFIDFWords(sparkContext, ngramDataFrame.select("filteredWords").rdd)

    //Word2Vec Model Generation
    val word2Vec = new Word2Vec()
      .setInputCol("filteredWords")
      .setOutputCol("result")
      .setVectorSize(3)
      .setMinCount(0)
    val model = word2Vec.fit(ngramDataFrame)

    val output_tfidf = new PrintStream("data/tfidf.values")
    val topic_output = new PrintStream("data/W2V")

    topWords.foreach(f => {
      output_tfidf.println(f._1)
      if (f._1 != "") {
        val result = model.findSynonyms(f._1, 5)
        //result.take(5).foreach(println)
        topic_output.println(f._1)
        result.take(5).foreach(f1 => {
          topic_output.println(((f1.toString().replace("]", "")).replace("[", "")).split(",")(0))
        })
      }
    })
    val ipfile = input.map(s => {
      //Getting OpenIE Form of the word using lda.CoreNLP
      val output = MainMedNLP.returnTriplets(s._2)
      (s._1, output)
    })
    ipfile.collect().mkString("\n")
    MainMedNLP.tripletProcessing()
    spark.stop()
  }
}
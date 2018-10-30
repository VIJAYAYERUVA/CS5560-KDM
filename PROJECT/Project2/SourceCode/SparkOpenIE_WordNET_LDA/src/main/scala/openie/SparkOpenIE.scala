package openie

import org.apache.log4j.{Level, Logger}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by Mayanka on 27-Jun-16.
  */
object SparkOpenIE {

  def main(args: Array[String]) {
    // Configuration
    val sparkConf = new SparkConf().setAppName("SparkWordCount").setMaster("local[*]")

    val sc = new SparkContext(sparkConf)

    // For Windows Users
    //System.setProperty("hadoop.home.dir", "C:\\winutils")

    // Turn off Info Logger for Console
    Logger.getLogger("org").setLevel(Level.OFF)
    Logger.getLogger("akka").setLevel(Level.OFF)

    //Taking all abstracts
    val winput = sc.wholeTextFiles(path = "data/drugabuse", minPartitions = 2)

    val input = winput.map(abs => {
      abs._2
    })

    //Getting POS tags of the word using lda.CoreNLP
    val parseIO = input.map(line => {
      val t = CoreNLP.parse(line)
      t
    })

    val posIO = input.map(line => {
      val t = CoreNLP.postags(line)
      //t
      val pos1 = t.replace(".]", "")
      val pos2 = pos1.replace("[", "")
      val pos3 = pos2.replace(" ", "")
      val posf = pos3.split(",")
      /*      for (i <- 0 to posf.length) {
        println(i)
        if (i == "NN" || i == "NNS" || i == "NNP" || i == "NNPS") {
          POSN += 1
        }
        else if (i == "VB" || i == "VBD" || i == "VBG" || i == "VBN" || i == "VBP" || i == "VBZ") {
          POSV += 1
        }
        else {
          println("")
        }

      }*/
      pos3
    })

    val wc = posIO.flatMap(line => {
      line.split(",") // return statement I/P RDD[Array[]String] O/P RDD[String]
    }).map(word => (word, 1) // I/P RDD[String] O/P RDD[(String, Int)]
    ).cache()
    val output = wc.reduceByKey(_ + _) //Every Word will have one element I/P & O/P: RDD[(String, Int)]

    output.saveAsTextFile("output")

    val o = output.collect()

    var s: String = "Words:Count \n"
    o.foreach { case (word, count) => {

      s += word + " : " + count + "\n"

    }
    }
      //Getting NER of the word using lda.CoreNLP
      val nerIO = input.map(line => {
        val t = CoreNLP.returnTriplets(line)
        t
      })

      //Getting OpenIE Form of the word using lda.CoreNLP
      val openIEIO = input.map(line => {
        val t = CoreNLP.returnTriplets(line)
        t
      })
    // StopWords
    val stopwords=sc.textFile("data/stopwords.txt").collect()
    val stopwordBroadCast=sc.broadcast(stopwords)

    val input2= sc.textFile("data/sentenceSample").map(f=>{
      val afterStopWordRemoval=f.split(" ").filter(!stopwordBroadCast.value.contains(_))
    })

      //Getting OpenIE Form of the word using lda.CoreNLP
      val openIEIOn = input.map(line => {
        val t = CoreNLP.noofTriplets(line)
        t
      })

      val a = parseIO.collect().mkString("\n")
      val b = posIO.collect().mkString("\n")
      val c = nerIO.collect().mkString("\n")
      val d = openIEIO.collect().mkString("\n")
      val e = openIEIOn.collect().mkString("\n")

      //    println(parseIO.collect().mkString("\n"))
      //    println(posIO.collect().mkString("\n"))
      //    println(nerIO.collect().mkString("\n"))
      //    println(openIEIO.collect().mkString("\n"))
    }
  }
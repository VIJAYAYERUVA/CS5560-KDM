package openie;

import edu.stanford.nlp.simple.Document;
import edu.stanford.nlp.simple.Sentence;
import edu.stanford.nlp.util.Quadruple;
import scala.Int;

import java.util.Collection;
import java.util.List;

/**
 * Created by Mayanka on 27-Jun-16.
 */
public class CoreNLP {

    public static String parse(String sentence) {
        Document doc = new Document(sentence);
        String parse = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            // When we ask for the parse, it will load and run the parser
            Collection pa = sent.parse();
            for (int i = 0; i < pa.toArray().length; i++) {
                parse += pa.toString();
            }
            //System.out.println(parse);
        }
        return parse;
    }

    public static String postags(String sentence) {
        Document doc = new Document(sentence);
        String postags = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            // When we ask for the postags, it will load and run the postags
            Collection po = sent.posTags();
            for (int i = 0; i < po.toArray().length; i++) {
                postags += po.toString();
            }
            System.out.println(postags);
        }
//        String a = postags.toString();
//        String postags1 = a.split(",");
        return postags;
    }

    public static String ner(String sentence) {
        Document doc = new Document(sentence);
        String ner = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            // When we ask for the postags, it will load and run the postags
            Collection ne = sent.nerTags();
            for (int i = 0; i < ne.toArray().length; i++) {
                ner += ne.toString();
            }
            System.out.println(ner);
        }
        return ner;
    }

    public static String returnTriplets(String sentence) {
        Document doc = new Document(sentence);
        String triplets = "";
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            Collection<Quadruple<String, String, String, Double>> l = sent.openie();
            for (int i = 0; i < l.toArray().length; i++) {
                triplets += l.toString();
            }
            //System.out.println(triplets);
        }
        return triplets;
    }

    public static int noofTriplets(String sentence) {
        Document doc = new Document(sentence);
        String triplets = "";
        int k = 0;
        for (Sentence sent : doc.sentences()) {  // Will iterate over two sentences
            Collection<Quadruple<String, String, String, Double>> l = sent.openie();
            for (int i = 0; i < l.toArray().length; i++) {
                triplets += l.toString();
            }
            //System.out.println(triplets);
            k = l.toArray().length;
        }
        //System.out.println(k);
        return k;
    }
}

import edu.stanford.nlp.coref.CorefCoreAnnotations;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.CoreAnnotations;
import edu.stanford.nlp.ling.CoreLabel;
import edu.stanford.nlp.pipeline.Annotation;
import edu.stanford.nlp.pipeline.StanfordCoreNLP;
import edu.stanford.nlp.trees.Tree;
import edu.stanford.nlp.trees.TreeCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 * Created by Mayanka on 13-Jun-16.
 */
public class Main {
    public static void main(String args[]) throws IOException {
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

// read some text in the text variable
        File file = new File("data/drug addiction5");
        FileInputStream fis = new FileInputStream(file);
        byte[] bytesArray = new byte[(int) file.length()];
        fis.read(bytesArray);
        String text = new String(bytesArray);
        //String text = "The sexual behaviors of 15- to 24-year-olds increase the risk of this population to acquire sexually transmitted infections (STIs). The present study aimed to describe the sexual behavior in the transition to adulthood Brazilian population and its association with STI history.We analyzed cross-sectional data collected from 8562 sexually active women and men who participated in the National Survey of Human Papillomavirus Prevalence (POP-Brazil). This large-scale survey enrolled participants from 26 Brazilian capitals and the Federal District. Professionals from primary care facilities were trained to collect data utilizing a standardized questionnaire with questions on sociodemographic, sexual behavior, and drug use. We constructed a Poisson model with robust variance for both crude and adjusted analysis to investigate the associations between the variables. To adjust the distribution of the sample to the study population, we weighted the measures by the population size in each city and by gender.There were differences in several aspects from sexual behavior between genders. The majority of men reported an early sexual initiation, more sexual partners, and a different practice in sexual positions when compared with women. Women reported use of contraception more frequently than men (P < .001). The use of alcohol and drugs and the use of drugs before sexual intercourse impact in STIs equally between the genders. Exclusive for women, the presence of any STI was associated with the practice of vaginal sex and other types of intercourse (adjusted prevalence ratio [APR] 1.43, 95% CI 1.08-1.88). For men, the number of sexual partners in the last year (APR 1.02, 95% CI 1.01-1.04), not having vaginal sex (APR 3.25, 95% CI 1.78-5.92) and sexual experience with someone of the same sex (APR 4.05, 95% CI, 2.88-5.70) were associated with a higher presence of STIs.This is the first report regarding sexual behavior in a nationally representative population sample in Brazil. This study provides more valid estimates of sexual behavior and associated STIs, identifying important differences in sexual behavior and identifying predictors for referred STIs among females and males."; // Add your text here!

// create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

// run all Annotators on this text
        pipeline.annotate(document);

        // these are all the sentences in this document
// a CoreMap is essentially a Map that uses class objects as keys and has values with custom types
        List<CoreMap> sentences = document.get(CoreAnnotations.SentencesAnnotation.class);
        int POSN = 0;
        int POSV = 0;
        int NERP = 0;
        int NERL = 0;

        for (CoreMap sentence : sentences) {
            // traversing the words in the current sentence
            // a CoreLabel is a CoreMap with additional token-specific methods
            for (CoreLabel token : sentence.get(CoreAnnotations.TokensAnnotation.class)) {

                System.out.println("\n" + token);

                // this is the text of the token
                String word = token.get(CoreAnnotations.TextAnnotation.class);
                System.out.println("Text Annotation");
                System.out.println(token + ":" + word);
                // this is the POS tag of the token

                String lemma = token.get(CoreAnnotations.LemmaAnnotation.class);
                System.out.println("Lemma Annotation");
                System.out.println(token + ":" + lemma);
                // this is the Lemmatized tag of the token


                String pos = token.get(CoreAnnotations.PartOfSpeechAnnotation.class);
                System.out.println("POS");
                System.out.println(token + ":" + pos);
                if (pos.equals("NN") || pos.equals("NNS") || pos.equals("NNP") || pos.equals("NNPS")) {
                    POSN++;
                }
                if (pos.equals("VB") || pos.equals("VBD") || pos.equals("VBG") || pos.equals("VBN") || pos.equals("VBP") || pos.equals("VBZ")) {
                    POSV++;
                }

                // this is the NER label of the token
                String ne = token.get(CoreAnnotations.NamedEntityTagAnnotation.class);
                System.out.println("NER");
                System.out.println(token + ":" + ne);
                if (ne.equals("PERSON")) {
                    NERP++;
                }
                if (ne.equals("LOCATION")) {
                    NERL++;
                }

                System.out.println("\n\n");
            }

            // this is the parse tree of the current sentence
            Tree tree = sentence.get(TreeCoreAnnotations.TreeAnnotation.class);
            System.out.println(tree);
            // this is the Stanford dependency graph of the current sentence


            Map<Integer, CorefChain> graph =
                    document.get(CorefCoreAnnotations.CorefChainAnnotation.class);
            System.out.println(graph.values().toString());
        }
        System.out.println(POSN);
        System.out.println(POSV);
        System.out.println(NERP);
        System.out.println(NERL);

    }
}

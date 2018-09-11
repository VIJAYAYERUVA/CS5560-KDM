import java.io.File;
import java.io.FileInputStream;

public class WordCount {
    public static void main(String args[]) throws Exception {

        int count = 0;
        //Read the file
        File file = new File("data/drug abuse1");
        FileInputStream fis = new FileInputStream(file);
        byte[] bytesArray = new byte[(int) file.length()];
        fis.read(bytesArray);
        String s = new String(bytesArray);
        //Split the data in the abstract by space(" ")
        String[] data = s.split(" ");
        for (int i = 0; i < data.length; i++) {
            count++;
        }
        //print the no of words in the abstracts
        System.out.println("Number of characters in the given file are " + count);
    }
}
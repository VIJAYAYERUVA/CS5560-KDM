import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

// Program to find types of properties in Ontologies
class propertytypes {

    public static void main(String[] args) throws IOException {
        FileInputStream Triplets = new FileInputStream("C:\\Users\\VIJAYA\\Desktop\\Course_Work\\2018\\3. Fall\\1. KDM\\ICPS\\ICP8\\OntContructor\\data\\Triplets");
        BufferedReader br = new BufferedReader(new InputStreamReader(Triplets));
        String strLine;
        ArrayList<String> triplets = new ArrayList<String>();
        ArrayList<String> objects = new ArrayList<String>();

        while ((strLine = br.readLine()) != null) {
            triplets.add(strLine);
        }
        for (int i = 0; i < triplets.size(); i++) {
            String triplet1[] = triplets.get(i).split(",");
            String subject1 = triplet1[0];
            String predicate1 = triplet1[1];
            String object1 = triplet1[2];
            for (int j = 0; j < triplets.size(); j++) {
                String triplet2[] = triplets.get(j).split(",");
                String subject2 = triplet2[0];
                String predicate2 = triplet2[1];
                String object2 = triplet2[2];

                //a. Inverse Of and b. Symmetric Property

                if (subject1.equals(object2) && object1.equals(subject2)) {

                    //a. Inverse Of
                    if (!predicate1.equals(predicate2)) {
                        System.out.println("Inverse Of");
                        System.out.println(triplets.get(i));
                        System.out.println(triplets.get(j));
                    } else {//b. Symmetric Property
                        System.out.println("Symmetric Property");
                        System.out.println(triplets.get(i));
                        System.out.println(triplets.get(j));
                    }
                }

                //c. Transitive Property
                if (object1.equals(subject2) && predicate1.equals(predicate2) && !object1.equals(object2)) {
                    System.out.println("Transitive Property");
                    System.out.println(triplets.get(i));
                    System.out.println(triplets.get(j));
                }

                //d. Property Chain Axiom
                for (int k = 0; k < triplets.size(); k++) {
                    String triplet3[] = triplets.get(j).split(",");
                    String subject3 = triplet3[0];
                    String predicate3 = triplet3[1];
                    String object3 = triplet3[2];

                    if (object1.equals(subject2) && predicate1.equals(predicate2) && !object1.equals(object2)) {

                        if (subject1.equals(subject3) && object2.equals(object3) && !predicate1.equals(predicate3)) {
                            System.out.println("Property Chain Axiom");
                            System.out.println(triplets.get(i));
                            System.out.println(triplets.get(j));
                        }
                    }
                }
                //e. Asymmetric Property
                //f. Irreflexive Property
                if (object2.equals(subject2) && subject1.equals(subject2) && predicate1.equals(predicate2) && !object1.equals(object2)) {
                    System.out.println("Irreflexive Property");
                    System.out.println(triplets.get(i));
                    System.out.println(triplets.get(j));
                }
            }
        }
        System.out.println(triplets);
        br.close();
    }
}
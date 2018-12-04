import java.io.File

import org.semanticweb.owlapi.apibinding.OWLManager
import org.semanticweb.owlapi.model.IRI
import org.semanticweb.owlapi.model.OWLOntology
import org.semanticweb.owlapi.model.OWLOntologyManager

object OntoStats {
  def main(args: Array[String]): Unit = {

    val manager = OWLManager.createOWLOntologyManager
    //val ontology = manager.loadOntology(IRI.create("https://protege.stanford.edu/ontologies/pizza/pizza.owl"))
    val ontology = manager.loadOntologyFromOntologyDocument(new File("data/dron-full.owl"))
    System.out.println("Classes\n\n"+ontology.classesInSignature().toArray.mkString("\n")+"\n\n")
    System.out.println("Individuals\n\n"+ontology.individualsInSignature().toArray.mkString("\n")+"\n\n")
    System.out.println("Object Properties \n\n"+ontology.objectPropertiesInSignature().toArray.mkString("\n")+"\n\n")
    System.out.println("Data Properties \n\n"+ontology.dataPropertiesInSignature().toArray.mkString("\n")+"\n\n")

  }

}

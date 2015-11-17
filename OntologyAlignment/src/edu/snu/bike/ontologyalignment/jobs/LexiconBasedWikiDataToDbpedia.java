package edu.snu.bike.ontologyalignment.jobs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.AlignmentCollector;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.LexiconbasedAlign;

public class LexiconBasedWikiDataToDbpedia {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Config config = new Config();
		config.setNamespace1("<http://rdf.freebase.com/");
		config.setNamespace2("<http://www.wikidata.org/");
		
//		config.setNamespace1("<http://www.semanticweb.org/nansu/ontologies/2015/6/untitled-ontology-23#");
//		config.setNamespace2("<http://www.semanticweb.org/nansu/ontologies/2015/6/untitled-ontology-24#");
		
		
		String ontology1="data/freebase.nt";
		String ontology2="data/wikidata.nt";
//		String ontology1="data/ontology1.nt";
//		String ontology2="data/ontology2.nt";
		
//		LexiconbasedAlign align  = new LexiconbasedAlign(args[0], args[1], config);
		LexiconbasedAlign align  = new LexiconbasedAlign(ontology1, ontology2, config);
		
		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
//		for(DefaultEdge edge:graph.edgeSet()){
//			System.out.println(edge);
//		}
		
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector = new AlignmentCollector();

		collector.writeAlignmentToFile(results, config, "alignments/lexicon-based/sub.txt",
				"alignments/lexicon-based/eql.txt");
	}

}

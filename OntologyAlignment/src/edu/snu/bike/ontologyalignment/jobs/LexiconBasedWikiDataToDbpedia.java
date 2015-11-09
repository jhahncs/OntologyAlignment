package edu.snu.bike.ontologyalignment.jobs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.AlignmentCollector;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstancebasedAlign;
import edu.snu.bike.ontologyalignment.methods.LexiconbasedAlign;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class LexiconBasedWikiDataToDbpedia {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Config config = new Config();
		// blooms
		
		LexiconbasedAlign align  = new LexiconbasedAlign(args[0], args[1], config);

		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector = new AlignmentCollector();

		collector.writeAlignmentToFile(results, config, "documents/data/output/dbpedia2wikidata/sub.txt",
				"documents/data/output/dbpedia2wikidata/eql.txt");
	}

}

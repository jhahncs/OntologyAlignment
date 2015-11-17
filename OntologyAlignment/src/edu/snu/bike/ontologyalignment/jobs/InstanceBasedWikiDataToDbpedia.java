package edu.snu.bike.ontologyalignment.jobs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.AlignmentCollector;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstancebasedAlign;

public class InstanceBasedWikiDataToDbpedia {

	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		Config config = new Config();
		// LSH
		config.setScale(false);
		config.setType(Config.inter);
		config.setXs(1.0);
		config.setXe(0.6);
		config.setNamespace1("<http://dbpedia.org/ontology/");
		config.setNamespace2("<http://www.wikidata.org/entity");


		InstancebasedAlign align = new InstancebasedAlign("data/testData/dbpedia_2014_refined.nt",
				"data/testData/wikidata-taxonomy-refined.nt",
				"data/testData/class-instance", config);

		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector = new AlignmentCollector();

		collector.writeAlignmentToFile(results, config, "alignments/instance-based/sub.txt",
				"alignments/instance-based/eql.txt");
	}

}

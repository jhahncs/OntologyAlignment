package edu.snu.bike.ontologyalignment.jobs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.AlignmentCollector;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstancebasedAlign;

public class WikiDataToDbpedia {

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

		InstancebasedAlign align = new InstancebasedAlign("documents/data/input/dbpedia/dbpedia_2014_refined.nt",
				"documents/data/input/wikidata/wikidata-taxonomy-refined.nt",
				"documents/data/input/wikidata-dbpedia/class-instance", config);

		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector = new AlignmentCollector();

		collector.writeAlignmentToFile(results, config, "documents/data/output/dbpedia2wikidata/sub.txt",
				"documents/data/output/dbpedia2wikidata/eql.txt");
	}

}

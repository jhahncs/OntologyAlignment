package edu.snu.bike.ontologyalignment.evaluation;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstancebasedAlign;

public class EvaluationTest {
	public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub

		Config config = new Config();
		config.setScale(true);
		config.setType(Config.inter);
		config.setXs(1.0);
		config.setXe(0.6);
		config.setNamespace1("<http://source1");
		config.setNamespace2("<http://source2");
		test1(config);
		// test2(config);
	}

	public static void test1(Config config) throws Exception {
		GoldAligner galigner = new GoldAligner();
		galigner.generateGraph("documents/data/input/Alignment/eqlmappings.nt",
				"documents/data/input/Alignment/submappings.nt");
		SimpleDirectedGraph<String, DefaultEdge> ggraph = galigner.getGold();

		InstancebasedAlign align = new InstancebasedAlign("documents/data/input/Alignment/sourceOntology1.nt",
				"documents/data/input/Alignment/sourceOntology2.nt", "documents/data/input/Alignment/types1.nt",
				"documents/data/input/Alignment/types2.nt", config);

		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		FMeasure f = new FMeasure(ggraph, results);
		Double eqlPrecision = f.eqlPrecision();
		Double eqlRecall = f.eqlRecall();
		System.out.println(eqlPrecision + " " + eqlRecall + " " + f.fscore(eqlPrecision, eqlRecall));

		Double subPrecision = f.subsumptionPrecision();
		Double subRecall = f.subsumptionRecall();
		System.out.println(subPrecision + " " + subRecall + " " + f.fscore(subPrecision, subRecall));

	}

	public static void test2(Config config) throws Exception {
		GoldAligner galigner = new GoldAligner();
		galigner.generateGraph("documents/data/input/eqlmappings.nt", "documents/data/input/submappings.nt");
		SimpleDirectedGraph<String, DefaultEdge> ggraph = galigner.getGold();

		InstancebasedAlign align = new InstancebasedAlign("documents/data/input/Alignment/sourceOntology1.nt",
				"documents/data/input/Alignment/sourceOntology2.nt", "documents/data/input/Alignment/types1.nt",
				"documents/data/input/Alignment/types2.nt", config);

		SimpleDirectedGraph<String, DefaultEdge> graph = align.align(align.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results = align.getAlignments(graph, config);

		FMeasure f = new FMeasure(ggraph, results);
		Double eqlPrecision = f.eqlPrecision();
		Double eqlRecall = f.eqlRecall();
		System.err.println(eqlPrecision + " " + eqlRecall + " " + f.fscore(eqlPrecision, eqlRecall));

		Double subPrecision = f.subsumptionPrecision();
		Double subRecall = f.subsumptionRecall();
		System.err.println(subPrecision + " " + subRecall + " " + f.fscore(subPrecision, subRecall));

	}

}

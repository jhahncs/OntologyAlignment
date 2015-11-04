package edu.snu.bike.ontologyalignment.methods;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.iut.IUT;
import edu.snu.bike.ontologyalignment.methods.iut.IUT_LSH;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;

public class InstancebasedAlign implements Align {

	private Config config = null;

	private SimpleDirectedGraph<String, DefaultEdge> graph = null;

	public SimpleDirectedGraph<String, DefaultEdge> getGraph() {
		return graph;
	}

	private InstancebasedMapper mapper = null;
	private InputOntologies input = null;

	public InputOntologies getInput() {
		return input;
	}

	public void setInput(InputOntologies input) {
		this.input = input;
	}

	public InstancebasedAlign(String taxonomyFile1, String taxonomyFile2, String typeFromO1, String typeFromO2,
			Config config) throws Exception {

		setting(config);
		InputOntologiesLoad inputLoad = new InputOntologiesLoad(taxonomyFile1, taxonomyFile2, typeFromO1, typeFromO2);
		input = inputLoad.getData();

	}

	public InstancebasedAlign(String taxonomyFile1, String taxonomyFile2, String commonTypeInstanceFile, Config config)
			throws Exception {

		setting(config);
		InputOntologiesLoad inputLoad = new InputOntologiesLoad(taxonomyFile1, taxonomyFile2, commonTypeInstanceFile);
		input = inputLoad.getData();

	}

	public void setting(Config config) {
		this.config = config;
	}

	@Override
	public SimpleDirectedGraph<String, DefaultEdge> align(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		if (!config.isScale()) {
			mapper = new IUT();
		} else {
			// this is the scale one
			mapper = new IUT_LSH();
		}
		graph = mapper.mapping(input, config);
		return graph;
	}

	@Override
	public SimpleDirectedGraph<String, DefaultEdge> getAlignments(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config) {
		// TODO Auto-generated method stub
		AlignmentCollector collector = new AlignmentCollector();
		SimpleDirectedGraph<String, DefaultEdge> results = collector.collect(graph, config);
		return results;
	}

}

/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.blooms.BLOOMS;
import edu.snu.bike.ontologyalignment.methods.iut.IUT;
import edu.snu.bike.ontologyalignment.methods.iut.IUT_LSH;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;

/**
 * 
 *
 */
public class LexiconbasedAlign implements Align {

	
	private Config config = null;

	private SimpleDirectedGraph<String, DefaultEdge> graph = null;

	public SimpleDirectedGraph<String, DefaultEdge> getGraph() {
		return graph;
	}

	private LexiconbasedMapper mapper = null;
	private InputOntologies input = null;

	public InputOntologies getInput() {
		return input;
	}

	public void setInput(InputOntologies input) {
		this.input = input;
	}

	public LexiconbasedAlign(String taxonomyFile1, String taxonomyFile2, Config config) throws Exception {

		setting(config);
		InputOntologiesLoad inputLoad = new InputOntologiesLoad(taxonomyFile1, taxonomyFile2);
		input = inputLoad.getData();

	}

	

	public void setting(Config config) {
		this.config = config;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.snu.bike.ontologyalignment.methods.Align#align(edu.snu.bike.ontologyalignment.models.data.InputOntologies, edu.snu.bike.ontologyalignment.methods.Config)
	 */
	@Override
	public SimpleDirectedGraph<String, DefaultEdge> align(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		if (config.isUseRam()) {
			
			
			
		} else {
			// this is the scale one
			
			
			
		}
		
		mapper = new BLOOMS();
		
		graph = mapper.mapping(input, config);
		return graph;
		
	}

	/* (non-Javadoc)
	 * @see edu.snu.bike.ontologyalignment.methods.Align#getAlignments(org.jgrapht.graph.SimpleDirectedGraph, edu.snu.bike.ontologyalignment.methods.Config)
	 */
	@Override
	public SimpleDirectedGraph<String, DefaultEdge> getAlignments(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config) {
		// TODO Auto-generated method stub
		return null;
	}

}

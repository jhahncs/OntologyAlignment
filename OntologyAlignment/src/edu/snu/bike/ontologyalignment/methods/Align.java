/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.models.data.InputOntologies;
import edu.snu.bike.ontologyalignment.models.data.OutputAlignments;


public interface Align {

	public SimpleDirectedGraph<String, DefaultEdge> align(InputOntologies input, Config config) throws Exception;

	public SimpleDirectedGraph<String, DefaultEdge> getAlignments(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config);

}

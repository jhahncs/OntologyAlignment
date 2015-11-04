/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.models.data.InputOntologies;



/**
 * 
 *  this interface for choosing lexical-based method or instance-based method
 *
 */
public interface Align {

	public SimpleDirectedGraph<String, DefaultEdge> align(InputOntologies input, Config config) throws Exception;

	public SimpleDirectedGraph<String, DefaultEdge> getAlignments(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config);

}

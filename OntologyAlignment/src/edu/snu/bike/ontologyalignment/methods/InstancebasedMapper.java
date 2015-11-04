/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.models.data.InputOntologies;


public interface InstancebasedMapper {

	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config) throws Exception;
	
	public void initial(InputOntologies input, Config config)  throws Exception;
}

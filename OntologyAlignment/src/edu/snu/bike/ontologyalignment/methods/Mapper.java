/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import java.io.FileNotFoundException;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.models.data.InputOntologies;

/**
 * @author nansu
 *
 */
public interface Mapper {

	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config) throws Exception;

}

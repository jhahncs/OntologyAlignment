/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;


public interface TaxonomyLoad {

	/**
	 * 
	 * @param taxonomyfile
	 *            example:
	 *            "<http://dbpedia.org/ontology/BasketballLeague> <http://www.w3.org/2000/01/rdf-schema#subClassOf> <http://dbpedia.org/ontology/SportsLeague> ."
	 * @return
	 * @throws Exception
	 * @output HashMap<subclass,HashSet<supclasses>>
	 */
	public HashMap<String, HashSet<String>> loadTaxonomy(String taxonomyfile) throws Exception;

}

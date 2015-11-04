/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import java.util.HashMap;
import java.util.HashSet;


public interface CommonTypesLoad {
	
	
	/**
	 * for big data , the common classes instance file needs to be processed by Hadoop in 
	 * "edu.snu.bike.ontologyalignment.data.processing"
	 * 
	 * @param commonTypeInstanceFile
	 * @return
	 * @throws Exception 
	 */
	public HashMap<String,HashSet<String>> loadTypes(String commonTypeInstanceFile) throws Exception; // or known as common classes instance file,
	// or big data 
	
	
	/**
	 * for small data
	 * @param typeFromO1
	 * @param typeFromO2
	 * @return
	 */
	public HashMap<String,HashSet<String>> loadTypes(String typeFromO1, String typeFromO2) throws Exception;; 

}

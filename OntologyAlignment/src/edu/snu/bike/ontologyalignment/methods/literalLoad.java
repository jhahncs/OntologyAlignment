package edu.snu.bike.ontologyalignment.methods;

import java.util.HashMap;

public interface literalLoad {
	
	public HashMap<String, String> loadLabels(String taxonomyfile1,String taxonomyfile2) throws Exception;
	public HashMap<String, String> loadDescriptions(String taxonomyfile,String taxonomyfile2) throws Exception;
}

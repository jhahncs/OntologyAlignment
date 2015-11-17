package edu.snu.bike.ontologyalignment.data.parse;

public interface InputSplit {
	
	public void split(String targetTaxonomy, String sourceTaxonomy, 
			String classInstance, String sourceClassLabel, String targetClassLabel) throws Exception;

}

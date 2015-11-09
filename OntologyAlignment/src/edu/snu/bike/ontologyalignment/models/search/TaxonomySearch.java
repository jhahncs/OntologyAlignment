package edu.snu.bike.ontologyalignment.models.search;

import java.util.Set;

public interface TaxonomySearch {
	
	public Set<String> getFathers(String type) throws Exception;

}

package edu.snu.bike.ontologyalignment.models.search;

import java.util.Set;

public interface TaxonomyRamSearch {
	
	public Set<String> getFathers(String type) throws Exception;

}

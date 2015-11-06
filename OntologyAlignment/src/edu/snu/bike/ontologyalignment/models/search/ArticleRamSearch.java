package edu.snu.bike.ontologyalignment.models.search;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public interface ArticleRamSearch {
	
	public HashMap<String, HashSet<String>> getTypes(Set<String> articleUrls) throws Exception ;
	
	public Set<String>  getArticle(String phrase) throws Exception ;


}

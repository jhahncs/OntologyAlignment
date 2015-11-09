package edu.snu.bike.ontologyalignment.models.search;

import java.util.Set;

public interface ArticleSearch {
	
	public Set<String> getTypes (String article) throws Exception ;
	
	public Set<String>  getArticle(String phrase) throws Exception ;


}

package edu.snu.bike.ontologyalignment.models.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

public interface TaxonomyIndex {
	
	public void writeOntology(IndexWriter writer, String ontologyfile) throws IOException;
	
}

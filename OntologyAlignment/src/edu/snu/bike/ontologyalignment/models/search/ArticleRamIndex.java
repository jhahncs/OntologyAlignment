package edu.snu.bike.ontologyalignment.models.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

public interface ArticleRamIndex {
	
	public void writeArticleToRam(IndexWriter writer, String articleTitleFile, String articleAbstrctFile) throws IOException;
	
	public void writeArticleTypeToRam(IndexWriter writer, String articleTypeFile) throws IOException;
	
}

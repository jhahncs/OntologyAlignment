package edu.snu.bike.ontologyalignment.models.search;

import java.io.IOException;

import org.apache.lucene.index.IndexWriter;

public interface ArticleIndex {
	
	public void writeArticle(IndexWriter writer, String articleTitleFile, String articleAbstrctFile) throws IOException;
	
	public void writeArticleType(IndexWriter writer, String articleTypeFile) throws IOException;
	
}

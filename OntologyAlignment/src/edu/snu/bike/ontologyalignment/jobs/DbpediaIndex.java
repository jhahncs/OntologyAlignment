package edu.snu.bike.ontologyalignment.jobs;

import java.io.IOException;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.LockObtainFailedException;

import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.models.search.KnowledgeBaseIndexer;

public class DbpediaIndex {

	public static void main(String[] args) throws CorruptIndexException, LockObtainFailedException, IOException {
		// TODO Auto-generated method stub
	
	Config config  =new Config();
	String articleTitleFile="data/article_labels_en.nt";
	String articleAbstrctFile="data/article_long-abstracts_en.nt";
	String articleTypeFile="data/instance_types_en_2015.nt";
	String taxonomyFile="data/dbpedia_2014.nt";
	
//	String articleTitleFile = "data/article_labels_en.nt";
//	String articleAbstrctFile = "data/article_long-abstracts_en.nt";
//
//	String articleClassFile1 = "data/article-categories_en.nt";
//	String ontologyfile1 = "data/skos-categories_en.nt";
//
//	String articleClassFile2 = "data/instance_types_en_2015.nt";
//	String ontologyfile2 = "data/dbpedia_2014.nt";
	
	
	String articleIndexDir=config.getArticleDirectory();
	String typeIndexDir=config.getTypeDirectory();
	String taxonomyIndexDir=config.getTaxonomyDirectory();
		
	 KnowledgeBaseIndexer indexer= new KnowledgeBaseIndexer(articleTitleFile, articleAbstrctFile, 
			 articleTypeFile, taxonomyFile, articleIndexDir, typeIndexDir, taxonomyIndexDir);
	 
	 indexer.index();
	}

}

package edu.snu.bike.ontologyalignment.models.search;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.store.Directory;

public class KnowledgeBaseSearcher implements ArticleSearch,TaxonomySearch {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private HashMap<String, HashSet<String>> fathers;
	private Directory typeDirectory;
	private Directory articleDirectory;
	private Directory taxonomyDirectory;

	public KnowledgeBaseSearcher(Directory typeDirectory, Directory articleDirectory, Directory taxonomyDirectory) {
		this.typeDirectory = typeDirectory;
		this.articleDirectory = articleDirectory;
		this.taxonomyDirectory = taxonomyDirectory;
	}

	@Override
	public Set<String> getArticle(String phrases) throws CorruptIndexException, IOException {
		// TODO Auto-generated method stub

		Set<String> articles = new HashSet<String>();

		BooleanQuery bq = new BooleanQuery();
		BooleanQuery.setMaxClauseCount(100);
		BooleanQuery bq1 = new BooleanQuery();
		for (String string : phrases.split(" ")) {
			TermQuery tq = new TermQuery(new Term("description", string.toLowerCase()));
			bq1.add(tq, Occur.MUST);
		}

		bq.add(bq1, Occur.SHOULD);
		BooleanQuery bq2 = new BooleanQuery();
		for (String string : phrases.split(" ")) {
			TermQuery tq = new TermQuery(new Term("label", string.toLowerCase()));
			bq2.add(tq, Occur.MUST);
		}
		bq.add(bq1, Occur.MUST);

		// System.out.println("query: "+bq);

		IndexSearcher searcher = new IndexSearcher(this.articleDirectory);
		TopDocs rs = searcher.search(bq, 10);
		// System.out.println("number of hits: "+rs.totalHits);
		ScoreDoc[] docs = rs.scoreDocs;

		for (ScoreDoc doc : docs) {
			articles.add(searcher.doc(doc.doc).get("url"));
		}
		return articles;
	}


	@Override
	public Set<String> getFathers(String type) throws Exception {
		// TODO Auto-generated method stub
		Set<String> fathers = new HashSet<String>();

		TermQuery tq = new TermQuery(new Term("url", type));

		// System.out.println("query: "+bq);

		IndexSearcher searcher = new IndexSearcher(this.taxonomyDirectory);
		TopDocs rs = searcher.search(tq,searcher.maxDoc());
		// System.out.println("number of hits: "+rs.totalHits);
		ScoreDoc[] docs = rs.scoreDocs;

		for (ScoreDoc doc : docs) {
			fathers.add(searcher.doc(doc.doc).get("fathers"));
		}
		
		return fathers;
	}

	@Override
	public Set<String> getTypes(String article) throws Exception {
		// TODO Auto-generated method stub
		Set<String> types = new HashSet<String>();

		TermQuery tq = new TermQuery(new Term("url", article));
		// System.out.println("query: "+bq);

		IndexSearcher searcher = new IndexSearcher(this.typeDirectory);
		
		TopDocs rs = searcher.search(tq,searcher.maxDoc());
		// System.out.println("number of hits: "+rs.totalHits);
		ScoreDoc[] docs = rs.scoreDocs;

		for (ScoreDoc doc : docs) {
			for(String type:searcher.doc(doc.doc).get("types").split(" ")){
				types.add(type);
			}
		}
		
		return types;
	}

}

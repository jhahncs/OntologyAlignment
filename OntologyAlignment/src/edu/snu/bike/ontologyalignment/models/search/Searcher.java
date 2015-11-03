package edu.snu.bike.ontologyalignment.models.search;

import java.io.File;
import java.io.IOException;

import org.apache.lucene.index.Term;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.store.FSDirectory;

public class Searcher {

	IndexSearcher searcher;

	public Searcher(String idx) throws IOException {
		FSDirectory dir = FSDirectory.open(new File(idx));
		searcher = new IndexSearcher(dir);
	}

	public Integer getFreq(String phrase) throws IOException {

		PhraseQuery pq = new PhraseQuery();

		for (String term : phrase.toLowerCase().split(" ")) {
			pq.add(new Term("content", term));
		}

		pq.setSlop(0);

		int docfreq = searcher.search(pq, searcher.maxDoc()).totalHits;

		return docfreq;

	}

	public Integer getFreq(String phrase1, String phrase2) throws IOException {

		PhraseQuery pq1 = new PhraseQuery();

		for (String term : phrase1.toLowerCase().split(" ")) {
			pq1.add(new Term("content", term));
		}

		pq1.setSlop(0);

		PhraseQuery pq2 = new PhraseQuery();

		for (String term : phrase2.toLowerCase().split(" ")) {
			pq2.add(new Term("content", term));
		}

		pq2.setSlop(0);

		BooleanQuery bq = new BooleanQuery();
		bq.add(pq1, Occur.MUST);
		bq.add(pq2, Occur.MUST);

		int docfreq = searcher.search(bq, searcher.maxDoc()).totalHits;

		return docfreq;

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

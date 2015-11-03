package edu.snu.bike.ontologyalignment.data.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.ParseException;
import org.apache.lucene.search.BooleanClause.Occur;
import org.apache.lucene.search.BooleanQuery;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.sindice.siren.analysis.AnyURIAnalyzer;
import org.sindice.siren.analysis.AnyURIAnalyzer.URINormalisation;

public class IndexerAndSearcher {

	public static void indexIns(String idx, String file) throws CorruptIndexException, IOException {

		Analyzer stringAnalyzer = new StandardAnalyzer(Version.LUCENE_34);

		AnyURIAnalyzer anyURIAnalyzer = new AnyURIAnalyzer(Version.LUCENE_34);
		anyURIAnalyzer.setUriNormalisation(URINormalisation.LOCALNAME);

		// IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_34, new
		// TupleAnalyzer(Version.LUCENE_34, stringAnalyzer, anyURIAnalyzer));

		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_34, anyURIAnalyzer);

		Directory dir = FSDirectory.open(new File(idx));

		IndexWriter writer = new IndexWriter(dir, conf);

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));

		String line = null;
		int i = 0;
		System.out.println("index : " + file);
		while ((line = br.readLine()) != null) {
			String[] ele = line.split("	");

			if (i % 5000 == 0) {
				writer.commit();
			}

			if (ele.length > 1) {
				Document doc = new Document();

				doc.add(new Field("url", ele[0], Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

				doc.add(new Field("content", ele[1], Store.YES, Field.Index.ANALYZED));
				writer.addDocument(doc);
			}

			i++;
		}
		br.close();
		writer.commit();
		writer.close();
		System.out.println("doc num: " + i);

	}

	public static void indexCls(String idx, String file) throws CorruptIndexException, IOException {

		Analyzer stringAnalyzer = new StandardAnalyzer(Version.LUCENE_34);

		AnyURIAnalyzer anyURIAnalyzer = new AnyURIAnalyzer(Version.LUCENE_34);
		anyURIAnalyzer.setUriNormalisation(URINormalisation.LOCALNAME);

		// IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_34, new
		// TupleAnalyzer(Version.LUCENE_34, stringAnalyzer, anyURIAnalyzer));

		IndexWriterConfig conf = new IndexWriterConfig(Version.LUCENE_34, anyURIAnalyzer);

		Directory dir = FSDirectory.open(new File(idx));

		IndexWriter writer = new IndexWriter(dir, conf);

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));

		String line = null;
		int i = 0;
		int ins = 0;
		System.out.println("index : " + file);

		while ((line = br.readLine()) != null) {
			String[] ele = line.split("	");
			i++;
			if (i % 5000 == 0) {
				writer.commit();
			}

			ins += ele[1].split(" ").length;

			if (ele.length > 1) {
				Document doc = new Document();

				doc.add(new Field("url", ele[0], Store.YES, Field.Index.NOT_ANALYZED_NO_NORMS));

				doc.add(new Field("content", ele[1], Store.YES, Field.Index.ANALYZED));

				writer.addDocument(doc);
			}

		}

		br.close();
		writer.commit();
		writer.close();
		System.err.println("cls num: " + i);
		System.err.println("ins num: " + ins);
		System.err.println("avg ins num: " + (double) ins / i);

	}

	public static void getBigCls(String outfile, String file, int number) throws CorruptIndexException, IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));

		String line = null;
		int i = 0;
		int ins = 0;
		System.out.println("index : " + file);

		while ((line = br.readLine()) != null) {
			String[] ele = line.split("	");

			if (ele[1].split(" ").length >= number) {
				ins += ele[1].split(" ").length;
				i++;
				bw.write(ele[0] + "\r\n");
				bw.flush();
			}
		}

		br.close();
		bw.flush();
		bw.close();
		System.err.println("cls num: " + i);
		System.err.println("ins num: " + ins);
		System.err.println("avg ins num: " + (double) ins / i);

	}

	public static void getBigCls(String outfile, String file, int min, int max)
			throws CorruptIndexException, IOException {

		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(outfile)));

		BufferedReader br = new BufferedReader(new FileReader(new File(file)));

		String line = null;
		int i = 0;
		int ins = 0;
		System.out.println("index : " + file);

		while ((line = br.readLine()) != null) {
			String[] ele = line.split("	");

			if (ele[1].split(" ").length >= min && ele[1].split(" ").length < max) {
				ins += ele[1].split(" ").length;
				i++;
				bw.write(ele[0] + "\r\n");
				bw.flush();
			}

		}

		br.close();
		bw.flush();
		bw.close();
		System.err.println("cls num: " + i);
		System.err.println("ins num: " + ins);
		System.err.println("avg ins num: " + (double) ins / i);

	}

	public static HashMap<String, HashSet<String>> searchPartial(String idx, HashSet<String> urls) throws IOException {

		FSDirectory dir = FSDirectory.open(new File(idx));
		IndexSearcher searcher = new IndexSearcher(dir);
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

		int num = urls.size();
		BooleanQuery bq = new BooleanQuery();
		BooleanQuery.setMaxClauseCount(num);
		for (String u : urls) {
			bq.add(new TermQuery(new Term("url", u.toLowerCase())), Occur.SHOULD);
		}

		ScoreDoc[] docs = searcher.search(bq, searcher.maxDoc()).scoreDocs;

		for (ScoreDoc doc : docs) {
			String[] values = searcher.doc(doc.doc).get("content").split(" ");
			String name = searcher.doc(doc.doc).get("url");
			HashSet<String> set = new HashSet<String>();
			if (urls.contains(name)) {
				for (String value : values) {
					set.add(value);
				}
				map.put(name, set);
			}
		}
		System.out.println("search size : " + urls.size());
		System.out.println("obtained size : " + map.size());
		searcher.close();
		return map;
	}

	public static HashMap<String, HashSet<String>> searchALL(String idx) throws IOException {

		FSDirectory dir = FSDirectory.open(new File(idx));
		IndexSearcher searcher = new IndexSearcher(dir);
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();

		int max = searcher.maxDoc();

		for (int i = 0; i < max; i++) {
			String name = searcher.doc(i).get("url");
			HashSet<String> set = new HashSet<String>();
			for (String value : searcher.doc(i).get("content").split(" ")) {
				set.add(value);
			}
			map.put(name, set);
		}

		searcher.close();
		return map;
	}

	public HashSet<String> search(String idx, String query) throws IOException {

		FSDirectory dir = FSDirectory.open(new File(idx));
		IndexSearcher searcher = new IndexSearcher(dir);

		System.out.println("search : " + query);

		HashSet<String> set = new HashSet<String>();

		ScoreDoc[] docs = searcher.search(new TermQuery(new Term("url", query)), searcher.maxDoc()).scoreDocs;
		for (ScoreDoc doc : docs) {
			String[] values = searcher.doc(doc.doc).get("content").split(" ");
			String name = searcher.doc(doc.doc).get("url");
			System.out.println(name + " " + values);
			for (String value : values) {
				set.add(value);
			}
		}
		searcher.close();

		return set;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws CorruptIndexException
	 * @throws ParseException
	 */
	public static void main(String[] args) throws CorruptIndexException, IOException, ParseException {
		// TODO Auto-generated method stub
		IndexerAndSearcher indexer = new IndexerAndSearcher();
		// indexer.indexCls("E://Datasets/research/index/dbpediaOntologyTypes",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaOntologyOut/part-r-00000");
		// indexer.indexCls("E://Datasets/research/index/wikiCategoryTypes",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/wikiCategoryTypesOut/part-r-00000");
		// indexer.indexCls("E://Datasets/research/index/yagoTypes",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000");
		// indexer.indexCls("E://hierarchy/index/yagoWikiTypes",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoWikiCategoryOut/part-r-00000");
		// indexer.indexCls("E://hierarchy/index/dbpediaYagoTypes201206",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaYago201206Out/part-r-00000");
		// indexer.indexCls("E://hierarchy/index/dbpediaOntologyAndYago(merge_commonIns)",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/out1/part-r-00000");

		// indexer.getClassDistribution("E://hierarchy/seeds/yago_distribution_1.csv"
		// ,
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000");
		// indexer.getClassDistribution("E://hierarchy/seeds/dbpediaOntology_distribution.csv"
		// ,
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaOntologyOut/part-r-00000");
		// indexer.getClassDistribution("E://hierarchy/seeds/wikiCategory_distribution_1.csv"
		// ,
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/wikiCategoryTypesOut/part-r-00000");

		IndexerAndSearcher.getBigCls("E://hierarchy/seeds/yago_0_100_20140821.txt",
				"C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000", 0, 100);
		IndexerAndSearcher.getBigCls("E://hierarchy/seeds/yago_100_500_20140821.txt",
				"C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000", 100, 500);
		IndexerAndSearcher.getBigCls("E://hierarchy/seeds/yago_500_10000_20140821.txt",
				"C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000", 500, 10000);

		// indexer.getBigCls("E://hierarchy/seeds/yago_seeds_all.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoTyesOut/part-r-00000",0,Integer.MAX_VALUE);

		// indexer.getBigCls("E://hierarchy/seeds/dbpedia_0_100.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaOntologyOut/part-r-00000",0,100);
		// indexer.getBigCls("E://hierarchy/seeds/dbpedia_100_500.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaOntologyOut/part-r-00000",100,500);
		// indexer.getBigCls("E://hierarchy/seeds/dbpedia_500_10000.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/dbpediaOntologyOut/part-r-00000",500,10000);

		// indexer.getBigCls("E://hierarchy/seeds/wiki_0_100.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoWikiCategoryOut/part-r-00000",0,100);
		// indexer.getBigCls("E://hierarchy/seeds/wiki_100_500.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoWikiCategoryOut/part-r-00000",100,500);
		// indexer.getBigCls("E://hierarchy/seeds/wiki_500_10000.txt",
		// "C://cygwin64/usr/local/hadoop/hadoop-0.20.2/yagoWikiCategoryOut/part-r-00000",500,10000);

		// searchCls("E://hierarchy/index/dbpediaOntologyTypes","<http://dbpedia.org/ontology/Philosopher>");
		// searchCls("E://hierarchy/index/wikiCategoryTypes","<http://dbpedia.org/resource/Category:Radiation>");
		// searchCls("E://hierarchy/index/yagoTypes","<http://dbpedia.org/class/yago/YagoLegalActorGeo>");

	}

}

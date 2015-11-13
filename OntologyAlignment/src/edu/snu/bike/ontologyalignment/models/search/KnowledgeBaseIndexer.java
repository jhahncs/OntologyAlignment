package edu.snu.bike.ontologyalignment.models.search;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

import edu.snu.bike.ontologyalignment.util.TaxonomyUtil;

public class KnowledgeBaseIndexer implements ArticleIndex, TaxonomyIndex {

	public static void main(String args[]) throws CorruptIndexException, IOException {

		String dir0 = "/externalDisk/1/samsung2015/index/dbpediaArticle";
		String dir1 = "/externalDisk/1/samsung2015/index/wikiCategoryType";
		String dir2 = "/externalDisk/1/samsung2015/index/dbpediaOntologyType";

		String articleTitleFile = "data/article_labels_en.nt";
		String articleAbstrctFile = "data/article_long-abstracts_en.nt";

		String articleClassFile1 = "data/article-categories_en.nt";
		String ontologyfile1 = "data/skos-categories_en.nt";

		String articleClassFile2 = "data/instance_types_en_2015.nt";
		String ontologyfile2 = "data/dbpedia_2014.nt";

		KnowledgeBaseIndexer indexer=new KnowledgeBaseIndexer();
		indexer.index();

	}
	
	private HashMap<String, HashSet<String>> fathers;
	private Directory typeDirectory;
	private Directory articleDirectory;
	private Directory taxonomyDirectory;
	private String articleTitleFile;
	private String articleAbstrctFile;
	private String articleTypeFile;
	private String taxonomyFile;
	
	public Directory getTypeDirectory() {
		return typeDirectory;
	}

	public void setTypeDirectory(Directory typeDirectory) {
		this.typeDirectory = typeDirectory;
	}

	public Directory getArticleDirectory() {
		return articleDirectory;
	}

	public void setArticleDirectory(Directory articleDirectory) {
		this.articleDirectory = articleDirectory;
	}

	public Directory getTaxonomyDirectory() {
		return taxonomyDirectory;
	}

	public void setTaxonomyDirectory(Directory taxonomyDirectory) {
		this.taxonomyDirectory = taxonomyDirectory;
	}


	public  KnowledgeBaseIndexer(){
		
	}
			
	public KnowledgeBaseIndexer(String articleTitleFile, String articleAbstrctFile,
			String articleTypeFile, String taxonomyFile)
					throws CorruptIndexException, LockObtainFailedException, IOException {
		
		fathers=new HashMap<String,HashSet<String>>();
		
		typeDirectory = new RAMDirectory();
		articleDirectory = new RAMDirectory();
		taxonomyDirectory = new RAMDirectory();

		this.articleTitleFile = articleTitleFile;
		this.articleAbstrctFile = articleAbstrctFile;
		this.articleTypeFile = articleTypeFile;
		this.taxonomyFile = taxonomyFile;
	}
	
	public KnowledgeBaseIndexer(String articleTitleFile, String articleAbstrctFile,
			String articleTypeFile, String taxonomyFile, String articleIndexDir, String typeIndexDir, String taxonomyIndexDir)
					throws CorruptIndexException, LockObtainFailedException, IOException {
		
		fathers=new HashMap<String,HashSet<String>>();
		
		typeDirectory = FSDirectory.open(new File(typeIndexDir));
		articleDirectory = FSDirectory.open(new File(articleIndexDir));
		taxonomyDirectory = FSDirectory.open(new File(taxonomyIndexDir));

		this.articleTitleFile = articleTitleFile;
		this.articleAbstrctFile = articleAbstrctFile;
		this.articleTypeFile = articleTypeFile;
		this.taxonomyFile = taxonomyFile;
	}
	
	

	public void index() throws IOException {
		
		System.out.println(" write article ...");
		
		IndexWriter taxonomyDirectoryWriter = new IndexWriter(taxonomyDirectory,
				new StandardAnalyzer(Version.LUCENE_34), true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		System.out.println(" indexing ontology ...");
		
		writeOntology(taxonomyDirectoryWriter, this.taxonomyFile);
		
		
		
		System.out.println(" indexing article type ...");
		
		IndexWriter typeDirectoryWriter = new IndexWriter(typeDirectory, new StandardAnalyzer(Version.LUCENE_34), true,
				IndexWriter.MaxFieldLength.UNLIMITED);
		
		
		writeArticleType(typeDirectoryWriter, this.articleTypeFile);
		
		
		System.out.println(" indexing article ...");
		
		IndexWriter articleDirectoryWriter = new IndexWriter(articleDirectory, new StandardAnalyzer(Version.LUCENE_34),
				true, IndexWriter.MaxFieldLength.UNLIMITED);
		
		writeArticle(articleDirectoryWriter, this.articleTitleFile, this.articleAbstrctFile);
		
		
		System.out.println("knowledgebase indexing finished ...");
	}

	@Override
	public void writeOntology(IndexWriter writer, String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		fathers = new HashMap<String, HashSet<String>>();
		BufferedReader br = new BufferedReader(new FileReader(new File(ontologyfile)));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("<http://")) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser(inputStream, false);
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();

					if (p.equals("<http://www.w3.org/2000/01/rdf-schema#subClassOf>")
							|| p.equals("<http://www.w3.org/2004/02/skos/core#broader>")) {
						if ((s.startsWith("<http://dbpedia.org/resource/Category:")
								& o.startsWith("<http://dbpedia.org/resource/Category:"))
								|| (s.startsWith("<http://dbpedia.org/ontology/")
										& o.startsWith("<http://dbpedia.org/ontology/"))) {

							if (!s.equals(o)) {
								if (fathers.containsKey(s)) {
									fathers.get(s).add(o);
								} else {
									HashSet<String> father = new HashSet<String>();
									father.add(o);
									fathers.put(s, father);
								}
							} else {
								// System.out.println(line);
							}

						}
					}

				}
			}

		}
		
		br.close();	
		
		for(Entry<String,HashSet<String>>entry:fathers.entrySet()){
			String string="";
			for(String father:entry.getValue()){
				string+=father+" ";
			}
			Document doc = new Document();
			doc.add(new Field("url", entry.getKey(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("fathers", string.trim(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			writer.addDocument(doc);
		}
		writer.commit();
		writer.close();
	}

	@Override
	public void writeArticle(IndexWriter writer, String articleTitleFile, String articleAbstrctFile)
			throws IOException {
		// TODO Auto-generated method stub
		
		BufferedReader br = new BufferedReader(new FileReader(new File(articleTitleFile)));
		String line = null;
		HashMap<String, String> names = new HashMap<String, String>();

		while ((line = br.readLine()) != null) {
			if (line.startsWith("<http://")) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser(inputStream, false);
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();
					if (p.equals("<http://www.w3.org/2000/01/rdf-schema#label>") & o.contains("@en")) {
						// System.out.println(o);
						String content = o.substring(o.indexOf("\"") + 1, o.lastIndexOf("\""));

						if (names.containsKey(s)) {
							names.put(s, content);
						} else {
							names.put(s, names.get(s) + " , " + content);
						}
					}

				}
			}

		}
		br.close();
		if(articleAbstrctFile!=""){
			br = new BufferedReader(new FileReader(new File(articleAbstrctFile)));
			line = null;
			while ((line = br.readLine()) != null) {
				if (line.startsWith("<http://")) {
					InputStream inputStream = new ByteArrayInputStream(line.getBytes());
					NxParser nxp = new NxParser(inputStream, false);
					while (nxp.hasNext()) {
						Node[] quard = nxp.next();
						String s = quard[0].toN3().trim();
						String p = quard[1].toN3().trim();
						String o = quard[2].toN3().trim();
						if (p.equals("<http://dbpedia.org/ontology/abstract>") & o.contains("@en")) {
							// System.out.println(o);
							String content = o.substring(o.indexOf("\"") + 1, o.lastIndexOf("\""));
							String label = "";
							if (names.containsKey(s)) {
								label = names.get(s);
								names.remove(s);
							}

							Document doc = new Document();
							doc.add(new Field("url", s, Field.Store.YES, Field.Index.NOT_ANALYZED));
							doc.add(new Field("label", label, Field.Store.YES, Field.Index.ANALYZED));
							doc.add(new Field("description", content, Field.Store.YES, Field.Index.ANALYZED));
							writer.addDocument(doc);
						}
					}
				}
			}
			writer.commit();	
		}
		
		for (Entry<String, String> entry : names.entrySet()) {
			Document doc = new Document();
			doc.add(new Field("url", entry.getKey(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("label", entry.getValue(), Field.Store.YES, Field.Index.ANALYZED));
			doc.add(new Field("description", entry.getValue(), Field.Store.YES, Field.Index.ANALYZED));
			writer.addDocument(doc);
		}

		writer.commit();
		writer.close();
		names.clear();
		br.close();
		
	}

	@Override
	public void writeArticleType(IndexWriter writer, String articleTypeFile)
			throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, HashSet<String>> articleClass = new HashMap<String, HashSet<String>>();

		HashMap<String, HashSet<String>> ancestors = new HashMap<String, HashSet<String>>();
		HashMap<String, HashSet<String>> tmp = new HashMap<String, HashSet<String>>();

		System.out.println("enrichment ...");
		for (Entry<String, HashSet<String>> entry : fathers.entrySet()) {
			HashSet<String> aset = new HashSet<String>();
			LinkedList<String> list = new LinkedList<String>();
			for (String string : entry.getValue()) {
				list.add(string);
			}
			TaxonomyUtil.enrichment(fathers, list, aset);
			ancestors.put(entry.getKey(), aset);
		}

		BufferedReader br = new BufferedReader(new FileReader(new File(articleTypeFile)));
		String line = null;
		while ((line = br.readLine()) != null) {
			if (line.startsWith("<http://dbpedia.org/resource/")) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser(inputStream, false);
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();
					if (p.equals("<http://purl.org/dc/terms/subject>")
							|| p.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")) {
						// System.out.println(o);
						if (o.startsWith("<http://dbpedia.org/resource/Category:")
								|| o.startsWith("<http://dbpedia.org/ontology/")) {

							if (tmp.containsKey(s)) {
								tmp.get(s).add(o);

							} else {
								HashSet<String> father = new HashSet<String>();
								father.add(o);
								tmp.put(s, father);
							}

						}
					}
				}
			}
		}

		System.out.println("feed with category ...");

		for (Entry<String, HashSet<String>> entry : tmp.entrySet()) {
			HashSet<String> tmpSet = new HashSet<String>();
			for (String string : entry.getValue()) {
				if (ancestors.containsKey(string)) {
					HashSet<String> set = (HashSet<String>) ancestors.get(string).clone();
					tmpSet.addAll(set);
				}
			}
			entry.getValue().removeAll(tmpSet);
			articleClass.put(entry.getKey(), entry.getValue());
		}
		
		for (Entry<String, HashSet<String>> entry : articleClass.entrySet()) {
			String string="";
			for(String type:entry.getValue()){
				string+=type+" ";
			}
			
			
			
			if(entry.getKey().equals("<http://dbpedia.org/resource/Title_Bout_Championship_Boxing>")){
				System.out.println("==========================");
				System.out.println(entry.getValue());
				System.out.println("==========================");
			}
			
			
			Document doc = new Document();
			doc.add(new Field("url", entry.getKey(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			doc.add(new Field("types", string.trim(), Field.Store.YES, Field.Index.NOT_ANALYZED));
			writer.addDocument(doc);
		}
		ancestors.clear();
		fathers.clear();
		articleClass.clear();
		writer.commit();
		writer.close();
		br.close();
	}


}

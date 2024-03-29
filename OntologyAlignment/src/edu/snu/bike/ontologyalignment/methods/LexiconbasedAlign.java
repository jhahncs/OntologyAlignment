/**
 * 
 */
package edu.snu.bike.ontologyalignment.methods;

import java.io.File;

import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.blooms.BLOOMS;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;
import edu.snu.bike.ontologyalignment.models.search.KnowledgeBaseIndexer;

/**
 * 
 *
 */
public class LexiconbasedAlign implements Align {

	
	private Config config = null;

	private SimpleDirectedGraph<String, DefaultEdge> graph = null;

	public SimpleDirectedGraph<String, DefaultEdge> getGraph() {
		return graph;
	}

	private LexiconbasedMapper mapper = null;
	private InputOntologies input = null;

	public InputOntologies getInput() {
		return input;
	}

	public void setInput(InputOntologies input) {
		this.input = input;
	}

	public LexiconbasedAlign(String taxonomyFile1, String taxonomyFile2, Config config) throws Exception {

		setting(config);
		InputOntologiesLoad inputLoad = new InputOntologiesLoad(taxonomyFile1, taxonomyFile2);
		input = inputLoad.getData();

	}

	

	public void setting(Config config) {
		this.config = config;
	}
	
	
	/* (non-Javadoc)
	 * @see edu.snu.bike.ontologyalignment.methods.Align#align(edu.snu.bike.ontologyalignment.models.data.InputOntologies, edu.snu.bike.ontologyalignment.methods.Config)
	 */
	@Override
	public SimpleDirectedGraph<String, DefaultEdge> align(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		
		Directory typeDirectory = null;
		Directory articleDirectory = null;
		Directory taxonomyDirectory = null;
		
		if (config.isUseRam()) {
			String at=config.getArticleTypeFile();
			String al=config.getArticleLableFile();
			String ad=config.getArticleDescriptionFile();
			String rt=config.getReferTaxonomy();
			
			KnowledgeBaseIndexer indexer=new KnowledgeBaseIndexer(al, ad, at, rt);
			indexer.index();
			
			typeDirectory=indexer.getTypeDirectory();
			articleDirectory=indexer.getArticleDirectory();
			taxonomyDirectory=indexer.getTaxonomyDirectory();
			
			
		} else {
			// uses index in the file system
			
			typeDirectory=FSDirectory.open(new File(config.getTypeDirectory()));
			articleDirectory=FSDirectory.open(new File(config.getArticleDirectory()));
			taxonomyDirectory=FSDirectory.open(new File(config.getTaxonomyDirectory()));
			
		}
		
		mapper = new BLOOMS(typeDirectory,articleDirectory,taxonomyDirectory,config.getWordNetDirectory());
		
		graph = mapper.mapping(input, config);
		return graph;
		
	}

	/* (non-Javadoc)
	 * @see edu.snu.bike.ontologyalignment.methods.Align#getAlignments(org.jgrapht.graph.SimpleDirectedGraph, edu.snu.bike.ontologyalignment.methods.Config)
	 */
	@Override
	public SimpleDirectedGraph<String, DefaultEdge> getAlignments(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config) {
		AlignmentCollector collector = new AlignmentCollector();
		SimpleDirectedGraph<String, DefaultEdge> results = collector.collect(graph, config);
		return results;
	}

}

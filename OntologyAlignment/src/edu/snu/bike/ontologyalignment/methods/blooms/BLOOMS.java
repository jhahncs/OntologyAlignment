package edu.snu.bike.ontologyalignment.methods.blooms;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.store.Directory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.semanticweb.owl.align.AlignmentException;
import org.semanticweb.owl.align.Cell;

import com.hp.hpl.jena.ontology.OntClass;

import edu.mit.jwi.item.IWord;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.LexiconbasedMapper;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;
import edu.snu.bike.ontologyalignment.models.search.KnowledgeBaseSearcher;
import edu.wright.cs.knoesis.blooms.module.wordnet.wordnetop.WordnetTester;
import edu.wright.cs.knoesis.datastructure.BloomsNode;
import edu.wright.cs.knoesis.datastructure.BloomsTree;
import edu.wright.cs.knoesis.datastructure.BreadthFirstSearch;
import edu.wright.cs.knoesis.relationshipfinder.BloomsRelationship;
import edu.wright.cs.knoesis.relationshipfinder.BloomsRelationshipFinder;
import fr.inrialpes.exmo.align.impl.BasicCell;
import fr.inrialpes.exmo.align.impl.BasicRelation;

public class BLOOMS implements LexiconbasedMapper {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private Directory typeDirectory;
	private Directory articleDirectory;
	private Directory taxonomyDirectory;
	
	private HashMap ont1ConceptToTreeMap;
	private HashMap ont2ConceptToTreeMap;
	private HashMap ont1NameMapper;
	private HashMap ont2NameMapper;
	private SimpleDirectedGraph<String, DefaultEdge> graph = null;
	
	private KnowledgeBaseSearcher searcher;
	private static int cellID = 1;
	private WordnetTester wntester;
	
	
	public BLOOMS(Directory typeDirectory, Directory articleDirectory, Directory taxonomyDirectory, String wordNetDir) {
		this.typeDirectory = typeDirectory;
		this.articleDirectory = articleDirectory;
		this.taxonomyDirectory = taxonomyDirectory;
		searcher=new KnowledgeBaseSearcher(typeDirectory, articleDirectory, taxonomyDirectory);
		wntester=new WordnetTester(wordNetDir);
	}

	@Override
	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		initial(input, config);
		match(0.95);
		return getGraph();
	}

	@Override
	public void initial(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		
		graph= new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
		
		HashSet<String> classSet1 = input.getClassSet1();
		HashSet<String> classSet2 = input.getClassSet1();
		HashMap<String, String> labels = input.getClassLabels();

		long start = System.currentTimeMillis();
		for (String class1 : classSet1) {
			String className = labels.get(class1);
			String url = class1;
			// System.err.println(entry);
			if (className != null || className != "null") {
				// System.err.println("className: "+className);
				BloomsTree dbpediaTree = createBloomsTree(className);
				BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode) dbpediaTree.getRoot());
				bfsSearch.printBFSTraversal();
				if (dbpediaTree != null) {
					ont1ConceptToTreeMap.put(url, dbpediaTree);
				}

			}
		}

		// System.out.println("====================================");

		for (String class2 : classSet2) {
			String className = labels.get(class2);
			String url = class2;
			// System.err.println(entry);
			if (className != null || className != "null") {
				// System.err.println("className: "+className);
				BloomsTree dbpediaTree = createBloomsTree(className);
				BreadthFirstSearch bfsSearch = new BreadthFirstSearch((BloomsNode) dbpediaTree.getRoot());
				bfsSearch.printBFSTraversal();
				if (dbpediaTree != null) {
					ont2ConceptToTreeMap.put(url, dbpediaTree);
				}

			}
		}

		System.out.println("search finished. data size: " + ont1ConceptToTreeMap.size() + " time spend: "
				+ (System.currentTimeMillis() - start) + " (ms)");

	}

	public void match(Double tHold) throws Exception {

		int counter = 0;
		for (Iterator ont1ConceptItr = ont1ConceptToTreeMap.keySet().iterator(); ont1ConceptItr.hasNext();) {
			counter++;
			String concept1 = (String) ont1ConceptItr.next();
			System.out.println("mapping: " + concept1 + " id: " + counter + " percentage: "
					+ ((double) counter) / ont1ConceptToTreeMap.size());
			BloomsTree sourceTree = (BloomsTree) ont1ConceptToTreeMap.get(concept1);
			for (Iterator ont2ConceptItr = ont2ConceptToTreeMap.keySet().iterator(); ont2ConceptItr.hasNext();) {
				String concept2 = (String) ont2ConceptItr.next();

				if (!concept1.equals(concept2)) {

					BloomsTree targetTree = (BloomsTree) ont2ConceptToTreeMap.get(concept2);
					BloomsRelationshipFinder wtrf = new BloomsRelationshipFinder(this.wntester);
					BasicRelation rel = wtrf.identifyRelationship(sourceTree, targetTree);
					// System.out.println((new
					// StringBuilder("Concept-1:")).append(concept1).toString());
					// System.out.println((new
					// StringBuilder("Concept-2:")).append(concept2).toString());
					HashMap commonNodeHMap = sourceTree.commonNodes(targetTree);
					BloomsNode n;
					// for(Iterator nodeItr =
					// commonNodeHMap.keySet().iterator(); nodeItr.hasNext();
					// System.out.println((new StringBuilder("Common
					// Node:")).append(n.getNodeName()).append("
					// Level:").append(n.getLevel()).toString()))
					// n = (BloomsNode)nodeItr.next();
					if (rel != null) {
						double strength = wtrf.getRelationshipStrength().doubleValue();
						// System.out.println(rel.getRelation()+" "+strength);
						if (strength > tHold.doubleValue()) {

							if (rel.getRelation().equals("=")) {
								graph.addEdge(concept1, concept2);
								graph.addEdge(concept2, concept1);

							} else if (rel.getRelation().equals("<")) {

								graph.addEdge(concept2, concept1);
							} else {
								graph.addEdge(concept1, concept2);
							}
						}
					}
				}

			}

		}
		
	}

	void findRelationships(ArrayList ont1NormalizedNameList, ArrayList ont2NormalizedNameList) {
		for (int i = 0; i < ont1NormalizedNameList.size(); i++) {
			String class1 = (String) ont1NormalizedNameList.get(i);
			for (int j = 0; j < ont2NormalizedNameList.size(); j++) {
				String class2 = (String) ont2NormalizedNameList.get(j);
				match(class1, class2);
			}

		}

	}

	void printHypernyms(String phrase, List hypernymList) {
		for (int i = 0; i < hypernymList.size(); i++) {
			IWord iword = (IWord) hypernymList.get(i);
		}

	}

	BloomsTree createBloomsTree(String phrase) throws Exception {
	

		Set<String> articles = searcher.getArticle(phrase.toLowerCase());

		BloomsNode rootNode = new BloomsNode(phrase, true);
		BloomsTree categoryTree = new BloomsTree(rootNode, true);
		
		for (String article:articles) {

			BloomsNode articleNode = new BloomsNode(article, true);
			articleNode.setParent(rootNode);
			BloomsTree articleTree = new BloomsTree(articleNode, true);
			articleNode = constructCategoryTree(articleNode);
			
			for (int i = 0; i < 1; i++) {
				
				List bloomsNodeList = articleTree.getLeaves();
				
				for (int k = 0; k < bloomsNodeList.size(); k++) {
					BloomsNode n = (BloomsNode) bloomsNodeList.get(k);
					constructSubTree(n);
					// System.err.println(phrase+" "+articleName+"
					// "+n.getUserObject()+" "+n.getChildCount());
				}

			}

		}

		return categoryTree;
	}

	BloomsNode constructCategoryTree(BloomsNode node) throws Exception {
		String name = (String) node.getUserObject();
		constructCategoryTree(name, node);
		return node;
	}

	void constructCategoryTree(String name, BloomsNode n)
			throws Exception {

		Set<String> fathers = searcher.getTypes(name);
		addChildrenToParent(fathers, n);
	}

	void addChildrenToParent(Set<String> nodes, BloomsNode n) throws CorruptIndexException, IOException {
		for (String name : nodes) {
			BloomsNode sub = new BloomsNode(name, true);
			sub.setParent(n);
		}

	}
	
	BloomsNode constructSubTree(BloomsNode node) throws Exception {
		String name = (String) node.getUserObject();
		constructSubTree(name, node);
		return node;
	}

	void constructSubTree(String name, BloomsNode n)
			throws Exception {

		Set<String> fathers = searcher.getFathers(name);
		addChildrenToParent(fathers, n);
	}

	public SimpleDirectedGraph<String, DefaultEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleDirectedGraph<String, DefaultEdge> graph) {
		this.graph = graph;
	}
	
	public Cell match(String o1, String o2) {
		BasicCell c = null;
		String s1;
		String s2;
		s1 = o1;
		s2 = o2;
		if (s1 == null || s2 == null)
			return c;
		if (s1.toLowerCase().equals(s2.toLowerCase())) {
			String cellIDStr = Integer.toString(cellID);
			if (ont1NameMapper.get(s1) != null && ont2NameMapper.get(s2) != null) {
				String arg1 = ((OntClass) ont1NameMapper.get(s1)).toString();
				String arg2 = ((OntClass) ont2NameMapper.get(s2)).toString();
				URI uri1 = null;
				URI uri2 = null;
				try {
					uri2 = new URI(arg2);
					uri1 = new URI(arg1);
					double strength = 1.0D;
					c = new BasicCell(cellIDStr, uri1, uri2, BloomsRelationship.getEquivalenceRelation(), strength);
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (AlignmentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			return c;
		}
		try {
			return c;
		} catch (Exception owex) {
			owex.printStackTrace();
		}
		return c;
	}

}

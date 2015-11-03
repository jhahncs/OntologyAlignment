package edu.snu.bike.ontologyalignment.methods.iut;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.approches.lsh.HashBean;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.Mapper;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;
import edu.snu.bike.ontologyalignment.util.GraphUtil;
import edu.snu.bike.ontologyalignment.util.SimCaculater;

public class IUT implements Mapper {

	// HashMap<String, String[]> data = new HashMap<String, String[]>();
	private HashMap<String, HashSet<String>> data = null;
	private String most = "";
	private int numerOfInstance = 0;
	private Config config = null;

	private Multigraph<String, DefaultEdge> similarGraph = new Multigraph<String, DefaultEdge>(DefaultEdge.class);
	// class-class similarity (using a value of predicates as a class)
	private ArrayList<HashSet<String>> rankedCentralityClasses = null;

	private SimpleDirectedGraph<String, DefaultEdge> graph = null;

	private HashMap<String, HashSet<String>> taxonomy1 = null; // map<song,set<fathers>>;
	private HashMap<String, HashSet<String>> taxonomy2 = null;

	public IUT() {

	}

	@Override
	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config) throws IOException {
		// TODO Auto-generated method stub

		initial(input, config);
		int counter = 0;
		for (int i = rankedCentralityClasses.size() - 1; i > 0 || i == 0; i--) {
			if (rankedCentralityClasses.get(i) != null) {
				HashSet<String> currentCls = rankedCentralityClasses.get(i);
				for (String topNode : currentCls) {

					HashBean bean = null;

					if (config.getType().equals(Config.intra)) {
						bean = findMostIntraSimilar(topNode);
					} else {
						bean = findMostInterSimilar(topNode);
					}

					// ?
					GraphUtil.update(graph, topNode, bean, config.isTransitive());

				}
			}
		}

		return graph;
	}

	public void initial(InputOntologies input, Config config) throws IOException {
		if (input.getTaxonomy1() != null) {
			this.taxonomy1 = input.getTaxonomy1();
		} else {
			this.taxonomy2 = new HashMap<>();
		}

		if (input.getTaxonomy2() != null) {
			this.taxonomy2 = input.getTaxonomy2();
		} else {
			this.taxonomy2 = new HashMap<>();
		}

		this.config = config;
		this.data = input.getCommonInstances();

		rankedCentralityClasses = new ArrayList<HashSet<String>>();
		graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		HashMap<String, Integer> importantCls = new HashMap<String, Integer>();
		int max = 0;
		for (Entry<String, HashSet<String>> entry : data.entrySet()) {
			importantCls.put(entry.getKey(), entry.getValue().size());
			if (entry.getValue().size() > max) {
				max = entry.getValue().size();
			}
		}

		for (int i = 0; i < max + 1; i++) {
			rankedCentralityClasses.add(i, new HashSet<String>());
		}

		for (Entry<String, Integer> entry : importantCls.entrySet()) {
			rankedCentralityClasses.get(entry.getValue()).add(entry.getKey());
		}

		System.out.println("init finish");
	}

	public static LinkedList<String> sortMap(HashMap<String, Integer> map) {

		List<Map.Entry<String, Integer>> noderanked = new ArrayList<Map.Entry<String, Integer>>(map.entrySet());
		Collections.sort(noderanked, new Comparator<Map.Entry<String, Integer>>() {
			@Override
			public int compare(Map.Entry<String, Integer> obj1, Map.Entry<String, Integer> obj2) {
				return ((Comparable) ((Map.Entry) (obj2)).getValue()).compareTo(((Map.Entry) (obj1)).getValue());

			}
		});
		LinkedList<String> list = new LinkedList<String>();
		for (Map.Entry<String, Integer> entry : noderanked) {
			list.add(entry.getKey());
		}

		return list;
	}

	public HashBean findMostIntraSimilar(String name) throws FileNotFoundException {
		HashBean bean = new HashBean();
		HashMap<String, Integer> sups = new HashMap<String, Integer>();
		HashMap<String, Integer> subs = new HashMap<String, Integer>();
		HashMap<String, Integer> equals = new HashMap<String, Integer>();// name,

		for (String compareNode : graph.vertexSet()) {
			if (!compareNode.equals(name)) {

				int common = SimCaculater.strictIntersection(data.get(name), data.get(compareNode)).size(); // fast
																											// sort
				int insS = data.get(name).size();
				int insT = data.get(compareNode).size();
				Double sim1 = (double) common / insS;
				Double sim2 = (double) common / insT;
				Double jaccard = (double) common / (insS + insT - common);

				if (sim1.compareTo(config.getXs()) >= 0 && sim2.compareTo(config.getXs()) >= 0) {
					if (!equals.containsKey(compareNode)) {
						equals.put(compareNode, 1);
					}
				} else if (sim1.compareTo(config.getXs()) >= 0) {
					if (!sups.containsKey(compareNode)) {
						sups.put(compareNode, 1);
					}
				} else if (sim2.compareTo(config.getXs()) >= 0) {
					if (!subs.containsKey(compareNode)) {
						subs.put(compareNode, 1);
					}
				} else if (jaccard.compareTo(config.getXe()) >= 0) {
					if (!equals.containsKey(compareNode)) {
						equals.put(compareNode, 1);
					}
				}
			}

		}

		bean.setSups(sups);
		bean.setSubs(subs);
		bean.setEquals(equals);
		return bean;
	}

	public HashBean findMostInterSimilar(String name) throws FileNotFoundException {
		HashBean bean = new HashBean();
		HashMap<String, Integer> sups = new HashMap<String, Integer>();
		HashMap<String, Integer> subs = new HashMap<String, Integer>();
		HashMap<String, Integer> equals = new HashMap<String, Integer>();// name,
																			// common
		for (String compareNode : graph.vertexSet()) {
			String nameSpace1 = "";
			String nameSpace2 = "";

			if (name.startsWith(config.getNamespace1())) {
				nameSpace1 = config.getNamespace1();
			}
			if (name.startsWith(config.getNamespace2())) {
				nameSpace1 = config.getNamespace2();
			}

			if (compareNode.startsWith(config.getNamespace1())) {
				nameSpace2 = config.getNamespace1();
			}
			if (compareNode.startsWith(config.getNamespace2())) {
				nameSpace2 = config.getNamespace2();
			}

			if (!compareNode.equals(name) && !nameSpace1.equals(nameSpace2)) {
				int common = SimCaculater.strictIntersection(data.get(name), data.get(compareNode)).size(); // fast
																											// sort
				int insS = data.get(name).size();
				int insT = data.get(compareNode).size();
				Double sim1 = (double) common / insS;
				Double sim2 = (double) common / insT;
				Double jaccard = (double) common / (insS + insT - common);

				if (sim1.compareTo(config.getXs()) >= 0 && sim2.compareTo(config.getXs()) >= 0) {
					if (!equals.containsKey(compareNode)) {
						equals.put(compareNode, 1);
					}
				} else if (sim1.compareTo(config.getXs()) >= 0) {
					if (!sups.containsKey(compareNode)) {
						sups.put(compareNode, 1);
					}
				} else if (sim2.compareTo(config.getXs()) >= 0) {
					if (!subs.containsKey(compareNode)) {
						subs.put(compareNode, 1);
					}
				} else if (jaccard.compareTo(config.getXe()) >= 0) {
					if (!equals.containsKey(compareNode)) {
						equals.put(compareNode, 1);
					}
				}

			} else if (nameSpace1.equals(nameSpace2)) {
				// original relations
				if (this.taxonomy1.containsKey(name)) {
					if (taxonomy1.get(name).contains(compareNode)) {
						if (!sups.containsKey(compareNode)) {
							sups.put(compareNode, 1);
						}
					}

				} else if (this.taxonomy2.containsKey(name)) {
					if (taxonomy2.get(name).contains(compareNode)) {
						if (!sups.containsKey(compareNode)) {
							sups.put(compareNode, 1);
						}
					}
				} else if (this.taxonomy1.containsKey(compareNode)) {
					if (taxonomy1.get(compareNode).contains(name)) {
						if (!subs.containsKey(compareNode)) {
							subs.put(compareNode, 1);
						}
					}
				} else if (this.taxonomy2.containsKey(compareNode)) {
					if (taxonomy2.get(compareNode).contains(name)) {
						if (!subs.containsKey(compareNode)) {
							subs.put(compareNode, 1);
						}
					}
				}
			}
		}

		bean.setSups(sups);
		bean.setSubs(subs);
		bean.setEquals(equals);
		return bean;
	}

}

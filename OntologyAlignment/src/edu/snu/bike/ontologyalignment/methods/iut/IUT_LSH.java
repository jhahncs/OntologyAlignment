package edu.snu.bike.ontologyalignment.methods.iut;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.Multigraph;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstanceBasedMapper;
import edu.snu.bike.ontologyalignment.methods.InstancebasedMapper;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;
import edu.snu.bike.ontologyalignment.scaling.lsh.BitData;
import edu.snu.bike.ontologyalignment.scaling.lsh.BitMinHash;
import edu.snu.bike.ontologyalignment.scaling.lsh.DataLoader;
import edu.snu.bike.ontologyalignment.scaling.lsh.HashBean;
import edu.snu.bike.ontologyalignment.util.GraphUtil;
import edu.snu.bike.ontologyalignment.util.SimCaculater;

public class IUT_LSH implements InstancebasedMapper {

	private HashMap<String, HashSet<String>> data = null;
	private String most = "";
	private int numerOfInstance = 0;
	private Config config = null;
	private BitData mdata = null;
	private BitMinHash hash;

	private Multigraph<String, DefaultEdge> similarGraph = new Multigraph<String, DefaultEdge>(DefaultEdge.class);
	// class-class similarity (using a value of predicates as a class)
	private ArrayList<HashSet<String>> rankedCentralityClasses = null;

	private SimpleDirectedGraph<String, DefaultEdge> graph = null;

	private HashMap<String, HashSet<String>> taxonomy1 = null; // map<song,set<fathers>>;
	private HashMap<String, HashSet<String>> taxonomy2 = null;
	private HashMap<String, Integer> cls = null;
	private HashMap<String, Integer> ins = null;

	public IUT_LSH() {

	}

	@Override
	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config)
			throws FileNotFoundException {
		// TODO Auto-generated method stub
		for (int i = rankedCentralityClasses.size() - 1; i > 0 || i == 0; i--) {
			if (rankedCentralityClasses.get(i) != null) {
				HashSet<String> currentCls = rankedCentralityClasses.get(i);
				for (String topNode : currentCls) {

					HashBean bean = null;

					if (config.getType().equals(Config.intra)) {
						bean = findMostIntraSimilar(hash, topNode);
					} else {
						bean = findMostInterSimilar(hash, topNode);
					}

					// ?
					GraphUtil.update(graph, topNode, bean, config.isTransitive());

				}
			}
		}

		return graph;
	}
	
	@Override
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

		cls = new HashMap<String, Integer>();
		ins = new HashMap<String, Integer>();

		this.config = config;
		this.data = input.getCommonInstances();

		rankedCentralityClasses = new ArrayList<HashSet<String>>();
		graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);

		HashMap<String, Integer> importantCls = new HashMap<String, Integer>();
		int clsIdx = 0;
		int insIdx = 0;
		int max = 0;
		for (Entry<String, HashSet<String>> entry : data.entrySet()) {
			importantCls.put(entry.getKey(), entry.getValue().size());
			cls.put(entry.getKey(), clsIdx);
			clsIdx++;
			if (entry.getValue().size() > max) {
				max = entry.getValue().size();
			}
			for (String list : entry.getValue()) {
				if (!ins.containsKey(list)) {
					ins.put(list, insIdx);
					insIdx++;
				}
			}
		}

		for (int i = 0; i < max + 1; i++) {
			rankedCentralityClasses.add(i, new HashSet<String>());
		}

		for (Entry<String, Integer> entry : importantCls.entrySet()) {
			rankedCentralityClasses.get(entry.getValue()).add(entry.getKey());
		}

		DataLoader loader = new DataLoader();
		mdata = loader.loadbit(data, cls, ins);
		hash = new BitMinHash(mdata, config.getNumberofRow(), config.getNumofSig());
		hash.hash();
		hash.banding();
		System.out.println("init finish");
	}

	public HashBean findMostIntraSimilar(BitMinHash hash, String name) throws FileNotFoundException {

		HashSet<String> compares = new HashSet<String>();
		for (String compareNode : graph.vertexSet()) {
			if (!compareNode.equals("root") && !compareNode.equals(name)) {
				compares.add(compareNode);
				// System.out.println(topNode+"; "+compareNodeName);
			}
		}
		HashSet<String> hashCandi = hash.findCandidates(name);
		HashSet<String> tmp = (HashSet<String>) hashCandi.clone();
		tmp.retainAll(compares);
		HashSet<String> candidates = tmp;

		HashBean bean = new HashBean();
		HashMap<String, Integer> sups = new HashMap<String, Integer>();
		HashMap<String, Integer> subs = new HashMap<String, Integer>();
		HashMap<String, Integer> equals = new HashMap<String, Integer>();// name,
																			// common
		for (String candidate : candidates) {
			int common = SimCaculater.strictIntersection(data.get(name), data.get(candidate)).size();
			int insS = data.get(name).size();
			int insT = data.get(candidate).size();
			Double sim1 = (double) common / insS;
			Double sim2 = (double) common / insT;
			Double jaccard = (double) common / (insS + insT - common);

			if (sim1.compareTo(config.getXs()) >= 0 && sim2.compareTo(config.getXs()) >= 0) {
				if (!equals.containsKey(candidate)) {
					equals.put(candidate, 1);
				}
			} else if (sim1.compareTo(config.getXs()) >= 0) {
				if (!sups.containsKey(candidate)) {
					sups.put(candidate, 1);
				}
			} else if (sim2.compareTo(config.getXs()) >= 0) {
				if (!subs.containsKey(candidate)) {
					subs.put(candidate, 1);
				}
			} else if (jaccard.compareTo(config.getXe()) >= 0) {
				if (!equals.containsKey(candidate)) {
					equals.put(candidate, 1);
				}
			}

		}
		bean.setSups(sups);
		bean.setSubs(subs);
		bean.setEquals(equals);
		bean.setSearchSpace(candidates.size());
		return bean;
	}

	public HashBean findMostInterSimilar(BitMinHash hash, String name) throws FileNotFoundException {

		HashSet<String> compares = new HashSet<String>();
		for (String compareNode : graph.vertexSet()) {
			if (!compareNode.equals("root") && !compareNode.equals(name)) {
				compares.add(compareNode);
				// System.out.println(topNode+"; "+compareNodeName);
			}
		}
		HashSet<String> hashCandi = hash.findCandidates(name);
		HashSet<String> tmp = (HashSet<String>) hashCandi.clone();
		tmp.retainAll(compares);
		HashSet<String> candidates = tmp;

		HashBean bean = new HashBean();
		HashMap<String, Integer> sups = new HashMap<String, Integer>();
		HashMap<String, Integer> subs = new HashMap<String, Integer>();
		HashMap<String, Integer> equals = new HashMap<String, Integer>();// name,
																			// common
		for (String candidate : candidates) {
			String nameSpace1 = "";
			String nameSpace2 = "";

			if (name.startsWith(config.getNamespace1())) {
				nameSpace1 = config.getNamespace1();
			}
			if (name.startsWith(config.getNamespace2())) {
				nameSpace1 = config.getNamespace2();
			}

			if (candidate.startsWith(config.getNamespace1())) {
				nameSpace2 = config.getNamespace1();
			}
			if (candidate.startsWith(config.getNamespace2())) {
				nameSpace2 = config.getNamespace2();
			}

			if (!name.equals(candidate) && !nameSpace1.equals(nameSpace2)) {

				int common = SimCaculater.strictIntersection(data.get(name), data.get(candidate)).size();
				int insS = data.get(name).size();
				int insT = data.get(candidate).size();
				Double sim1 = (double) common / insS;
				Double sim2 = (double) common / insT;
				Double jaccard = (double) common / (insS + insT - common);

				if (sim1.compareTo(config.getXs()) >= 0 && sim2.compareTo(config.getXs()) >= 0) {
					if (!equals.containsKey(candidate)) {
						equals.put(candidate, 1);
					}
				} else if (sim1.compareTo(config.getXs()) >= 0) {
					if (!sups.containsKey(candidate)) {
						sups.put(candidate, 1);
					}
				} else if (sim2.compareTo(config.getXs()) >= 0) {
					if (!subs.containsKey(candidate)) {
						subs.put(candidate, 1);
					}
				} else if (jaccard.compareTo(config.getXe()) >= 0) {
					if (!equals.containsKey(candidate)) {
						equals.put(candidate, 1);
					}
				}
			} else if (nameSpace1.equals(nameSpace2)) {
				// original relations
				if (this.taxonomy1.containsKey(name)) {
					if (taxonomy1.get(name).contains(candidate)) {
						if (!sups.containsKey(candidate)) {
							sups.put(candidate, 1);
						}
					}

				} else if (this.taxonomy2.containsKey(name)) {
					if (taxonomy2.get(name).contains(candidate)) {
						if (!sups.containsKey(candidate)) {
							sups.put(candidate, 1);
						}
					}
				} else if (this.taxonomy1.containsKey(candidate)) {
					if (taxonomy1.get(candidate).contains(name)) {
						if (!subs.containsKey(candidate)) {
							subs.put(candidate, 1);
						}
					}
				} else if (this.taxonomy2.containsKey(candidate)) {
					if (taxonomy2.get(candidate).contains(name)) {
						if (!subs.containsKey(candidate)) {
							subs.put(candidate, 1);
						}
					}
				}
			}
		}
		bean.setSups(sups);
		bean.setSubs(subs);
		bean.setEquals(equals);
		bean.setSearchSpace(candidates.size());
		return bean;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

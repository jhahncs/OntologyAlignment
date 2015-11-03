package edu.snu.bike.ontologyalignment.evaluation;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class FMeasure {

	public HashSet<String> goldSubs;
	public HashSet<String> goldeqls;
	public HashSet<String> compareeqls;
	public HashSet<String> comparesubs;
	public SimpleDirectedGraph<String, DefaultEdge> gold;
	public SimpleDirectedGraph<String, DefaultEdge> compare;
	// public BufferedWriter bw = new BufferedWriter(new FileWriter(new
	// File("C://research/UTIE201403/experiment/yago2yago/log.txt")));

	public HashMap<String, Boolean> subcase1 = new HashMap<String, Boolean>(); // poor
																				// description
	public HashMap<String, Boolean> subcase2 = new HashMap<String, Boolean>(); // multiple
																				// instantiation

	public HashMap<String, Boolean> eqlcase1 = new HashMap<String, Boolean>(); // poor
																				// description
	public HashMap<String, Boolean> eqlcase2 = new HashMap<String, Boolean>(); // multiple
																				// instantiation

	public HashSet<String> case1 = new HashSet<String>(); // false positive
	public HashSet<String> case2 = new HashSet<String>(); // false negative
	public HashSet<String> corrects = new HashSet<String>();

	public FMeasure(SimpleDirectedGraph<String, DefaultEdge> g, SimpleDirectedGraph<String, DefaultEdge> c)
			throws Exception {
		goldSubs = new HashSet<String>();
		goldeqls = new HashSet<String>();
		compareeqls = new HashSet<String>();
		comparesubs = new HashSet<String>();

		gold = g;
		compare = c;
		for (DefaultEdge edge : g.edgeSet()) {
			String[] nodes = edge.toString().split(" : ");
			String node1 = nodes[0].trim().substring(1).trim();
			String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim();
			if (!g.containsEdge(node2, node1)) {
				goldSubs.add(edge.toString());
			} else {
				goldeqls.add(edge.toString());
				goldeqls.add(g.getEdge(node2, node1).toString());

			}

		}

		for (DefaultEdge edge : c.edgeSet()) {
			String[] nodes = edge.toString().split(" : ");
			String node1 = nodes[0].trim().substring(1).trim();
			String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim();
			if (!c.containsEdge(node2, node1)) {
				comparesubs.add(edge.toString());
			} else {
				compareeqls.add(edge.toString());
				compareeqls.add(c.getEdge(node2, node1).toString());
			}

		}
		System.out.println("========== subsumption alignments ======");
		for (String string : comparesubs) {
			System.out.println(string);
		}

		System.out.println("============ eql alignments ============");

		for (String string : compareeqls) {
			System.err.println(string);
		}

		System.out.println("golden sub size: " + goldSubs.size());
		System.out.println("compare sub size: " + comparesubs.size());
		System.out.println("golden eql size: " + goldeqls.size());
		System.out.println("compare eql size: " + compareeqls.size());
	}

	public Double eqlPrecision() {

		int size = compareeqls.size();
		int match = 0;

		for (String edge : compareeqls) {
			if (goldeqls.contains(edge)) {
				corrects.add(edge);
				match++;
			} else {
				if (!eqlcase1.containsKey(edge)) {
					eqlcase1.put(edge, null);
				}
				if (eqlcase2.containsKey(edge)) {
					System.err.println("@ warning: case1 in case2");
				}

			}

		}
		return (double) match / size;
	}

	public Double eqlRecall() {

		int size = goldeqls.size();
		int match = 0;

		for (String edge : goldeqls) {
			if (compareeqls.contains(edge)) {
				match++;
			} else {
				if (!eqlcase2.containsKey(edge)) {
					eqlcase2.put(edge, null);
				}
				if (eqlcase1.containsKey(edge)) {
					System.err.println("@ warning: case2 in case1");
				}
			}

		}
		return (double) match / size;
	}

	public Double fscore(Double p, Double r) {
		System.out.println("precision: " + p);
		System.out.println("recall: " + r);
		System.out.println("f score: " + 2 * (r * p) / (r + p));
		return 2 * (r * p) / (r + p);
	}

	public Double subsumptionPrecision() throws IOException {
		int match = 0;
		int size = comparesubs.size();
		for (String edge : comparesubs) {
			if (goldSubs.contains(edge)) {
				match++;
				// System.err.println("@ correct link: "+edge+"\r\n");
			} else {
				case1.add(edge);
				String[] nodes = edge.toString().trim().split(" : ");
				String node1 = nodes[0].trim().substring(1).trim(); // son
				String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim(); // father
				boolean poorDescription = false;
				for (DefaultEdge e : this.compare.incomingEdgesOf(node1)) {
					String[] commonNodes = e.toString().trim().split(" : ");
					String common = commonNodes[0].trim().substring(1).trim();
					if (compare.containsEdge(node1, common)) {
						poorDescription = true;
						break;
					}
				}

				if (poorDescription) {
					for (DefaultEdge e : this.compare.incomingEdgesOf(node2)) {
						String[] commonNodes = e.toString().trim().split(" : ");
						String common = commonNodes[0].trim().substring(1).trim();
						if (compare.containsEdge(node2, common)) {
							poorDescription = true;
							break;
						}
					}

				}

				if (poorDescription) {
					// bw.write("@ wrong link 1: "+edge+"\r\n");
					if (!subcase1.containsKey(edge)) {
						subcase1.put(edge, null);
					}
					if (subcase2.containsKey(edge)) {
						System.err.println("@ warning: case1 in case2");
					}
				} else {
					if (!subcase2.containsKey(edge)) {
						subcase2.put(edge, null);
					}
					if (subcase1.containsKey(edge)) {
						System.err.println("@ warning: case2 in case1");
					}
				}

				// System.out.println("@ wrong link: "+edge);
			}

		}
		// bw.flush();
		return (double) match / size;
	}

	public Double subsumptionRecall() throws IOException {

		int size = goldSubs.size();
		int match = 0;

		for (String edge : goldSubs) {
			if (comparesubs.contains(edge)) {
				match++;
			} else {
				case2.add(edge);
				if (compareeqls.contains(edge)) {
					// bw.write("@ wrong link 2: "+edge+"\r\n"); // missing link
					// because there are no relation in the instance level
					if (!subcase1.containsKey(edge)) {
						subcase1.put(edge, null);
					}
					if (subcase2.containsKey(edge)) {
						System.err.println("@ warning: case1 in case2");
					}
				} else {
					if (!subcase2.containsKey(edge)) {
						subcase2.put(edge, null);
					}
					if (subcase1.containsKey(edge)) {
						System.err.println("@ warning: case2 in case1");
					}
					// bw.write("@ missing link 3: "+edge+"\r\n");
					// System.out.println("@ missing link: "+edge); // missing
					// link because there are no relation in the instance level
				}

				// System.out.println("@ missing link: "+edge);

			}
		}
		// bw.flush();
		return (double) match / size;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

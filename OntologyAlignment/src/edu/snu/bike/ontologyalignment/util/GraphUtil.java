package edu.snu.bike.ontologyalignment.util;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.approches.lsh.HashBean;

public class GraphUtil {

	public static HashSet<String> getSameNodes(SimpleDirectedGraph<String, DefaultEdge> graph, String node) {
		HashSet<String> nodes = new HashSet<String>();

		for (DefaultEdge edge : graph.incomingEdgesOf(node)) {
			String[] vertex = edge.toString().trim().split(" : ");
			String v0 = vertex[0].trim().substring(1).trim();
			if (graph.containsEdge(node, v0)) {
				nodes.add(v0);
			}
		}

		return nodes;
	}

	public static HashSet<String> getSupNodes(SimpleDirectedGraph<String, DefaultEdge> graph, String node) {
		HashSet<String> nodes = new HashSet<String>();

		for (DefaultEdge edge : graph.outgoingEdgesOf(node)) {
			String[] vertex = edge.toString().trim().split(" : ");
			String v1 = vertex[1].trim().substring(0, vertex[1].trim().length() - 1).trim();
			if (!graph.containsEdge(v1, node)) {
				nodes.add(v1);
			}

		}

		return nodes;
	}

	public static boolean isAncestor(SimpleDirectedGraph<String, DefaultEdge> graph, String anc, String dec) {
		boolean ancestor = false;
		if (DijkstraShortestPath.findPathBetween(graph, dec, anc) != null
				&& DijkstraShortestPath.findPathBetween(graph, anc, dec) == null) {
			ancestor = true;
		}
		return ancestor;
	}

	public static void update(SimpleDirectedGraph<String, DefaultEdge> graph, String topNode, HashBean bean,
			boolean transitive) {
		graph.addVertex(topNode);

		HashMap<String, Integer> sups = bean.getSups();
		HashMap<String, Integer> equals = bean.getEquals();

		// System.out.println("======================");
		// System.out.println("node: "+topNode);
		// System.out.println("sups: "+sups.toString());
		// System.out.println("equals: "+equals.toString());
		for (String sup : sups.keySet()) {
			if (graph.containsVertex(sup)) {

				boolean isSup = true;

				for (String other : sups.keySet()) {
					if (!other.equals(sup) & graph.containsVertex(other)) {
						if (isAncestor(graph, sup, other)) {
							isSup = false;
							break;
						}
					}
				}
				// System.err.println(sup +" is the supclass of ");
				if (isSup) {
					// System.out.println(topNode+" is subclasses of "+ sup);
					graph.addEdge(topNode, sup);
					if (transitive) {
						for (String eql : getSameNodes(graph, sup)) {
							graph.addEdge(topNode, eql);
							System.out.println(topNode + " is subclasses of " + eql);
						}
					}
				}
			}
		}

		for (Entry<String, Integer> equal : equals.entrySet()) {
			graph.addEdge(equal.getKey(), topNode);
			graph.addEdge(topNode, equal.getKey());
			// System.err.println(topNode+" is equl with "+ equal.getKey());
			if (transitive) {
				for (String sup : getSupNodes(graph, equal.getKey())) {
					graph.addEdge(topNode, sup);
					System.out.println(topNode + " is subclasses of " + sup);
				}
			}
		}
		// System.out.println("======================");

	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
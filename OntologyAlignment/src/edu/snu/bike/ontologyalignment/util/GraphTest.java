package edu.snu.bike.ontologyalignment.util;

import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraphTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		DefaultDirectedGraph<String, DefaultEdge> graph = new DefaultDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);
		graph.addVertex("c1");
		graph.addVertex("c2");
		graph.addVertex("c3");
		graph.addVertex("c4");
		graph.addVertex("c5");
		graph.addVertex("c6");
		graph.addVertex("c7");
		graph.addVertex("c8");
		graph.addVertex("c9");
		graph.addVertex("c10");

		graph.addEdge("c1", "c2");
		graph.addEdge("c2", "c1");
		graph.addEdge("c7", "c2");
		graph.addEdge("c2", "c8");
		graph.addEdge("c3", "c2");
		graph.addEdge("c5", "c3");
		graph.addEdge("c4", "c2");
		graph.addEdge("c4", "c6");
		graph.addEdge("c9", "c10");

		ConnectivityInspector<String, DefaultEdge> inspector = new ConnectivityInspector<String, DefaultEdge>(graph);
		System.out.println(inspector.pathExists("c2", "c1"));
		System.out.println(inspector.pathExists("c7", "c1"));
		System.out.println(inspector.pathExists("c8", "c1"));
		System.out.println(inspector.pathExists("c5", "c1"));
		System.out.println(inspector.pathExists("c7", "c1"));
		System.out.println(inspector.pathExists("c6", "c1"));
		System.out.println(inspector.pathExists("c9", "c10"));
		System.out.println(inspector.pathExists("c10", "c9"));

		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c2", "c1"));
		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c7", "c1"));
		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c8", "c1"));
		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c5", "c1"));
		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c6", "c1"));
		System.out.println(DijkstraShortestPath.findPathBetween(graph, "c10", "c9"));

	}

}

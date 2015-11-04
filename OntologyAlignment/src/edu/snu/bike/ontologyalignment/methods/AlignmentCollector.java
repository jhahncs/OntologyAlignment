package edu.snu.bike.ontologyalignment.methods;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

public class AlignmentCollector {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public SimpleDirectedGraph<String, DefaultEdge> collect(SimpleDirectedGraph<String, DefaultEdge> graph,
			Config config) {

		SimpleDirectedGraph<String, DefaultEdge> resutls = (SimpleDirectedGraph<String, DefaultEdge>) graph.clone();
		HashSet<DefaultEdge> removes = new HashSet<DefaultEdge>();
		if (config.getType().equals(Config.inter)) {
			for (DefaultEdge edge : resutls.edgeSet()) {
				String[] nodes = edge.toString().split(" : ");
				String node1 = nodes[0].trim().substring(1).trim();
				String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim();

				String nameSpace1 = "";
				String nameSpace2 = "";

				if (node1.startsWith(config.getNamespace1())) {
					nameSpace1 = config.getNamespace1();
				}
				if (node1.startsWith(config.getNamespace2())) {
					nameSpace1 = config.getNamespace2();
				}

				if (node2.startsWith(config.getNamespace1())) {
					nameSpace2 = config.getNamespace1();
				}
				if (node2.startsWith(config.getNamespace2())) {
					nameSpace2 = config.getNamespace2();
				}

				if (nameSpace1.equals(nameSpace2)) {
					removes.add(edge);
				}

			}
		} else {
			for (DefaultEdge edge : resutls.edgeSet()) {
				String[] nodes = edge.toString().split(" : ");
				String node1 = nodes[0].trim().substring(1).trim();
				String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim();
				String nameSpace1 = node1.substring(0, node1.lastIndexOf("/"));
				String nameSpace2 = node2.substring(0, node2.lastIndexOf("/"));

				if (!nameSpace1.equals(nameSpace2)) {
					removes.add(edge);
				}

			}
		}
		// for (DefaultEdge edge : graph.edgeSet()) {
		// System.out.println(edge);
		// }
		resutls.removeAllEdges(removes);
		return resutls;
	}

	public void writeAlignmentToFile(SimpleDirectedGraph<String, DefaultEdge> resutls, Config config, String subfile,
			String eqlfile) throws IOException {

		String namespace1 = config.getNamespace1();
		String namespace2 = config.getNamespace2();

		HashSet<String> compareeqls = new HashSet<String>();
		HashSet<String> comparesubs = new HashSet<String>();
		;
		BufferedWriter bw1 = new BufferedWriter(new FileWriter(new File(subfile)));
		BufferedWriter bw2 = new BufferedWriter(new FileWriter(new File(eqlfile)));
		for (DefaultEdge edge : resutls.edgeSet()) {
			String[] nodes = edge.toString().split(" : ");
			String node1 = nodes[0].trim().substring(1).trim();
			String node2 = nodes[1].trim().substring(0, nodes[1].trim().length() - 1).trim();
			if (!resutls.containsEdge(node2, node1)) {
				comparesubs.add(node1 + " <http://www.w3.org/2000/01/rdf-schema#subClassOf> " + node2);
			} else {
				if (node1.startsWith(namespace1) & node2.startsWith(namespace2)) {
					compareeqls.add(node1 + " <http://www.w3.org/2002/07/owl#equivalentClass> " + node2);
				}
				if (node2.startsWith(namespace1) & node1.startsWith(namespace2)) {
					compareeqls.add(node2 + " <http://www.w3.org/2002/07/owl#equivalentClass> " + node1);
				}
			}

		}

		// <http://www.w3.org/2000/01/rdf-schema#subClassOf>
		// <http://www.w3.org/2002/07/owl#equivalentClass>

		for (String string : comparesubs) {
			bw1.write(string + " .\n");
		}
		bw1.flush();
		for (String string : compareeqls) {
			bw2.write(string + " .\n");
		}
		bw2.flush();

		bw1.close();
		bw2.close();

	}

}
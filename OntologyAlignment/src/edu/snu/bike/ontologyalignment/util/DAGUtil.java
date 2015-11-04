package edu.snu.bike.ontologyalignment.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.jgraph.graph.DefaultEdge;
import org.jgrapht.alg.cycle.JohnsonSimpleCycles;
import org.jgrapht.graph.SimpleDirectedGraph;

public class DAGUtil {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public static void checkDAG(String taxonomy) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(taxonomy)));
		String line = null;
		SimpleDirectedGraph<String, DefaultEdge> graph = new SimpleDirectedGraph<String, DefaultEdge>(
				DefaultEdge.class);
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if (s.equals(o)) {
					System.out.println(line);
				} else {
					graph.addVertex(s);
					graph.addVertex(o);
					graph.addEdge(o, s);
				}
			}
		}
		br.close();
		System.out.println("graph loaded ....");
		JohnsonSimpleCycles<String, DefaultEdge> cycle = new JohnsonSimpleCycles<String, DefaultEdge>(graph);
		for (List<String> list : cycle.findSimpleCycles()) {
			System.out.println(list);
		}

	}

}

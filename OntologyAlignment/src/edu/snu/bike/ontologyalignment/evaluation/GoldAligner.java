package edu.snu.bike.ontologyalignment.evaluation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.InputStream;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class GoldAligner {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	SimpleDirectedGraph<String, DefaultEdge> graph = null;

	public SimpleDirectedGraph<String, DefaultEdge> getGold() {
		return graph;
	}

	public GoldAligner() {
		graph = new SimpleDirectedGraph<String, DefaultEdge>(DefaultEdge.class);
	}

	public SimpleDirectedGraph<String, DefaultEdge> generateGraph(String eqlalignmentfile, String subalignmentfile)
			throws Exception {
		if (eqlalignmentfile != null) {
			BufferedReader br = new BufferedReader(new FileReader(new File(eqlalignmentfile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser(inputStream, false);
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();
					graph.addVertex(s);
					graph.addVertex(o);
					graph.addEdge(s, o);
					graph.addEdge(o, s);
				}
			}
			br.close();
		}

		if (subalignmentfile != null) {
			BufferedReader br = new BufferedReader(new FileReader(new File(subalignmentfile)));
			String line = null;
			while ((line = br.readLine()) != null) {
				InputStream inputStream = new ByteArrayInputStream(line.getBytes());
				NxParser nxp = new NxParser(inputStream, false);
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();
					graph.addVertex(s);
					graph.addVertex(o);
					graph.addEdge(s, o);
				}
			}
			br.close();
		}

		// for(DefaultEdge edge:graph.edgeSet()){
		// System.out.println(edge);
		// }

		return graph;
	}

}

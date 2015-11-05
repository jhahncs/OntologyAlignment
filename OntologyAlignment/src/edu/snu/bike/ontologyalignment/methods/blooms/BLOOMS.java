package edu.snu.bike.ontologyalignment.methods.blooms;

import org.apache.lucene.store.Directory;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.LexiconbasedMapper;
import edu.snu.bike.ontologyalignment.models.data.InputOntologies;

public class BLOOMS implements LexiconbasedMapper{

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}
	Directory typeDirectory;
	Directory articleDirectory;
	Directory taxonomyDirectory;
	
	public BLOOMS (Directory typeDirectory, Directory articleDirectory, Directory taxonomyDirectory){
		this.typeDirectory=typeDirectory;
		this.articleDirectory=articleDirectory;
		this.taxonomyDirectory=taxonomyDirectory;
	}
	
	@Override
	public SimpleDirectedGraph<String, DefaultEdge> mapping(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initial(InputOntologies input, Config config) throws Exception {
		// TODO Auto-generated method stub
		
	}

}

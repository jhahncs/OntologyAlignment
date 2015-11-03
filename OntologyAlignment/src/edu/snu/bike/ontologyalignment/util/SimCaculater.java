package edu.snu.bike.ontologyalignment.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;

public class SimCaculater {

	public static double jaccardSim(Entry<String, HashSet<String>> entry1, Entry<String, HashSet<String>> entry2)
			throws FileNotFoundException {
		double results = 0.0;
		HashSet<String> s1 = entry1.getValue();
		HashSet<String> s2 = entry2.getValue();

		HashSet<String> tmp1 = (HashSet<String>) s1.clone();
		HashSet<String> tmp2 = (HashSet<String>) s1.clone();

		tmp1.addAll(s2);
		tmp2.retainAll(s2);
		int v1 = tmp1.size();
		int v2 = tmp2.size();
		results = (double) v1 / (double) v2;
		// System.out.println("score1: "+results);
		return results;
	}

	public static HashSet<String> strictIntersection(HashSet<String> source, HashSet<String> target) {
		HashSet<String> tmp = null;
		if (source.size() > target.size()) {
			tmp = (HashSet<String>) target.clone();
			tmp.retainAll(source);
		} else {
			tmp = (HashSet<String>) source.clone();
			tmp.retainAll(target);
		}

		return tmp;
	}

	/**
	 * @param args
	 * @throws IOException
	 * @throws NumberFormatException
	 */
	public static void main(String[] args) throws NumberFormatException, IOException {
		// TODO Auto-generated method stub
		// ArrayList<String> predicates= new ArrayList<String>();
		// predicates.add("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>");
		// DBpediaFileIndexerAndSearcher db =new
		// DBpediaFileIndexerAndSearcher();
		// String
		// content1=db.search("E://Datasets/research/yago2/index/dbpedia",
		// "<http://dbpedia.org/resource/Michael_Friendly>", "url");
		// String
		// content2=db.search("E://Datasets/research/yago2/index/dbpedia",
		// "<http://dbpedia.org/resource/Patrick_Huse>", "url");
		// HashMap<String,ArrayList<String>>
		// map1=db.docParser(content1,predicates);
		// HashMap<String,ArrayList<String>>
		// map2=db.docParser(content2,predicates);
		//
		// System.out.println("sim: "+strictJaccardMultiVectors(map1,map2));
		//

	}

}

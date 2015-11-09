package edu.snu.bike.ontologyalignment.data.preprocessing;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;
public class Merger {

	public static void main(String[] args) throws IOException {
		merge("documents/data/input/wikidata-yago/wdyago-type.nt", "documents/data/input/yago2/yagoTypes-refined.nt",
				"documents/data/input/wikidata-yago/class-instance");
	}

	/**
	 * This method merges the wikidata inferred type file and YAGO inferred type
	 * file and writes them in a class\tinstances... format separated by spaces
	 * on a line
	 * 
	 * @param wdType
	 *            - inferred wikidata types .nt
	 * @param yagoType
	 *            - inferred YAGO types .nt
	 * @param clsIns
	 *            - file to write to, entries separated by spaces
	 * 
	 * @throws IOException
	 */
	public static void merge(String wdType, String yagoType, String clsIns) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(wdType)));
		HashMap<String, HashSet<String>> classIns = new HashMap<String, HashSet<String>>();
		String line = null;
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (classIns.containsKey(o)) {
					classIns.get(o).add(s);
				} else {
					HashSet<String> types = new HashSet<String>();
					types.add(s);
					classIns.put(o, types);
				}
			}
		}
		br.close();
		System.out.println("Loaded Wikidata...");

		br = new BufferedReader(new FileReader(new File(yagoType)));
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (classIns.containsKey(o)) {
					classIns.get(o).add(s);
				} else {
					HashSet<String> types = new HashSet<String>();
					types.add(s);
					classIns.put(o, types);
				}
			}
		}
		br.close();
		System.out.println("Loaded YAGO...");

		// ugly hack
		boolean start = true;
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(clsIns)));
		for (String cls : classIns.keySet()) {
			bw.write(cls + "\t");
			for (String ins : classIns.get(cls)) {
				if (start == true) {
					bw.write(ins);
					start = false;
				} else {
					bw.write(" " + ins);
				}
			}
			bw.write("\n");
			start = true;
		}
		bw.flush();
		bw.close();
	}
}

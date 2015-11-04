package edu.snu.bike.ontologyalignment.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map.Entry;

public class TaxonomyUtil {

	public static HashMap<String, HashSet<String>> getAncesters(String cleanedtaxonomy) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(cleanedtaxonomy)));
		String line = null;
		HashMap<String, HashSet<String>> map = new HashMap<String, HashSet<String>>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if (!s.equals(o)) {
					if (map.containsKey(s)) {
						map.get(s).add(o);
					} else {
						HashSet<String> objects = new HashSet<String>();
						objects.add(o);
						map.put(s, objects);
					}
				}
			}
		}
		br.close();

		HashMap<String, HashSet<String>> amap = new HashMap<String, HashSet<String>>();

		for (Entry<String, HashSet<String>> entry : map.entrySet()) {
			HashSet<String> aset = new HashSet<String>();
			LinkedList<String> list = new LinkedList<String>();
			for (String string : entry.getValue()) {
				list.add(string);
			}
			enrichment(map, list, aset);
			amap.put(entry.getKey(), aset);
		}
		map.clear();
		System.gc();
		return amap;
	}

	public static void enrichment(HashMap<String, HashSet<String>> map, LinkedList<String> q, HashSet<String> aset) {
		while (q.size() > 0) {
			String string = q.pop();
			aset.add(string);
			if (map.containsKey(string)) {
				for (String sup : map.get(string)) {
					q.add(sup);
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

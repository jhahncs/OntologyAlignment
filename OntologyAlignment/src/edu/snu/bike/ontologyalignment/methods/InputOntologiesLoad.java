package edu.snu.bike.ontologyalignment.methods;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;

import edu.snu.bike.ontologyalignment.models.data.InputOntologies;

public class InputOntologiesLoad implements TaxonomyLoad, CommonTypesLoad,literalLoad {

	public InputOntologiesLoad(String taxonomyFile1, String taxonomyFile2, String commonTypeInstanceFile)
			throws IOException {
		data = new InputOntologies();

		data.setTaxonomy1(loadTaxonomy(taxonomyFile1));
		data.setTaxonomy2(loadTaxonomy(taxonomyFile2));
		data.setCommonInstances(loadTypes(commonTypeInstanceFile));
	}
	
	public InputOntologiesLoad(String taxonomyFile1, String taxonomyFile2)
			throws Exception {
		data = new InputOntologies();
		data.setTaxonomy1(loadTaxonomy(taxonomyFile1));
		data.setTaxonomy2(loadTaxonomy(taxonomyFile2));
		data.setClassLabels(loadLabels(taxonomyFile1,taxonomyFile2));
		data.setClassDescriptions(loadDescriptions(taxonomyFile1,taxonomyFile2));
	}

	public InputOntologiesLoad(String taxonomyFile1, String taxonomyFile2, String typeFromO1, String typeFromO2)
			throws IOException {
		data = new InputOntologies();

		data.setTaxonomy1(loadTaxonomy(taxonomyFile1));
		data.setTaxonomy2(loadTaxonomy(taxonomyFile2));
		data.setCommonInstances(loadTypes(typeFromO1, typeFromO2));
	}

	private InputOntologies data;

	public InputOntologies getData() {
		return data;
	}

	public void setData(InputOntologies data) {
		this.data = data;
	}

	@Override
	public HashMap<String, HashSet<String>> loadTaxonomy(String taxonomyFile) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, HashSet<String>> t = new HashMap<String, HashSet<String>>();
		BufferedReader br1 = new BufferedReader(new FileReader(new File(taxonomyFile)));
		String line = null;
		while ((line = br1.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				// String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if (t.containsKey(s)) {
					t.get(s).add(o);
				} else {
					HashSet<String> set = new HashSet<String>();
					set.add(o);
					t.put(s, set);
				}
			}

		}
		br1.close();
		return t;
	}

	@Override
	public HashMap<String, HashSet<String>> loadTypes(String commonTypeInstanceFile) throws IOException {
		// TODO Auto-generated method stub

		HashMap<String, HashSet<String>> instances = new HashMap<String, HashSet<String>>();

		BufferedReader br = new BufferedReader(new FileReader(new File(commonTypeInstanceFile)));

		String line = null;

		while ((line = br.readLine()) != null) {

			String[] elements = line.split("	");
			HashSet<String> ins = new HashSet<String>();
			for (String string : elements[1].split(" ")) {
				ins.add(string);
			}
			instances.put(elements[0], ins);
		}
		br.close();
		return instances;
	}

	@Override
	public HashMap<String, HashSet<String>> loadTypes(String typeFromO1, String typeFromO2) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String, HashSet<String>> instances = new HashMap<String, HashSet<String>>();

		BufferedReader br1 = new BufferedReader(new FileReader(new File(typeFromO1)));
		String line = null;
		while ((line = br1.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				// String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if (instances.containsKey(o)) {
					instances.get(o).add(s);
				} else {
					HashSet<String> set = new HashSet<String>();
					set.add(s);
					instances.put(o, set);
				}
			}

		}
		br1.close();

		line = null;
		BufferedReader br2 = new BufferedReader(new FileReader(new File(typeFromO2)));

		while ((line = br2.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				// String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if (instances.containsKey(o)) {
					instances.get(o).add(s);
				} else {
					HashSet<String> set = new HashSet<String>();
					set.add(s);
					instances.put(o, set);
				}
			}

		}
		br2.close();

		return instances;
	}

	@Override
	public HashMap<String, String> loadLabels(String taxonomyfile1,String taxonomyfile2) throws Exception {
		// TODO Auto-generated method stub
		
		HashMap<String, String> labels= new HashMap<String,String>();
		load(labels, taxonomyfile1, "<http://www.w3.org/2000/01/rdf-schema#label>");
		load(labels, taxonomyfile2, "<http://www.w3.org/2000/01/rdf-schema#label>");
		return labels;
	}

	@Override
	public HashMap<String, String> loadDescriptions(String taxonomyfile1,String taxonomyfile2) throws Exception {
		// TODO Auto-generated method stub
		HashMap<String, String> descriptions= new HashMap<String,String>();
		load(descriptions, taxonomyfile1, "<http://www.w3.org/2000/01/rdf-schema#comment>");
		load(descriptions, taxonomyfile2, "<http://www.w3.org/2000/01/rdf-schema#comment>");
		return descriptions;
	}
	
	
	public void load(HashMap<String,String> map, String taxonomyfile, String property){
		BufferedReader br1 = new BufferedReader(new FileReader(new File(taxonomyfile)));
		String line = null;
		while ((line = br1.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] quard = nxp.next();
				String s = quard[0].toN3().trim();
				String p = quard[1].toN3().trim();
				String o = quard[2].toN3().trim();
				if(p.equals(property)&o.contains("@en")){
					String content=o.substring(o.indexOf("\"")+1, o.lastIndexOf("\"@en"));
					map.put(s, content);
				}
			}
		}
		br1.close();
	}
	
	
	

}

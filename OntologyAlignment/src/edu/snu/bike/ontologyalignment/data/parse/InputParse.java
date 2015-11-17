package edu.snu.bike.ontologyalignment.data.parse;

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
import java.util.Map.Entry;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class InputParse implements InputSplit {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	private HashMap<String, HashSet<String>> instanceClass;
	private String sourceFile;
	private String targetFile;
	private String nameSpace1;
	private String nameSpace2;
	
	public InputParse(String sourceFile,String nameSpace1, String targetFile,String nameSpace2) {
		instanceClass = new HashMap<String, HashSet<String>>();
		this.sourceFile=sourceFile;
		this.targetFile=targetFile;
		this.nameSpace1=nameSpace1;
		this.nameSpace2=nameSpace2;
	}

	@Override
	public void split(String sourceTaxonomy, String targetTaxonomy, String classInstance, String sourceClassLabel,
			String targetClassLabel) throws IOException {
		// TODO Auto-generated method stub
		System.out.println("parse input data, and split into 4 parts... ");
		generateTaxonomy(this.sourceFile, sourceTaxonomy);
		generateTaxonomy(this.targetFile, targetTaxonomy);
		getInstanceClass(this.sourceFile, this.targetFile);
		generateCommonClassInstance(classInstance);
		generateClassLabel(this.sourceFile, sourceClassLabel);
		generateClassLabel(this.targetFile, targetClassLabel);
	}

	
	public void generateClassLabel(String file, String classLabel) throws IOException {
		HashSet<String> cls= new HashSet<String>();
		HashMap<String,String> labels= new HashMap<String,String>();
		
		BufferedReader br1 = new BufferedReader(new FileReader(new File(file)));
		String line = null;
		while ((line = br1.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);

			if (line.startsWith("<http:")) {
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();
					if (p.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")&o.equals("<http://www.w3.org/2002/07/owl#Class>")) {
						cls.add(s);
					}
					if(p.equals("<http://www.w3.org/2000/01/rdf-schema#label>")&o.contains("\"@en")){
						labels.put(s, o);
					}
				}
			}
		}
		br1.close();
		
		BufferedWriter bw= new BufferedWriter(new FileWriter(new File(classLabel)));
		for(String string:cls){
			if(labels.containsKey(string)){
				bw.write(string+" <http://www.w3.org/2000/01/rdf-schema#label> "+labels.get(string)+" .\n");
				bw.write(string+" <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://www.w3.org/2002/07/owl#Class> .\n");
			}
		}
		bw.flush();
		bw.close();
	}
	
	
	public void generateTaxonomy(String file, String taxonomy) throws IOException {
		HashMap<String, HashSet<String>> t = new HashMap<String, HashSet<String>>();
		BufferedReader br1 = new BufferedReader(new FileReader(new File(file)));
		String line = null;
		while ((line = br1.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);

			if (line.startsWith("<http:")) {
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();

					if (p.equals("<http://www.w3.org/2000/01/rdf-schema#subClassOf>")) {
						if (t.containsKey(s)) {
							t.get(s).add(o);
						} else {
							HashSet<String> set = new HashSet<String>();
							set.add(o);
							t.put(s, set);
						}
					}
				}
			}
		}
		br1.close();
		
		BufferedWriter bw= new BufferedWriter(new FileWriter(new File(taxonomy)));
		for(Entry<String,HashSet<String>> entry:t.entrySet()){
			for(String string:entry.getValue()){
				bw.write(entry.getKey()+" <http://www.w3.org/2000/01/rdf-schema#subClassOf> "+string+" .\n");
			}
		}
		bw.flush();
		bw.close();
	}

	public void getInstanceClass(String sourceFile, String targetFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(sourceFile)));
		String line = null;
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);

			if (line.startsWith("<http:")) {
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();

					if (p.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")) {
						if (instanceClass.containsKey(s)) {
							instanceClass.get(s).add(o);
						} else {
							HashSet<String> set = new HashSet<String>();
							set.add(o);
							instanceClass.put(s, set);
						}
					}
				}
			}
		}
		br.close();
		line=null;
		br = new BufferedReader(new FileReader(new File(targetFile)));
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);

			if (line.startsWith("<http:")) {
				while (nxp.hasNext()) {
					Node[] quard = nxp.next();
					String s = quard[0].toN3().trim();
					String p = quard[1].toN3().trim();
					String o = quard[2].toN3().trim();

					if (p.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")) {
						if (instanceClass.containsKey(s)) {
							instanceClass.get(s).add(o);
						} else {
							HashSet<String> set = new HashSet<String>();
							set.add(o);
							instanceClass.put(s, set);
						}
					}
				}
			}
		}
		br.close();
	}

	public void generateCommonClassInstance(String commonClassInstance) throws IOException {
		HashMap<String,HashSet<String>> ClassInstace= new HashMap<String,HashSet<String>>(); 
		for(Entry<String,HashSet<String>> entry:this.instanceClass.entrySet()){
			if(differentNameSpace(entry.getValue())){
				for(String string:entry.getValue()){
					if(ClassInstace.containsKey(string)){
						ClassInstace.get(string).add(entry.getKey());
					}else{
						HashSet<String> set= new HashSet<>();
						set.add(entry.getKey());
						ClassInstace.put(string, set);
					}
				}
			}
		}
		instanceClass.clear();
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(commonClassInstance)));
		for(Entry<String,HashSet<String>> entry:ClassInstace.entrySet()){
			StringBuffer sb = new StringBuffer();
			for(String string:entry.getValue()){
				sb.append(string+" ");
				
			}
			bw.write(entry.getKey()+"	"+sb.toString().trim()+" .\n");
		}
		bw.flush();
		bw.close();
		ClassInstace.clear();
	}

	
	public boolean differentNameSpace(HashSet<String> set){
		boolean differntNS=false;
		HashSet<String> ns= new HashSet<String>();
		ns.add(this.nameSpace1);
		ns.add(this.nameSpace2);
		
		for(String string:set){
			if(ns.size()>0){
				for(String nameSpace:ns){
					if(string.startsWith(nameSpace)){
						ns.remove(nameSpace);
						break;
					}
				}
			}else{
				differntNS=true;
			}
		}
		return differntNS;
	}
	
	
}

package edu.snu.bike.ontologyalignment.data.statistics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class Freebase implements Statistics{

	public static void main(String[] args) throws IOException {
		Freebase fb = new Freebase();
//		fb.multiInstantiation("F:/projects/samsung2015/data/Freebase/topics.nt");
		
//		ins size: 112,470,353
//		cls size: 16,671
//		fb.basicInormation("F:/projects/samsung2015/data/Freebase/topics.nt");
		
//		fb.getAllProperties("G:/projects/samsung2015/data/Freebase/original data/freebase-rdf-latest");
		fb.checkProperties("G:/projects/samsung2015/data/Freebase/original data/freebase-rdf-latest");
	}
	
	public static void getAllProperties(String file) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(file)));
		String line=null;
		HashSet<String> dp = new HashSet<String>();
		HashSet<String> op = new HashSet<String>();
		while((line=br.readLine())!=null){
			String[] statements=line.split("	");
			if(statements.length==4){
				if(statements[2].startsWith("\"")){
					dp.add(statements[1]);
				}
				if(statements[2].startsWith("<http:")){
					op.add(statements[1]);
				}
			}
			if(statements.length>4){
				dp.add(statements[1]);
				
			}
			
				
		}
		br.close();
		
		System.out.println("data property size: "+ dp.size());
		System.out.println("object property size: "+ op.size());
		
		writeToFile(dp,"src/main/java/tmp/dp.txt");
		writeToFile(op,"src/main/java/tmp/op.txt");
		
	}
	
	
	
	public  static void writeToFile(HashSet<String> set,String output) throws IOException{
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(output)));
		for(String string:set){
		bw.write(string+"\n");
		}
		bw.flush();
		bw.close();
	}

	@Override
	public void checkProperties(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		HashMap<String,Integer> map= new HashMap<String,Integer>();
		map.put("<http://rdf.freebase.com/ns/type.object.name>",11); // subject: type/topic/property object:""@en
		map.put("<http://www.w3.org/2000/01/rdf-schema#label>",11);  // subject: type/topic/property object:""@en
		map.put("<http://rdf.freebase.com/ns/type.domain.types>",11); //subject: domain object: type
		map.put("<http://rdf.freebase.com/ns/type.type.domain>",11); //subject: type object: domain
		map.put("<http://rdf.freebase.com/ns/type.domain.owners>",11); //subject: domain object: user
		map.put("<http://www.w3.org/2000/01/rdf-schema#domain>",11); //subject:property object:domain
		map.put("<http://rdf.freebase.com/ns/type.type.expected_by>",11); //subject:type object:property
		map.put("<http://rdf.freebase.com/ns/type.type.extends>",11); //subject:type (Rich Media) object:type (Digital Media Asset)
		map.put("<http://rdf.freebase.com/ns/type.property.expected_type>",11); //subject:type object:type
		map.put("<http://rdf.freebase.com/ns/type.object.type>",11); //type/property/topic object:property (a obejct is a type or property ot topic)
		map.put("<http://rdf.freebase.com/ns/type.type.instance>",11); //subject:type object:topic
		map.put("<http://rdf.freebase.com/ns/base.semantics.instance.is_a>",11); //subject:topic object:topic
		map.put("<http://rdf.freebase.com/ns/base.wordnet.synset.instance>",11); //不明确
		map.put("<http://rdf.freebase.com/ns/base.puzzles.puzzle_event_type.instance>",11); //subject:topic object:topic 专业型
		map.put("<http://rdf.freebase.com/ns/base.puzzles.puzzle_form.instance>",11); //subject: topic object:topic 专业型
		map.put("<http://rdf.freebase.com/ns/type.extension.uses_keys>",11); //subject:property object:topic/property
		map.put("<http://rdf.freebase.com/ns/type.extension.example_source>",11); //不能理解
		map.put("<http://rdf.freebase.com/ns/type.domain.owners>",11); //从字面理解
		map.put("<http://rdf.freebase.com/ns/type.extension.uri>",11); //只是外部uri
		map.put("<http://rdf.freebase.com/ns/freebase.type_hints.included_types>", 11); //subject:type object:type
		map.put("<http://rdf.freebase.com/ns/freebase.type_profile.strict_included_types>", 11); //subject:type object:type
		
	
		BufferedReader br = new BufferedReader(new FileReader(new File(ontologyfile)));
		String line=null;
		HashSet<String> triples = new HashSet<String>();
		while((line=br.readLine())!=null){
			if(map.size()>0){
				String[] statements=line.split("	");
				if(map.containsKey(statements[1])){
					int number=map.get(statements[1])-1;
					if(number>0){
						triples.add(line);
						map.remove(statements[1]);
						map.put(statements[1], number);
					}else{
						map.remove(statements[1]);
					}
				}	
			}else{
				break;
			}
			
		}
		br.close();
		writeToFile(triples,"src/main/java/tmp/triples.txt");
	}

	@Override
	public void multiInstantiation(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(ontologyfile)));
		String line=null;
		Map<String, String> topic=new HashMap<String, String>();
		while((line=br.readLine())!=null){
			InputStream   inputStream   =   new   ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream,false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
//				String p = triple[1].toN3().trim();
//				String o = triple[2].toN3().trim();
				String instance = topic.get(s);
				if (instance != null) {
					System.out.println(instance);
					System.out.println(line);
					br.close();
					return;
				} else {
					topic.put(s, line);
				}
			}
		}
		br.close();
	}

	@Override
	public void basicInormation(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(ontologyfile)));
		String line=null;
		HashSet<String> cls = new HashSet<String>();
		HashSet<String> ins = new HashSet<String>();
		while((line=br.readLine())!=null){
			InputStream   inputStream   =   new   ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream,false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
//				String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				ins.add(s);
				cls.add(o);
			}
		}
		br.close();
		
		System.out.println("ins size: "+ ins.size());
		System.out.println("cls size: "+ cls.size());
	}

	
}

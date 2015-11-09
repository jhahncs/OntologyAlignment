package edu.snu.bike.ontologyalignment.evaluation;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class Coverage {

	public String source = "";
	public HashSet<String> sclassset;
	public int sclassno = 0;

	public Coverage(String snamespace, int numberofsclass) {
		this.source = snamespace;
		this.sclassset = new HashSet<String>();
		this.sclassno = numberofsclass;
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		// Coverage c= new Coverage("<http://rdf.freebase.com",
		// "data/freebase/Falcon_freebase_withouttypes-refined.nt");
		// c.getFalconCoverage("data/wikiToFreebase_wihouttypes_alignments.csv");
		//
		// c= new Coverage("<http://www.wikidata.org/entity",
		// "data/Falcon_wikidata_withouttypes-refined1.nt");
		// c.getFalconCoverage("data/wikiToFreebase_wihouttypes_alignments.csv");

		// Coverage c= new Coverage("<http://rdf.freebase.com",8416);
		// c.getIUTCoverage("data/output/freebaseMappings/1.0s0.6e/eql.txt");
		// c= new Coverage("<http://rdf.freebase.com",8416 );
		// c.getIUTCoverage("data/output/freebaseMappings/1.0s0.8e/eql.txt");
		//
		// c= new Coverage("<http://rdf.freebase.com",8975 );
		// c.getIUTCoverage("data/output/samsungMappings/1.0s0.6e/eql.txt");
		// c= new Coverage("<http://rdf.freebase.com",8975 );
		// c.getIUTCoverage("data/output/samsungMappings/1.0s0.8e/eql.txt");

		//
		Coverage c = new Coverage("<http://rdf.freebase.com", 8416);
		c.getIUTCoverage("data/output/freebaseMappings/1.0s0.8e/sub.txt");
		c = new Coverage("<http://rdf.freebase.com", 8975);
		c.getIUTCoverage("data/output/samsungMappings/1.0s0.8e/sub.txt");
		//
		//
		// c= new Coverage("<http://rdf.freebase.com",8416 );
		// c.getIUTCoverage("data/output/freebaseMappings/1.0s0.8e/sub.txt","data/output/freebaseMappings/1.0s0.8e/eql.txt");
		//
		// c= new Coverage("<http://rdf.freebase.com",8975 );
		// c.getIUTCoverage("data/output/samsungMappings/1.0s0.8e/sub.txt","data/output/samsungMappings/1.0s0.8e/eql.txt");
		//
		//
		// c= new Coverage("<http://rdf.freebase.com",8416 );
		// c.getIUTCoverage("data/output/freebaseMappings/1.0s0.6e/sub.txt","data/output/freebaseMappings/1.0s0.6e/eql.txt");
		//
		// c= new Coverage("<http://rdf.freebase.com",8975 );
		// c.getIUTCoverage("data/output/samsungMappings/1.0s0.6e/sub.txt","data/output/samsungMappings/1.0s0.6e/eql.txt");

	}

	public Coverage(String snamespace, String sourceTypeFile) throws IOException {
		this.source = snamespace;
		sclassset = new HashSet<String>();
		BufferedReader br = new BufferedReader(new FileReader(new File(sourceTypeFile)));
		String line = null;
		HashSet<String> set = new HashSet<>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				String predicate = p;
				if (p.equals("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>") & s.startsWith(snamespace)
						& o.equals("<http://www.w3.org/2002/07/owl#Class>")) {
					set.add(s);
				}
			}

		}

		this.sclassno = set.size();
		System.out.println(sclassno);
	}

	public void getFalconCoverage(String csvfile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(csvfile)));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] elements = line.split(",");
			if (elements[1].startsWith(this.source)) {
				sclassset.add(elements[1]);
			} else if (elements[2].startsWith(this.source)) {
				sclassset.add(elements[2]);
			}
		}
		System.out.println(sclassset.size());

		System.out.println("coverage: " + (double) sclassset.size() / sclassno);
	}

	public void getIUTCoverage(String testfile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(testfile)));
		String line = null;
		HashSet<String> lines = new HashSet<>();
		while ((line = br.readLine()) != null) {
			lines.add(line);
			String[] elements = line.split(" ");
			if (elements[1].startsWith(this.source)) {
				sclassset.add(elements[1]);
			} else if (elements[2].startsWith(this.source)) {
				sclassset.add(elements[2]);
			}
		}
		System.out.println(lines.size());
		System.out.println(sclassset.size());
		System.out.println("coverage: " + (double) sclassset.size() / sclassno);
	}

	public void getIUTCoverage(String testfile1, String testfile2) throws IOException {
		BufferedReader br1 = new BufferedReader(new FileReader(new File(testfile1)));
		String line1 = null;
		while ((line1 = br1.readLine()) != null) {
			String[] elements = line1.split(" ");
			if (elements[1].startsWith(this.source)) {
				sclassset.add(elements[1]);
			} else if (elements[2].startsWith(this.source)) {
				sclassset.add(elements[2]);
			}
		}

		BufferedReader br2 = new BufferedReader(new FileReader(new File(testfile2)));
		String line2 = null;
		while ((line2 = br2.readLine()) != null) {
			String[] elements = line2.split(" ");
			if (elements[1].startsWith(this.source)) {
				sclassset.add(elements[1]);
			} else if (elements[2].startsWith(this.source)) {
				sclassset.add(elements[2]);
			}
		}

		System.out.println("coverage: " + (double) sclassset.size() / sclassno);
	}

}

package edu.snu.bike.ontologyalignment.data.statistics;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;

public class Wikidata implements Statistics {
	public static void main(String[] args) throws IOException {
		// ins size: 4,770,153
		// cls size: 16,405
		// typeClass(
		// "F:/projects/ontology-alignment-master/alignment201401/documents/data/input/wikidata/wikidata-type.nt");

		// ins size: 4,770,153
		// cls size: 13,964
		// engWikiTypeClass(
		// "F:/projects/samsung2015/data/Wikidata/original
		// data/wikidata-sitelinks.nt",
		// "F:/projects/samsung2015/data/Wikidata/original
		// data/wikidata-instances.nt");

		// cls size: 189,886
		// engWikiClass(
		// "F:/projects/samsung2015/data/Wikidata/original
		// data/wikidata-sitelinks.nt",
		// "F:/projects/samsung2015/data/Wikidata/original
		// data/wikidata-taxonomy.nt");

		// disambi size: 810,882
		disambiguationPage("F:/projects/samsung2015/data/Wikidata/original data/wikidata-instances.nt");
	}


	public static void engWikiTypeClass(String siteLinks, String typeFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(siteLinks)));
		String line = null;
		HashSet<String> eng = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (s.startsWith("<http://en.wikipedia.org/wiki/") && p.equals("<http://schema.org/about>")) {
					eng.add(o);
				}
			}
		}
		br.close();
		System.out.println("Finished loading sites");

		br = new BufferedReader(new FileReader(new File(typeFile)));
		line = null;
		HashSet<String> ins = new HashSet<String>();
		HashSet<String> cls = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (eng.contains(s)) {
					ins.add(s);
				}
				if (eng.contains(o)) {
					cls.add(o);
				}
			}
		}
		br.close();
		System.out.println("ins size: " + ins.size());
		System.out.println("cls size: " + cls.size());
	}

	public static void engWikiClass(String siteLinks, String taxonomy) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(siteLinks)));
		String line = null;
		HashSet<String> eng = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (s.startsWith("<http://en.wikipedia.org/wiki/") && p.equals("<http://schema.org/about>")) {
					eng.add(o);
				}
			}
		}
		br.close();
		System.out.println("Finished loading sites");

		br = new BufferedReader(new FileReader(new File(taxonomy)));
		line = null;
		HashSet<String> cls = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (eng.contains(s) || eng.contains(o)) {
					cls.add(s);
					cls.add(o);
				}
			}
		}
		br.close();
		System.out.println("cls size: " + cls.size());
	}

	public static void disambiguationPage(String typeFile) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(new File(typeFile)));
		String line = null;
		HashSet<String> disambi = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				if (o.equals("<http://www.wikidata.org/entity/Q4167410>")) {
					disambi.add(s);
				}
			}
		}
		br.close();
		System.out.println("disambi size: " + disambi.size());
	}

	public static void getAnnotationProperty() {

	}

	@Override
	public void checkProperties(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void multiInstantiation(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void basicInormation(String ontologyfile) throws IOException {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new FileReader(new File(ontologyfile)));
		String line = null;
		HashSet<String> ins = new HashSet<String>();
		HashSet<String> cls = new HashSet<String>();
		while ((line = br.readLine()) != null) {
			InputStream inputStream = new ByteArrayInputStream(line.getBytes());
			NxParser nxp = new NxParser(inputStream, false);
			while (nxp.hasNext()) {
				Node[] triple = nxp.next();
				String s = triple[0].toN3().trim();
				// String p = triple[1].toN3().trim();
				String o = triple[2].toN3().trim();
				ins.add(s);
				cls.add(o);
			}
		}
		br.close();

		System.out.println("ins size: " + ins.size());
		System.out.println("cls size: " + cls.size());
	}

}

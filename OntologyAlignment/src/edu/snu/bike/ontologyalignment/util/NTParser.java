package edu.snu.bike.ontologyalignment.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.semanticweb.yars.nx.Node;
import org.semanticweb.yars.nx.parser.NxParser;

public class NTParser {
	/**
	 * this function return triple and context
	 * 
	 * @param line
	 * @return
	 * @throws IOException
	 */
	public ArrayList<String> CTparsing(String line) throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		InputStream inputStream = new ByteArrayInputStream(line.getBytes());
		NxParser nxp = new NxParser(inputStream, false);

		while (nxp.hasNext()) {
			Node[] quard = nxp.next();

			String s = quard[0].toN3().trim();
			String p = quard[1].toN3().trim();
			String o = quard[2].toN3().trim();
			String c = quard[3].toN3().trim();

			ret.add(s);
			ret.add(s + " " + p + " " + o + " .");

			/**
			 * do not compute the String node and blank node
			 */

		}
		return ret;
	}

	public String[] CTparser(String line) throws IOException {
		return CTparsing(line).toArray(new String[0]);
	}

	/**
	 * this function return Star based context if all the statement contain same
	 * subject, this should be in same context.
	 * 
	 * @param line
	 * @return
	 * @throws IOException
	 */

	public ArrayList<String> Starparsing(String line) throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		InputStream inputStream = new ByteArrayInputStream(line.getBytes());
		NxParser nxp = new NxParser(inputStream, false);

		while (nxp.hasNext()) {
			Node[] quard = nxp.next();

			String s = quard[0].toN3().trim();
			String p = quard[1].toN3().trim();
			String o = quard[2].toN3().trim();
			// String c = quard[3].toN3().trim();

			ret.add(s);
			ret.add(s + " " + p + " " + o + " .");

			/**
			 * do not compute the String node and blank node
			 */

		}
		return ret;
	}

	/**
	 * this function return Star based context if all the statement contain same
	 * subject, this should be in same context.
	 * 
	 * @param line
	 * @return
	 * @throws IOException
	 */

	public ArrayList<String> parsing(String line) throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		InputStream inputStream = new ByteArrayInputStream(line.getBytes());
		NxParser nxp = new NxParser(inputStream, false);

		while (nxp.hasNext()) {
			Node[] quard = nxp.next();

			String s = quard[0].toN3().trim();
			String p = quard[1].toN3().trim();
			String o = quard[2].toN3().trim();
			// String c = quard[3].toN3().trim();

			ret.add(s);
			ret.add(p);
			ret.add(o);

			/**
			 * do not compute the String node and blank node
			 */

		}
		return ret;
	}

	public String[] Starparser(String line) throws IOException {
		return Starparsing(line).toArray(new String[0]);
	}

	public String[] parse(String line) throws IOException {
		return parsing(line).toArray(new String[0]);
	}

	public ArrayList<String> Termparsing(String line) throws IOException {
		ArrayList<String> ret = new ArrayList<String>();
		InputStream inputStream = new ByteArrayInputStream(line.getBytes());
		NxParser nxp = new NxParser(inputStream, false);

		while (nxp.hasNext()) {
			Node[] quard = nxp.next();

			String s = quard[0].toN3().trim();
			String p = quard[1].toN3().trim();
			String o = quard[2].toN3().trim();
			// String c = quard[3].toN3().trim();

			ret.add(s);
			ret.add(o + " . ");

			/**
			 * do not compute the String node and blank node
			 */

		}
		return ret;
	}

	public String[] Termparser(String line) throws IOException {
		return Termparsing(line).toArray(new String[0]);
	}

	public static void main(String[] args) throws Exception {

		NTParser parser = new NTParser();
		String string = "http://en.dbpedia.org/resource/Aguascalientes,_Aguascalientes__Historic_Ensemble_of_Aguascalientes_\nCamino_Real_d__1> <http://www.w3.org/1999/02/22-rdf-syntax-ns#type> <http://dbpedia.org/ontology/WorldHeritageSite> .";
		String[] elements = parser.parse(string);
		System.out.println(elements[0] + " " + elements[1] + " " + elements[2]);
	}

}

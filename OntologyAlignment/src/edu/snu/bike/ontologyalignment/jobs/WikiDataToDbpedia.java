package edu.snu.bike.ontologyalignment.jobs;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;

import edu.snu.bike.ontologyalignment.data.parse.InputParse;
import edu.snu.bike.ontologyalignment.data.parse.OutputParse;
import edu.snu.bike.ontologyalignment.methods.AlignmentCollector;
import edu.snu.bike.ontologyalignment.methods.Config;
import edu.snu.bike.ontologyalignment.methods.InstancebasedAlign;
import edu.snu.bike.ontologyalignment.methods.LexiconbasedAlign;

public class WikiDataToDbpedia {
	public static void main(String[] args) throws Exception {
		
		Config config = new Config();
		String nameSpace1="<http://www.semanticweb.org/nansu/ontologies/2015/6/untitled-ontology-23#";
		String nameSpace2="<http://www.semanticweb.org/nansu/ontologies/2015/6/untitled-ontology-24#";
		config.setNamespace1(nameSpace1);
		config.setNamespace2(nameSpace2);
		config.setScale(false);
		config.setType(Config.inter);
		config.setXs(1.0);
		config.setXe(0.6);
		
		String ontology1="data/ontology1.nt";
		String ontology2="data/ontology2.nt";
		
		String sourceTaxonomy="data/tmp/taxonomy1.nt";
		String targetTaxonomy="data/tmp/taxonomy2.nt";
		String classInstance="data/tmp/commonClassInstance.nt";
		String sourceClassLabel="data/tmp/sourceClassLabel1.nt";
		String targetClassLabel ="data/tmp/targetClassLabel2.nt";
		
		String lexiconSub="alignments/lexicon-based/sub.nt";
		String lexiconEql="alignments/lexicon-based/eql.nt";
		String instanceSub=	"alignments/instance-based/sub.nt";
		String instanceEql="alignments/instance-based/eql.txt";
		String sub="alignments/sub.nt";
		String eql="alignments/eql.nt";
		
		/**
		 *  input data split
		 */
		
		InputParse parse1= new InputParse(ontology1, nameSpace1, ontology2, nameSpace2);
		parse1.split(sourceTaxonomy, targetTaxonomy, classInstance, sourceClassLabel, targetClassLabel);
		
		
		/**
		 *  lexicon-based align
		 */
		
		
		LexiconbasedAlign align1  = new LexiconbasedAlign(sourceClassLabel, targetClassLabel, config);
		
		SimpleDirectedGraph<String, DefaultEdge> graph1 = align1.align(align1.getInput(), config);
		
		SimpleDirectedGraph<String, DefaultEdge> results1 = align1.getAlignments(graph1, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector1 = new AlignmentCollector();

		collector1.writeAlignmentToFile(results1, config, lexiconSub,
				lexiconEql);
		
		/**
		 *  instance-based align
		 */
		
		
		InstancebasedAlign align2 = new InstancebasedAlign(sourceTaxonomy,
				targetTaxonomy,
				classInstance, config);

		SimpleDirectedGraph<String, DefaultEdge> graph2 = align2.align(align2.getInput(), config);
		SimpleDirectedGraph<String, DefaultEdge> results2 = align2.getAlignments(graph2, config);

		System.out.println("Collector finished...");
		AlignmentCollector collector2 = new AlignmentCollector();

		collector2.writeAlignmentToFile(results2, config, instanceSub,
				instanceEql);
		
		/**
		 *  output alignment merge
		 */
		
		OutputParse parse2= new OutputParse();
		parse2.merge(lexiconSub, instanceSub, sub);
		parse2.merge(lexiconEql, instanceEql, eql);
		
	}
}

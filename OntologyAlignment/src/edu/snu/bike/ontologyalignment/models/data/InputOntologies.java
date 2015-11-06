package edu.snu.bike.ontologyalignment.models.data;

import java.util.HashMap;
import java.util.HashSet;

public class InputOntologies {

	public String getNameSpace1() {
		return nameSpace1;
	}

	public void setNameSpace1(String nameSpace1) {
		this.nameSpace1 = nameSpace1;
	}

	public String getNameSpace2() {
		return nameSpace2;
	}

	public void setNameSpace2(String nameSpace2) {
		this.nameSpace2 = nameSpace2;
	}

	public HashMap<String, HashSet<String>> getTaxonomy1() {
		return taxonomy1;
	}

	public void setTaxonomy1(HashMap<String, HashSet<String>> taxonomy1) {
		this.taxonomy1 = taxonomy1;
	}

	public HashMap<String, HashSet<String>> getTaxonomy2() {
		return taxonomy2;
	}

	public void setTaxonomy2(HashMap<String, HashSet<String>> taxonomy2) {
		this.taxonomy2 = taxonomy2;
	}

	public HashMap<String, HashSet<String>> getCommonInstances() {
		return commonInstances;
	}

	public void setCommonInstances(HashMap<String, HashSet<String>> commonInstances) {
		this.commonInstances = commonInstances;
	}

	
	public HashMap<String, String> getClassLabels() {
		return classLabels;
	}

	public void setClassLabels(HashMap<String, String> classLabels) {
		this.classLabels = classLabels;
	}

	public HashMap<String, String> getClassDescriptions() {
		return classDescriptions;
	}

	public void setClassDescriptions(HashMap<String, String> classDescriptions) {
		this.classDescriptions = classDescriptions;
	}
	
	public HashSet<String> getClassSet1() {
		return classSet1;
	}

	public void setClassSet1(HashSet<String> classSet1) {
		this.classSet1 = classSet1;
	}

	public HashSet<String> getClassSet2() {
		return classSet2;
	}

	public void setClassSet2(HashSet<String> classSet2) {
		this.classSet2 = classSet2;
	}

	
	private String nameSpace1 = "";
	private String nameSpace2 = "";
	private HashMap<String, HashSet<String>> taxonomy1; // HashMap<subclasses,HashSet<supclasses>>
	private HashMap<String, HashSet<String>> taxonomy2; // HashMap<subclasses,HashSet<supclasses>>
	private HashMap<String, HashSet<String>> commonInstances; // HashMap<classes,HashSet<common instances>>
	private HashMap<String,	String> classLabels;
	private HashMap<String,	String> classDescriptions;
	private HashSet<String> classSet1;
	private HashSet<String> classSet2;
	
}

package edu.snu.bike.ontologyalignment.scaling.lsh;

import java.util.ArrayList;
import java.util.HashMap;

public class HashBean {
	private int searchSpace = 0;

	public int getSearchSpace() {
		return searchSpace;
	}

	public void setSearchSpace(int searchSpace) {
		this.searchSpace = searchSpace;
	}

	private ArrayList<String> mosts = new ArrayList<String>();
	private HashMap<String, Integer> sups = new HashMap<String, Integer>();
	private HashMap<String, Integer> subs = new HashMap<String, Integer>();
	private HashMap<String, Integer> equals = new HashMap<String, Integer>();

	public HashMap<String, Integer> getSups() {
		return sups;
	}

	public void setSups(HashMap<String, Integer> sups) {
		this.sups = sups;
	}

	public HashMap<String, Integer> getSubs() {
		return subs;
	}

	public void setSubs(HashMap<String, Integer> subs) {
		this.subs = subs;
	}

	public ArrayList<String> getMosts() {
		return mosts;
	}

	public HashMap<String, Integer> getEquals() {
		return equals;
	}

	public void setEquals(HashMap<String, Integer> equals) {
		this.equals = equals;
	}

	public void setMosts(ArrayList<String> mosts) {
		this.mosts = mosts;
	}

	public Double getMax() {
		return max;
	}

	public void setMax(Double max) {
		this.max = max;
	}

	private Double max = 0.0;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

package edu.snu.bike.ontologyalignment.scaling.lsh;

import java.util.HashMap;

public class Data {

	HashMap<String, Integer> ins;

	public HashMap<String, Integer> getIns() {
		return ins;
	}

	public void setIns(HashMap<String, Integer> ins) {
		this.ins = ins;
	}

	public HashMap<String, Integer> getCls() {
		return cls;
	}

	public void setCls(HashMap<String, Integer> cls) {
		this.cls = cls;
	}

	public Boolean[][] getData() {
		return data;
	}

	public void setData(Boolean[][] data) {
		this.data = data;
	}

	HashMap<String, Integer> cls;
	Boolean[][] data;

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

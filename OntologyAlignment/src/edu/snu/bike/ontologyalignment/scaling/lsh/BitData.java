package edu.snu.bike.ontologyalignment.scaling.lsh;

import java.util.BitSet;
import java.util.HashMap;

public class BitData {
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

	public HashMap<Integer, BitSet> getData() {
		return data;
	}

	public void setData(HashMap<Integer, BitSet> data) {
		this.data = data;
	}

	HashMap<String, Integer> cls;
	HashMap<Integer, BitSet> data;

}

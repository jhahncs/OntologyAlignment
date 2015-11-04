package edu.snu.bike.ontologyalignment.scaling.lsh;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

public class DataLoader {

	public Data load(HashMap<String, ArrayList<String>> clsIns) {

		Data data = new Data();

		HashMap<String, Integer> clsMap = new HashMap<String, Integer>();
		HashMap<String, Integer> insMap = new HashMap<String, Integer>();

		int clsIdx = 0;
		int insIdx = 0;
		for (Entry<String, ArrayList<String>> entry : clsIns.entrySet()) {
			clsMap.put(entry.getKey(), clsIdx);
			clsIdx++;

			for (String list : entry.getValue()) {
				if (!insMap.containsKey(list)) {
					insMap.put(list, insIdx);
					insIdx++;
				}
			}
		}

		int numIns = insMap.size();
		int numCls = clsMap.size();

		Boolean[][] matrix = new Boolean[numIns][numCls];

		for (int i = 0; i < numIns; i++) {
			for (int j = 0; j < numCls; j++) {
				matrix[i][j] = false;
			}
		}

		for (Entry<String, ArrayList<String>> entry : clsIns.entrySet()) {
			for (String list : entry.getValue()) {
				matrix[insMap.get(list)][clsMap.get(entry.getKey())] = true;
			}
		}

		
		data.setCls(clsMap);
		data.setData(matrix);
		data.setIns(insMap);
		return data;
	}

	public Data load(HashMap<String, ArrayList<String>> clsIns, HashMap<String, Integer> cls,
			HashMap<String, Integer> ins) {

		Data data = new Data();

		int numIns = ins.size();
		int numCls = cls.size();
		System.out.println("instance: " + numIns + " numCls:" + numCls);
		Boolean[][] matrix = new Boolean[numIns][numCls];

		for (int i = 0; i < numIns; i++) {
			for (int j = 0; j < numCls; j++) {
				matrix[i][j] = false;
			}
		}

		for (Entry<String, ArrayList<String>> entry : clsIns.entrySet()) {
			for (String list : entry.getValue()) {
				matrix[ins.get(list)][cls.get(entry.getKey())] = true;
			}
		}

		data.setCls(cls);
		data.setData(matrix);
		data.setIns(ins);
		return data;
	}

	public BitData loadbit(HashMap<String, HashSet<String>> clsIns, HashMap<String, Integer> cls,
			HashMap<String, Integer> ins) {

		int numIns = ins.size();
		int numCls = cls.size();
		System.out.println("instance: " + numIns + " numCls:" + numCls);
		BitData data = new BitData();

		HashMap<Integer, BitSet> bitmap = new HashMap<Integer, BitSet>();

		for (int n = 0; n < numCls; n++) {
			BitSet matrix = new BitSet(numIns);
			bitmap.put(n, matrix);
		}

		for (Entry<String, HashSet<String>> entry : clsIns.entrySet()) {
			for (String list : entry.getValue()) {
				bitmap.get(cls.get(entry.getKey())).set(ins.get(list), true);
			}
		}

		data.setCls(cls);
		data.setData(bitmap);
		data.setIns(ins);
		return data;
	}

	public BitData loadbit(HashMap<String, ArrayList<String>> clsIns) {

		HashMap<String, Integer> cls = new HashMap<String, Integer>();
		HashMap<String, Integer> ins = new HashMap<String, Integer>();

		int clsIdx = 0;
		int insIdx = 0;
		for (Entry<String, ArrayList<String>> entry : clsIns.entrySet()) {
			cls.put(entry.getKey(), clsIdx);
			clsIdx++;

			for (String list : entry.getValue()) {
				if (!ins.containsKey(list)) {
					ins.put(list, insIdx);
					insIdx++;
				}
			}
		}

		int numIns = ins.size();
		int numCls = cls.size();
		System.out.println("instance: " + numIns + " numCls:" + numCls);
		BitData data = new BitData();

		HashMap<Integer, BitSet> bitmap = new HashMap<Integer, BitSet>();

		for (int n = 0; n < numCls; n++) {
			BitSet matrix = new BitSet(numIns);
			bitmap.put(n, matrix);
		}

		for (Entry<String, ArrayList<String>> entry : clsIns.entrySet()) {
			for (String list : entry.getValue()) {
				bitmap.get(cls.get(entry.getKey())).set(ins.get(list), true);
			}
		}

		data.setCls(cls);
		data.setData(bitmap);
		data.setIns(ins);
		return data;
	}

}

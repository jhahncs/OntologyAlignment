package edu.snu.bike.ontologyalignment.approches.lsh;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Random;

public class BitMinHash {
	HashMap<String, Integer> ins;
	HashMap<String, Integer> cls;
	HashMap<Integer, String> clsIdx = new HashMap<Integer, String>();
	HashMap<Integer, BitSet> matrix;
	ArrayList<int[]> sigMatrix;
	ArrayList<HashMap<String, ArrayList<String>>> buckets;
	int row = 0;
	int signature = 0;

	public BitMinHash(BitData data, int numofRow, int numberOfSignature) {
		ins = data.getIns();
		cls = data.getCls();
		matrix = data.getData();
		sigMatrix = new ArrayList<int[]>();
		buckets = new ArrayList<HashMap<String, ArrayList<String>>>(); // band,key,<cls,cls>
		row = numofRow;
		signature = numberOfSignature;

		for (Entry<String, Integer> entry : cls.entrySet()) {
			clsIdx.put(entry.getValue(), entry.getKey());
		}

		// System.out.println("ins size:"+ins.size());
		// System.out.println("cls size:"+cls.size());
	}

	public BitMinHash(BitData data, int numberOfSignature) {
		ins = data.getIns();
		cls = data.getCls();
		matrix = data.getData();
		sigMatrix = new ArrayList<int[]>();
		buckets = new ArrayList<HashMap<String, ArrayList<String>>>(); // band, key, <cls,cls>
		signature = numberOfSignature;

		for (Entry<String, Integer> entry : cls.entrySet()) {
			clsIdx.put(entry.getValue(), entry.getKey());
		}

		// System.out.println("ins size:"+ins.size());
		// System.out.println("cls size:"+cls.size());
	}

	public int[][] hashAt(int signature) {

		Random random = new Random();
		int[][] hashs = new int[ins.size()][signature];
		for (int i = 0; i < signature; i++) {
			int a = random.nextInt();
			int b = random.nextInt();
			int c = random.nextInt();
			for (int j = 0; j < ins.size(); j++) {
				hashs[j][i] = (j * a + b) % c;
			}
		}
		return hashs;

	}

	public ArrayList<int[]> hash() {

		int[][] hash = hashAt(signature);
		long range = Integer.MAX_VALUE;
		for (int i = 0; i < cls.size(); i++) {

			int[] counters = new int[signature];

			for (int c = 0; c < signature; c++) {
				counters[c] = Integer.MAX_VALUE;
			}

			for (int j = 0; j < ins.size(); j++) {
				if (matrix.get(i).get(j)) {
					for (int k = 0; k < signature; k++) {
						if (hash[j][k] < counters[k]) {
							counters[k] = hash[j][k];
						}

					}
				}
			}

			sigMatrix.add(counters);

		}

		return sigMatrix;

	}

	public Double sim(String cls1, String cls2) {

		int[] counters1 = sigMatrix.get(cls.get(cls1));
		int[] counters2 = sigMatrix.get(cls.get(cls2));
		int simNUm = 0;

		for (int i = 0; i < counters1.length; i++) {
			if (counters1[i] == counters2[i]) {
				simNUm++;
			}
		}
		return (double) simNUm / counters1.length;
	}

	public void banding() {

		for (int band = 0; band < Math.ceil((double) signature / row); band++) {
			// System.out.println("band: "+band);
			// System.out.println("================");
			ArrayList<int[]> localsigMatrix = new ArrayList<int[]>();
			for (int[] sigValues : sigMatrix) {
				int rowNum = 0;
				if ((band + 1) * row > signature) {
					rowNum = (signature - band * row);
				} else {
					rowNum = row;
				}
				int[] localsig = new int[rowNum];
				int counter = 0;
				// System.out.println(cls.get(sigMatrix.indexOf(sigValues)));
				for (int i = band * row; (i < (band + 1) * row)
						&& (i < signature); i++) {
					// System.out.print(sigValues[i]+"\t");
					localsig[counter] = sigValues[i];
					counter++;
				}
				localsigMatrix.add(localsig);
				// System.out.println();
			}

			cluster(localsigMatrix);
		}

	}

	public void cluster(ArrayList<int[]> localsigMatrix) {
		
		HashMap<String, ArrayList<String>> bucket = new HashMap<String, ArrayList<String>>(); // bucket for one band value of signature, class

		for (int i = 0; i < localsigMatrix.size(); i++) {

			String key = "";
			for (int value : localsigMatrix.get(i)) {
				key = value + " " + key;
			}
			key.trim();
			// System.out.println(key);
			if (!bucket.containsKey(key)) {
				ArrayList<String> classes = new ArrayList<String>();
				classes.add(clsIdx.get(i));
				bucket.put(key, classes);
			} else {
				bucket.get(key).add(clsIdx.get(i));
			}

		}
		HashMap<String, ArrayList<String>> cleanB = new HashMap<String, ArrayList<String>>(); // elements>1
		for (Entry<String, ArrayList<String>> entry : bucket.entrySet()) {
			if (entry.getValue().size() > 1) {
				cleanB.put(entry.getKey(), entry.getValue());
			}
		}

		buckets.add(cleanB);
		// System.err.println(buckets);
	}

	public boolean isSame(int[] array1, int[] array2) {
		boolean same = true;
		for (int i = 0; i < array2.length; i++) {
			if (array1[i] != array2[i]) {
				same = false;
				break;
			}

		}
		return same;
	}

//	public String queryOR_notGood(String className) {
//
//		int[] sigvalues = sigMatrix.get(cls.get(className));
//		HashMap<String, Integer> candidates = new HashMap<String, Integer>();
//
//		for (int band = 0; band < Math.ceil((double) signature / row); band++) {
//
//			String key = "";
//
//			for (int i = band * row; (i < (band + 1) * row) && (i < signature); i++) {
//				key = sigvalues[i] + " " + key;
//			}
//			key.trim();
//
//			ArrayList<String> cluster = buckets.get(band).get(key);
//			if (cluster != null) {
//				updateByOR(cluster, candidates, className);
//			}
//
//		}
//
//		Double max = 0.0;
//		String best = "";
//		ArrayList<String> cleanCandidates = new ArrayList<String>();
//
//		int freq = 0;
//		for (Entry<String, Integer> entry : candidates.entrySet()) {
//			if (entry.getValue() > freq) {
//				cleanCandidates = new ArrayList<String>();
//				cleanCandidates.add(entry.getKey());
//				freq = entry.getValue();
//			} else if (entry.getValue() == freq) {
//				cleanCandidates.add(entry.getKey());
//			}
//
//		}
//
//		for (String candidate : cleanCandidates) {
//			Double sim = sim(className, candidate);
//			if (sim >= max) {
//				best = candidate;
//			}
//		}
//		return best;
//
//	}

	public Integer getNumberOfSameSig(String cls1, String cls2) {

		int[] sigvalues1 = sigMatrix.get(cls.get(cls1));
		int[] sigvalues2 = sigMatrix.get(cls.get(cls1));
		int num = 0;
		for (int i = 0; i < sigvalues1.length; i++) {
			if (sigvalues1[i] == sigvalues2[i]) {
				num++;
			}
		}

		return num;
	}

	public String queryOR(String className) {
		// System.out.println("**************************");
		// System.out.println(className+" idx: "+cls.indexOf(className));
		int[] sigvalues = sigMatrix.get(cls.get(className));
		HashSet<String> candidates = new HashSet<String>();

		// for(int s:sigvalues){
		// System.out.print(s+"\t");
		// }
		// System.out.println();
		for (int band = 0; band < Math.ceil((double) signature / row); band++) {

			String key = "";

			for (int i = band * row; (i < (band + 1) * row) && (i < signature); i++) {
				key = sigvalues[i] + " " + key;
			}
			key.trim();

			ArrayList<String> cluster = buckets.get(band).get(key);
			if (cluster != null) {
				updateByOR(cluster, candidates, className);
			}
			// System.out.println("band "+band+" "+key.trim()+
			// " "+cluster+" "+candidates);
		}

		Double max = 0.0;
		String best = "";

		for (String candidate : candidates) {
			Double sim = sim(className, candidate);
			if (sim >= max) {
				best = candidate;
			}
		}

		// if(best!=""){
		// System.out.println(className+" "+best+" "+sim(className,best));
		// }

		// System.out.println("**************************");
		return best;

	}

	public HashSet<String> queryORList(String className) {
		int[] sigvalues = sigMatrix.get(cls.get(className));
		HashSet<String> candidates = new HashSet<String>();

		for (int band = 0; band < Math.ceil((double) signature / row); band++) {

			String key = "";

			for (int i = band * row; (i < (band + 1) * row) && (i < signature); i++) {
				key = sigvalues[i] + " " + key;
			}
			key.trim();

			ArrayList<String> cluster = buckets.get(band).get(key);
			if (cluster != null) {
				updateByOR(cluster, candidates, className);
			}
		}

		Double max = 0.0;
		HashSet<String> bests = new HashSet<String>();
		for (String candidate : candidates) {

			Double sim = sim(className, candidate);
			if (sim > max) {
				bests = new HashSet<String>();
				bests.add(candidate);
				max = sim;
			} else if (sim == max) {
				bests.add(candidate);
			}
		}

		return bests;

	}

	public HashSet<String> findCandidates(String className) {
		int[] sigvalues = sigMatrix.get(cls.get(className));
		HashSet<String> candidates = new HashSet<String>();

		for (int band = 0; band < Math.ceil((double) signature / row); band++) {

			String key = "";

			for (int i = band * row; (i < (band + 1) * row) && (i < signature); i++) {
				key = sigvalues[i] + " " + key;
			}
			key.trim();

			ArrayList<String> cluster = buckets.get(band).get(key);
			if (cluster != null) {
				updateByOR(cluster, candidates, className);
			}
		}

		return candidates;

	}

	public String query(String className, ArrayList<String> candidates) {

		Double max = 0.0;
		String best = "";

		for (String candidate : candidates) {
			if (!candidate.trim().equals(className.trim())) {
				Double sim = sim(className, candidate);
				if (sim >= max) {
					best = candidate;
				}
			}

		}
		// System.out.println(className+" "+best+" "+sim(className,best));
		return best;

	}

	public ArrayList<String> queryList(String className,
			ArrayList<String> candidates) {

		Double max = 0.0;
		ArrayList<String> bests = new ArrayList<String>();

		for (String candidate : candidates) {
			if (!candidate.trim().equals(className.trim())) {
				Double sim = sim(className, candidate);
				if (sim > max) {
					bests = new ArrayList<String>();
					bests.add(candidate);
					max = sim;
				} else if (sim == max) {
					bests.add(candidate);
				}
			}

		}

		return bests;

	}

	public void updateByOR(ArrayList<String> source,
			HashMap<String, Integer> target, String query) {
		for (String string : source) {

			if (!query.equals(string)) {
				if (target.containsKey(string)) {
					int freq = target.get(string) + 1;
					target.remove(string);
					target.put(string, freq);
				} else {
					target.put(string, 1);
				}
			}
		}

	}

	public void updateByOR(ArrayList<String> source, HashSet<String> target,
			String query) {
		for (String string : source) {

			if (!query.equals(string)) {
				if (!target.contains(string)) {
					target.add(string);
				}
			}
		}

	}

	public Double accuarrcy(HashMap<String, String> map1,
			HashMap<String, String> map2) {
		int match = 0;
		for (Entry<String, String> entry : map1.entrySet()) {
			if (entry.getValue().trim()
					.equals(map2.get(entry.getKey().trim()).trim())) {
				match++;
			}
		}
		return (double) match / map1.size();
	}

	public static Double check(ArrayList<String> candidates,
			ArrayList<String> gold) {
		int i = 0;
		for (String g : gold) {
			if (candidates.contains(g)) {
				i++;
			}
		}
		return (double) i / gold.size();
	}

	public static boolean equal(ArrayList<String> candidates,
			ArrayList<String> gold) {
		boolean eql = true;

		if (candidates.size() != gold.size()) {
			eql = false;
		} else {
			for (String candidate : candidates) {
				if (!gold.contains(candidate)) {
					eql = false;
					break;
				}
			}
		}

		return eql;
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

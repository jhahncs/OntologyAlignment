package edu.snu.bike.ontologyalignment.methods;

public class Config {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Config() {
		xs = 1.0;
		xe = 0.25;
		scale = false;
		type = inter;
	}

	public Config(Double xe, Double xs, String type, boolean scale, boolean useRam) {
		this.xs = xs;
		this.xe = xe;
		this.scale = scale;
		this.type = type;
	}

	public static String inter = "INTER";
	public static String intra = "INTRA";
	
	private Double xs = 1.0;
	private Double xe = 0.25;
	private boolean scale = false;
	private String type = inter;
	private int numofSig = 50;
	private int numberofRow = 2;
	private String namespace1 = "";
	private String namespace2 = "";
	private boolean transitive = false;
	private boolean useRam = true; 
	
	
	public boolean isUseRam() {
		return useRam;
	}

	public void setUseRam(boolean useRam) {
		this.useRam = useRam;
	}

	public boolean isTransitive() {
		return transitive;
	}

	public void setTransitive(boolean transitive) {
		this.transitive = transitive;
	}

	public String getNamespace1() {
		return namespace1;
	}

	public void setNamespace1(String namespace1) {
		this.namespace1 = namespace1;
	}

	public String getNamespace2() {
		return namespace2;
	}

	public void setNamespace2(String namespace2) {
		this.namespace2 = namespace2;
	}

	public Double getXs() {
		return xs;
	}

	public void setXs(Double xs) {
		this.xs = xs;
	}

	public Double getXe() {
		return xe;
	}

	public void setXe(Double xe) {
		this.xe = xe;
	}

	public boolean isScale() {
		return scale;
	}

	public void setScale(boolean scale) {
		this.scale = scale;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getNumberofRow() {
		return numberofRow;
	}

	public void setNumberofRow(int numberofRow) {
		this.numberofRow = numberofRow;
	}

	public int getNumofSig() {
		return numofSig;
	}

	public void setNumofSig(int numofSig) {
		this.numofSig = numofSig;
	}

}

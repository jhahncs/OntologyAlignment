package edu.snu.bike.ontologyalignment.methods;

public class Config {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

	public Config() {
	}

	public Config(String nameSpace1, String nameSpace2) {
		this.namespace1=nameSpace1;
		this.namespace2=nameSpace2;
	}
	
	
	/**if useRam = true, files are rdf files for building index
	 * if useRam = false, files are built index files 
	 * 
	 * @param useRam
	 * @param knowledgeBaseFile
	 */
	public Config(Boolean useRam, String articleTypeFile,String articleLableFile,String articleDescriptionFile,String referTaxonomy) {
		this.useRam=useRam;
		this.articleTypeFile=articleTypeFile;
		this.articleLableFile=articleLableFile;
		this.articleDescriptionFile=articleDescriptionFile;
		this.referTaxonomy=referTaxonomy;
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
	private String articleTypeFile="";
	private String articleLableFile="";
	private String articleDescriptionFile="";
	private String referTaxonomy="";
	
	
	public String getReferTaxonomy() {
		return referTaxonomy;
	}

	public void setReferTaxonomy(String referTaxonomy) {
		this.referTaxonomy = referTaxonomy;
	}

	public String getArticleTypeFile() {
		return articleTypeFile;
	}

	public void setArticleTypeFile(String articleTypeFile) {
		this.articleTypeFile = articleTypeFile;
	}

	public String getArticleLableFile() {
		return articleLableFile;
	}

	public void setArticleLableFile(String articleLableFile) {
		this.articleLableFile = articleLableFile;
	}

	public String getArticleDescriptionFile() {
		return articleDescriptionFile;
	}

	public void setArticleDescriptionFile(String articleDescriptionFile) {
		this.articleDescriptionFile = articleDescriptionFile;
	}
	

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

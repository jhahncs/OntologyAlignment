package edu.snu.bike.ontologyalignment.methods;

import java.util.HashSet;

public interface ClassLoad {
	public HashSet<String> loadClasses(String taxonomyfile) throws Exception;
}

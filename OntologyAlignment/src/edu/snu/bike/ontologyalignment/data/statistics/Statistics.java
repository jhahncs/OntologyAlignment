package edu.snu.bike.ontologyalignment.data.statistics;

import java.io.IOException;

public interface Statistics {
	public void checkProperties(String ontologyfile) throws IOException;
	public void multiInstantiation(String ontologyfile) throws IOException;
	public void basicInormation(String ontologyfile) throws IOException;
}

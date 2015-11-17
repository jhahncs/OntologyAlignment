package edu.snu.bike.ontologyalignment.data.parse;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;

import javax.annotation.processing.Filer;
import javax.lang.model.element.Element;
import javax.tools.FileObject;
import javax.tools.JavaFileObject;
import javax.tools.JavaFileManager.Location;

public class OutputParse implements OutputMerge {

	@Override
	public void merge(String file1, String file2, String file) throws Exception {
		// TODO Auto-generated method stub
		HashSet<String> alignments1 = new HashSet<String>();
		HashSet<String> alignments2 = new HashSet<String>();
		reafFile(alignments1, file1);
		reafFile(alignments2, file2);
		alignments1.retainAll(alignments2);
		BufferedWriter bw = new BufferedWriter(new FileWriter(new File(file)));
		for(String string:alignments1){
			bw.write(string+"\n");
		}
		bw.flush();
		bw.close();
	}

	
	public void reafFile(HashSet<String> set, String file) throws Exception{
		BufferedReader br =  new BufferedReader(new FileReader(new File(file)));
		String line=null;
		while((line=br.readLine())!=null){
			set.add(line);
		}
		br.close();
	}
	
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}

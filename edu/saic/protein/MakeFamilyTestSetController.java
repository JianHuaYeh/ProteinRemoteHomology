package edu.saic.protein;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.TreeSet;

public class MakeFamilyTestSetController {
	private String seqfile;
	private TreeSet<String> familylist;
	private int ngram;
	
	public static void main(String[] args) {
		String seqf = "";
		String familyl = "";
		int ngram = 3;
		MakeFamilyTestSetController controller = new MakeFamilyTestSetController(seqf, familyl, ngram); 
		controller.go();
	}
	
	public void go() {
		for (String family: this.familylist) {
			MakeFamilyTestSet mfts = new MakeFamilyTestSet(this.seqfile, family, ""+this.ngram);
			mfts.makeTestSet();
		}
	}
	
	public MakeFamilyTestSetController(String s1, String s2, int i) {
		this.seqfile = s1;
		try {
			this.familylist = loadFamilyList(s2);
		} catch (Exception e) {
			e.printStackTrace(System.err);
			System.err.println("Not valid family list.");
			System.exit(0);
		}
		this.ngram = i;
	}
	
	private TreeSet<String> loadFamilyList(String fname) throws Exception {
		BufferedReader br = new BufferedReader(new FileReader(fname));
		String line="";
		TreeSet<String> result = new TreeSet<String>();
		while ((line=br.readLine()) != null) {
			result.add(line.trim());
		}
		br.close();
		return result;
	}

}

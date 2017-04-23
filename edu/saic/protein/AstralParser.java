package edu.saic.protein;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;


public class AstralParser extends Thread {
    private String fname;
    private int count;
    public static void main(String[] args) {
        //AstralParser ap = new AstralParser(args[0]);
    	String fname = "scop.1.53.1e-25.fasta";
    	AstralParser ap = new AstralParser(fname);
        ap.start();
    }
    
    public AstralParser(String s) {
        this.fname = s;
        count = 0;
    }
    
    public void run() {
        try {
        	BufferedReader br = new BufferedReader(new FileReader(fname));
        	String line = "";
        	ArrayList<String> rec = new ArrayList<String>();
        	while ((line=br.readLine()) != null) {
        		if (line.startsWith(">")) { // record start
        			// pass previous record
        			if (rec.size() != 0) {
        				processRecord(rec);
        				rec = new ArrayList<String>();
        			}
        			rec.add(line);
        		}
        		else rec.add(line);
        	}
        	if (rec.size() != 0) {
        		processRecord(rec);
        	}
        	br.close();
        } catch (Exception e) {
            System.err.println("Error: "+e);
        }
        System.err.println("Total "+count+" records processed.");
    }
    
    private void processRecord(ArrayList rec) {
        count++;
        String major_id="", minor_id="", classification="", version="";
        String title="", note="";
        //System.out.println(count);
        if (rec.size() <= 1) {
            System.err.println("Record error, exit.");
            return;
        }
        String header = (String)rec.get(0);
        StringTokenizer st = new StringTokenizer(header);
        String tmp = "";
        // 1: major_id, minor_id
        if (st.hasMoreTokens()) {
            tmp = st.nextToken();
            //System.out.println(tmp);
            // major_id
            major_id = tmp.substring(2, 6);
            minor_id = tmp.substring(6);
            if (minor_id.endsWith("_"))
                minor_id = minor_id.substring(0, minor_id.length()-1);
        }
        // 2: class
        if (st.hasMoreTokens()) {
            classification = st.nextToken();
        }
        // 3: (version)
        if (st.hasMoreTokens()) {
            version = st.nextToken();
        }
        // 4~"{": title
        String tmp2 = "";
        while (st.hasMoreTokens()) {
            tmp = st.nextToken();
            if (tmp.startsWith("{")) {
                break;
            }
            tmp2 += tmp+" ";
        }
        title = tmp2.trim();
        // rest: note
        tmp2 = tmp;
        while (st.hasMoreTokens()) {
            tmp2 += st.nextToken()+" ";
        }
        note = tmp2.trim();
        
        String seq = "";
        for (int i=1; i<rec.size(); i++) {
            seq += (String)rec.get(i);
        }
        
        String result = major_id+" "+minor_id+" "+classification+" "+seq;
        System.err.println(result);
        
        /*
        //System.out.println(major_id+" "+minor_id+" "+classification+" "+version+" ["+title+"] ["+note+"]");
        //String sqlstr = "insert into astralseq63 (major_id, minor_id, classification, version, title, note, sequence) values (";
        String sqlstr = "insert into astralseq63 (major_id, minor_id, classification, sequence) values (";
        sqlstr += "'"+major_id +"',";
        sqlstr += "'"+minor_id +"',";
        sqlstr += "'"+classification +"',";
        //sqlstr += "'"+version +"',";
        //sqlstr += "'"+title +"',";
        //sqlstr += "'"+note +"',";
        sqlstr += "'"+seq+"'";
        sqlstr += ")";
        System.err.println(sqlstr);
        */
    }
}


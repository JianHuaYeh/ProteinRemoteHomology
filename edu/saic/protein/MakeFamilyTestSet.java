package edu.saic.protein;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.*;
import java.util.*;

/**
 *
 * @author jhyeh
 */
public class MakeFamilyTestSet {
    private String fname;
    private String family_ptest;
    private String family_super;
    private String family_fold;
    private int ngram;
    private HashMap negative;
    
    public static void main(String[] args) {
        MakeFamilyTestSet mfds = new MakeFamilyTestSet(args[0], args[1], args[2]);
        mfds.makeTestSet();
    }
    public MakeFamilyTestSet(String str, String str2, String str3) {
        this.fname = str;
        this.family_ptest = str2;
        this.family_super = getSuperFamily(str2);
        this.family_fold = getFold(str2);
        //System.out.println("Family fold: "+this.family_fold);
        this.ngram = Integer.parseInt(str3);
        negative = new HashMap();
    }
    private String getSuperFamily(String str) {
        int idx = str.lastIndexOf(".");
        return str.substring(0, idx);
    }
    private String getFold(String str) {
        int idx = str.indexOf(".");
        return str.substring(0, idx);
    }
    private boolean belongsTo(String cid, String family) {
        // find if cid1 belongs to family
        if (cid.startsWith(family))
            if (cid.charAt(family.length()+1)=='.')
                return true;
        return false;
    }
    public void makeTestSet() {
        try {
            BufferedReader br= new BufferedReader(new FileReader(fname));
            String line="";
            while((line=br.readLine())!= null) {
                //generateFile(line);
                StringTokenizer st = new StringTokenizer(line);
                String major="", minor="", classification="", seq="";
                if (st.hasMoreElements()) major = st.nextToken();
                if (st.hasMoreElements()) minor = st.nextToken();
                if (st.hasMoreElements()) classification = st.nextToken();
                if (st.hasMoreElements()) seq = st.nextToken();
                
                // process classification
                if (!belongsTo(classification, this.family_ptest)) {
                    if (belongsTo(classification, this.family_super)) {
                        // positive training
                        String outfname = "./ptrain/"+classification+"."+major+"."+minor+".seq";
                        System.out.println("Positive training: "+outfname);
                        this.outputFile(outfname, seq);
                    }
                    else if (!belongsTo(classification, this.family_fold)) {
                        // negative training and test here
                        String key = classification+"."+major+"."+minor+".seq";
                        String val = seq;
                        System.out.println("Negative set candidate: "+key);
                        negative.put(key, val);
                    }
                    continue;
                }
                
                String outfname = "./ptest/"+classification+"."+major+"."+minor+".seq";
                System.out.println("Positive test: "+outfname);
                this.outputFile(outfname, seq);
                /*seq = getNgram(seq);
                File f = new File(outfname);
                if (f.exists()) {
                    System.out.println(outfname+" duplicated.");
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(outfname));
                bw.write(seq);
                bw.newLine();
                bw.close();*/
            }
            br.close();
            // deal with negative set
            Set set = negative.keySet();
            int size = set.size();
            int ntest_count = size/4;
            Object[] keys = set.toArray();
            // randomize
            for (int i=0; i<10000; i++) {
                int pos1 = (int)(Math.random()*size);
                int pos2 = (int)(Math.random()*size);
                Object tmp = keys[pos1];
                keys[pos1] = keys[pos2];
                keys[pos2] = tmp; 
            }
            // negative test part
            for (int i=0; i<ntest_count; i++) {
                String fname = (String)keys[i];
                String seq = (String)negative.get(fname);
                this.outputFile("./ntest/"+fname, seq);
            }
            for (int i=ntest_count; i<size; i++) {
                String fname = (String)keys[i];
                String seq = (String)negative.get(fname);
                this.outputFile("./ntrain/"+fname, seq);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void outputFile(String outfname, String seq) throws IOException {
        seq = getNgram(seq);
        //System.out.println("Generating "+major+"."+minor);
        //String outfname = "./ptest/"+this.family_ptest+"."+major+"."+minor+".seq";
        File f = new File(outfname);
        if (f.exists()) {
            System.out.println(outfname+" duplicated.");
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(outfname));
        bw.write(seq);
        bw.newLine();
        bw.close();
    }
    
    private String getNgram(String seq) {
        String result = "";
        for (int i=0; i<seq.length()-ngram+1; i++) {
            result += seq.substring(i, i+ngram)+" ";
        }
        return result.trim();
    }
}

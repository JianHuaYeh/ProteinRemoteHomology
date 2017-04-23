package edu.saic.protein;
/*
 * Ngram4.java
 *
 * Created on 2008�~12��14��, �U�� 6:05
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
import java.io.*;
import java.util.*;
/**
 *
 * @author sean9527
 */
public class Ngram4 {
    private String fname;
    private int ngram;
    /** Creates a new instance of Ngram4 */
    public Ngram4(String str, String str2) {
        fname = str;
        ngram = Integer.parseInt(str2);
    }
    
    public static void main(String[] args) {
        Ngram4 n4 = new Ngram4(args[0], args[1]);
        n4.doNgram();
    }
    
    public void doNgram() {
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
                seq = getNgram(seq);
                //System.out.println("Generating "+major+"."+minor);
                String outfname = major+"."+minor+".txt";
                File f = new File(outfname);
                if (f.exists()) {
                    System.out.println(outfname+" duplicated.");
                }
                BufferedWriter bw = new BufferedWriter(new FileWriter(outfname));
                bw.write(seq);
                bw.newLine();
                bw.close();
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private String getNgram(String seq) {
        String result = "";
        for (int i=0; i<seq.length()-ngram+1; i++) {
            result += seq.substring(i, i+ngram)+" ";
        }
        return result.trim();
    }
    
}

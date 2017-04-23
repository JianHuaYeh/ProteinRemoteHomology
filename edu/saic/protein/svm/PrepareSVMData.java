/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.saic.protein.svm;

import java.io.*;
import java.util.*;
/**
 *
 * @author jhyeh
 */
public class PrepareSVMData {
    private String infname, outfname;
    private String label;
    private int random;

    public static void main(String[] args) {
        PrepareSVMData pd = null;
        if (args.length == 3)
            pd = new PrepareSVMData(args[0], args[1], args[2]);
        else if (args.length == 4) {
            int random = Integer.parseInt(args[3]);
            pd = new PrepareSVMData(args[0], args[1], args[2], random);
        }
        pd.doPrepare();
    }

    public PrepareSVMData(String s1, String s2, String s3) {
        this(s1, s2, s3, -1);
    }

    public PrepareSVMData(String s1, String s2, String s3, int i) {
        this.infname = s1;
        this.outfname = s2;
        this.label = s3;
        this.random = i;
    }

    private int countLines(String infname) {
        int count = 0;
        try {
            BufferedReader br = new BufferedReader(new FileReader(infname));
            while (br.readLine() != null) {
                count++;
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
            count = -1;
        }
        return count;
    }

    private void doPrepareSample() {
        int count = this.countLines(infname);
        int base = (int)(Math.random()*this.random);
        int skip = count/this.random;
        try {
            BufferedReader br = new BufferedReader(new FileReader(infname));
            PrintWriter pw = new PrintWriter(new FileWriter(outfname));
            String line="";
            // skip "base" lines first
            for (int i=0; i<base; i++)
                br.readLine();
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                String result = "";
                if (this.label != null) result += this.label+" ";
                while (st.hasMoreTokens()) {
                    result += st.nextToken()+" ";
                }
                pw.println(result.trim());
                // systematic sampling, skip "count" lines
                for (int i=0; i<skip; i++)
                    br.readLine();
            }
            br.close();
            pw.close();
        } catch (Exception e) {
            // do nothing to avoid annoying output
        }
    }

    public void doPrepare() {
        if (this.random >= 0) { // do systematic sampling
            this.doPrepareSample();
            return;
        }
        try {
            BufferedReader br = new BufferedReader(new FileReader(infname));
            PrintWriter pw = new PrintWriter(new FileWriter(outfname));
            String line="";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                String result = "";
                if (this.label != null) result += this.label+" ";
                while (st.hasMoreTokens()) {
                    result += st.nextToken()+" ";
                }
                pw.println(result.trim());
            }
            br.close();
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

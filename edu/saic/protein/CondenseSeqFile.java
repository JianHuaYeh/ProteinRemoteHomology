/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.saic.protein;

import java.io.*;
import java.util.*;
/**
 *
 * @author jhyeh
 */
public class CondenseSeqFile {
    private TreeSet vocset;
    private String inpath, outpath;

    public static void main(String[] args) {
        CondenseSeqFile csf = new CondenseSeqFile(args[0], args[1], args[2]);
        csf.doCondense();
    }

    public CondenseSeqFile(String str1, String str2, String str3) {
        inpath = str1;
        if (!inpath.endsWith("/")) inpath += "/";
        outpath = str2;
        if (!outpath.endsWith("/")) outpath += "/";
        File opath = new File(outpath);
        opath.mkdirs();
        vocset = loadVoc(str3);
    }

    private TreeSet loadVoc(String fname) {
        TreeSet result = new TreeSet();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line="";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                result.add(st.nextToken());
            }
            br.close();
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doCondense() {
        try {
            File ppath = new File(inpath);
            String[] flist = ppath.list();
            for (int i=0; i<flist.length; i++)
                condenseFile(inpath, flist[i]);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void condenseFile(String inpath, String fname) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(inpath+fname));
            String line="";
            String result = "";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    String term = st.nextToken();
                    if (vocset.contains(term)) {
                        result += term + " ";
                    }
                }
            }
            br.close();
            // begin output
            PrintWriter pw = new PrintWriter(new FileOutputStream(outpath+fname));
            pw.println(result);
            pw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

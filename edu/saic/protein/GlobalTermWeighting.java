/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.saic.protein;

import java.util.*;
import java.io.*;
/**
 *
 * @author jhyeh
 */
public class GlobalTermWeighting {
    private String path;
    private double threshold;
    private HashMap tfmap;
    private HashMap dfmap;
    private int grandTf, grandDf;

    public static void main(String[] args) {
        GlobalTermWeighting ti = new GlobalTermWeighting(args[0], Double.parseDouble(args[1]));
        ti.doCalc();
    }

    public GlobalTermWeighting(String str, double d) {
        this.path = str;
        if (!this.path.endsWith("/")) this.path += "/";
        this.threshold = d;
        this.tfmap = new HashMap();
        this.dfmap = new HashMap();
        this.grandDf = this.grandTf = 0;
    }

    public void doCalc() {
        try {
            File ppath = new File(path);
            String[] flist = ppath.list();
            grandDf = flist.length;
            for (int i=0; i<flist.length; i++) {
                /*if (i%100 == 0) {
                    System.out.println("Document: "+(i/100)*100);
                }*/
                doCount(path+flist[i]);
            }
            for (Iterator it=tfmap.keySet().iterator(); it.hasNext(); ) {
                String term = (String)it.next();
                double tf = ((Integer)tfmap.get(term)).intValue()/(double)grandTf;
                double idf = ((double)grandDf)/((Integer)dfmap.get(term)).intValue();
                idf = lg(idf);
                double tfidf = tf*idf;
                tfidf *= 10000;
                if (tfidf > threshold)
                    System.out.println(term+" "+tfidf);
                    //System.out.println(""+tfidf+", term="+term);
                    //System.out.println("Term: "+term+", tfidf = "+tfidf+"("+tf+","+df+")");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doCount(String fname) {
        try {
            TreeSet ts = new TreeSet();
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line="";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    String term = st.nextToken();
                    // check for "X"
                    if (term.toLowerCase().indexOf("x") >= 0) continue;
                    grandTf++;
                    // process tf
                    Object obj1 = tfmap.get(term);
                    if (obj1 != null) {
                        int freq = ((Integer)obj1).intValue()+1;
                        tfmap.put(term, new Integer(freq));
                    }
                    else {
                        tfmap.put(term, new Integer(1));
                    }
                    // process df by TreeSet
                    ts.add(term);
                }
            }
            for (Iterator it=ts.iterator(); it.hasNext(); ) {
                String term = (String)it.next();
                // process df
                Object obj1 = dfmap.get(term);
                if (obj1 != null) {
                    int freq = ((Integer)obj1).intValue()+1;
                    dfmap.put(term, new Integer(freq));
                }
                else {
                    dfmap.put(term, new Integer(1));
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public double lg(double x) {
        return Math.log(x)/Math.log(2);
    }
}

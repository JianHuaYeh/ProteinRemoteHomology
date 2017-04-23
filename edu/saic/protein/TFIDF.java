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
public class TFIDF {
    private String path;
    private HashMap<String,HashMap> tfidf;
    private HashMap<String,Integer> dfmap;
    private ArrayList<String> termlist;
    private ArrayList<String> doclist;
    private double[][] matrix;

    public class ASObject implements Comparable, java.io.Serializable {
        public String label;
        public double cosine;

        public ASObject(String s, double d) {
            this.label = s;
            this.cosine = d;
        }

        public int compareTo(Object obj) {
            ASObject other = (ASObject)obj;
            if (this.cosine > other.cosine) return -1;
            else if (this.cosine < other.cosine) return 1;
            else return 0;
        }
    }

    public static void main(String[] args) {
        TFIDF p = new TFIDF(args[0]);
        p.doCalc();
    }
    
    public TFIDF() {}

    public TFIDF(String str) {
        this.path = str;
        this.tfidf = new HashMap();
        this.dfmap = new HashMap();
        this.termlist = new ArrayList();
        this.doclist = new ArrayList();
    }

    private HashMap doCount(String fn) throws Exception {
        HashMap<String,Integer> localmap = new HashMap();

        BufferedReader br = new BufferedReader(new FileReader(fn));
        String line="";
        while ((line=br.readLine()) != null) {
            StringTokenizer st = new StringTokenizer(line);
            while (st.hasMoreTokens()) {
                String term = st.nextToken();
                // check for "X"
                if (term.toLowerCase().indexOf("x") >= 0) continue;
                if (termlist.indexOf(term) < 0) termlist.add(term);
                try {
                    int freq = localmap.get(term);
                    localmap.put(term, freq+1);
                } catch (NullPointerException e) {
                    localmap.put(term, 1);
                }
            }            
        }
        br.close();
        if (localmap.size() == 0) return null;
        return localmap;
    }

    private int findDF(String term) {
        Integer ii = dfmap.get(term);
        if (ii != null) return ii;
        // not exists, do calculation
        int count = 0;
        for (Iterator<String> it=tfidf.keySet().iterator();
            it.hasNext(); ) {
            String fn = it.next();
            HashMap<String,Integer> localmap = tfidf.get(fn);
            if (localmap.get(term) != null) count++;
        }
        dfmap.put(term, count);
        return count;
    }

    private int getGTF(HashMap<String,Integer> localmap) {
        int count = 0;
        for (Iterator<String> it=localmap.keySet().iterator();
            it.hasNext(); ) {
            count += localmap.get(it.next());
        }
        return count;
    }

    public void doCalc() {
        try {
            HashMap<String,Integer> tmap = new HashMap();
            
            File dir = new File(this.path);
            String[] flist = dir.list();

            for (int i=0; i<flist.length; i++) {
                String fn = flist[i];
                System.err.println(""+i+":"+fn);
                HashMap localmap = doCount(this.path+fn);
                if (localmap != null) {
                    tfidf.put(fn, localmap);
                    doclist.add(fn);
                }
            }

            // now calculating tf-idf
            int row = flist.length;
            int col = termlist.size();
            System.err.println("TFIDF matrix dimension will be "+
                    row+"x"+col);
            this.matrix = new double[row][col];
            for (int i=0; i<row; i++)
                for (int j=0; j<col; j++)
                    this.matrix[i][j] = 0.0;
            // document-major
            int gdf = tfidf.size();
            for (Iterator<String> it=tfidf.keySet().iterator();it.hasNext();) {
                String fn = it.next();
                int r = doclist.indexOf(fn);
                HashMap<String,Integer> localmap = tfidf.get(fn);
                int gtf = getGTF(localmap);
                for (Iterator<String> it2=localmap.keySet().iterator();
                    it2.hasNext(); ) {
                    String term = it2.next();
                    int tf = localmap.get(term);
                    int df = findDF(term);
                    double ratio = ((double)tf/gtf)*Math.log(gdf/df);
                    int c = termlist.indexOf(term);
                    this.matrix[r][c] = ratio;
                }
            }
            // output tfidf matrix and save to a file
            doOutput();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    private void doOutputFile() {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(
                    new FileOutputStream(System.currentTimeMillis()+".tfidf"));
            oos.writeObject(this.matrix);
            oos.writeObject(this.termlist);
            oos.writeObject(this.doclist);
            oos.flush();
            oos.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public void doLoadData(String fname) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                    new FileInputStream(fname));
            this.matrix = (double[][])ois.readObject();
            this.termlist = (ArrayList)ois.readObject();
            this.doclist = (ArrayList)ois.readObject();
            ois.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    public double getTFIDF(String doc, String term) {
        int r = doclist.indexOf(doc);
        int c = termlist.indexOf(term);
        if (r<0 || c<0) return -1.0;
        return this.matrix[r][c];
    }

    public ArrayList getPhraseList(String doc, double threshold) {
        int r = doclist.indexOf(doc);
        int size = termlist.size();
        ArrayList al = new ArrayList();
        for (int i=0; i<size; i++) {
            double tfidf = this.matrix[r][i];
            if (tfidf < threshold) continue;
            String term = termlist.get(i);
            ASObject obj = new ASObject(term, tfidf);
            al.add(obj);
        }
        Object[] objs = al.toArray();
        java.util.Arrays.sort(objs);
        ArrayList result = new ArrayList();
        for (Object aobj: objs) {
            result.add(((ASObject)aobj).label);
        }
        return result;
    }

    public void doOutput() {
        doOutputFile();
        for (int i=0; i<this.matrix.length; i++) {
            for (int j=0; j<this.matrix[i].length; j++)
                System.out.print(this.matrix[i][j]+",");
            System.out.println();
        }
        System.out.println();
        // output term list
        int count = 1;
        for (Iterator<String> it=termlist.iterator(); it.hasNext(); ) {
            System.out.println(count+","+it.next());
            count++;
        }
        System.out.println();
        // output doc list
        count = 1;
        for (Iterator<String> it=doclist.iterator(); it.hasNext(); ) {
            System.out.println(count+","+it.next());
            count++;
        }
        System.out.println();

    }
}

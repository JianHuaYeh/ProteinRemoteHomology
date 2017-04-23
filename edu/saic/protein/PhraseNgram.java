package edu.saic.protein;

import java.io.*;
import java.util.*;

public class PhraseNgram {
    private String fname;
    private int ngram;
    private String phfname;
    private ArrayList phrases;
    /** Creates a new instance of Ngram4 */
    public PhraseNgram(String str, String str2, String str3) {
        fname = str;
        ngram = Integer.parseInt(str2);
        phfname = str3;
        phrases = loadPhrase(phfname);
    }

    private ArrayList loadPhrase(String fname) {
        ArrayList ph = new ArrayList();
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line = "";
            while ((line=br.readLine()) != null) {
                ph.add(line.trim());
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ph;
    }
    
    public static void main(String[] args) {
        // args: seq file name, n for n-gram, phrase file name
        PhraseNgram n4 = new PhraseNgram(args[0], args[1], args[2]);
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
                String outfname = classification+"."+major+"."+minor+".txt";
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
    
    private String getNgram(String str) {
        String result = "";
        String match = "";
        String remaining = str;

        /*for (Iterator it=phrases.iterator(); it.hasNext(); ) {
            String ph = (String)it.next();
            int idx = seq.indexOf(ph);
            if (idx >= 0) {
                String before = seq.substring(0, idx);
                String after = seq.substring(idx+ph.length());
                // originally we don't do n-gram on pat-tree phrases
                //seq = before + " " + after;
                //result += ph+" ";
                // modified: do n-gram on all kinds of string components
                seq = ph + " " + before + " " + after;
            }
        }*/

        for (Iterator it=phrases.iterator(); it.hasNext(); ) {
            String ph = (String)it.next();
            int idx = 0;
            while (idx >= 0) {
                idx = remaining.indexOf(ph);
                if (idx >= 0) {
                    String before = remaining.substring(0, idx);
                    String after = remaining.substring(idx+ph.length());
                    // originally we don't do n-gram on pat-tree phrases
                    //seq = before + " " + after;
                    //result += ph+" ";
                    // modified: do n-gram on all kinds of string components
                    match += ph+" ";
                    remaining = before + " " + after;
                }
            }
        }

        String seq = match + remaining;
        StringTokenizer st = new StringTokenizer(seq);
        while (st.hasMoreTokens()) {
            String subseq = st.nextToken();
            if (subseq.length() < 3) continue;
            if (subseq.length() == 3) result += subseq+" ";
            else {
                for (int i=0; i<subseq.length()-ngram+1; i++) {
                    result += subseq.substring(i, i+ngram)+" ";
                }
            }
        }
        return result.trim();
    }
    
}

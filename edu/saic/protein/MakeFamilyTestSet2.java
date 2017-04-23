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
public class MakeFamilyTestSet2 {
    private int nset = 54;
    private int nseq = 4352;
    private int[][] membership;
    private String[] idmap;
    private String[] setmap;
    private HashMap seqhash;
    private String seqpath;
    
    public static void main(String[] args) {
        MakeFamilyTestSet2 mfds = new MakeFamilyTestSet2(args[0], args[1]);
        mfds.makeTestSet();
    }

    public MakeFamilyTestSet2(String str, String str2) {
        this.membership = new int[nseq][nset];
        this.idmap = new String[nseq];
        this.setmap = new String[nset];
        System.out.println("Loading membership matrix...");
        loadMembershipMatrix(str);
        System.out.println("Loading sequence file mapping...");
        loadSeqFileMapping(str2);
        this.seqpath = str2;
        if (!this.seqpath.endsWith("/")) this.seqpath += "/";
    }

    private void loadSeqFileMapping(String path) {
        this.seqhash = new HashMap();
        File dir = new File(path);
        String[] flist = dir.list();
        for (int i=0; i<flist.length; i++) {
            String fname = flist[i];
            String id = makeIdByFile(fname);
            System.out.println(fname+" ==> "+id);
            this.seqhash.put(id, fname);
        }
    }

    private String makeIdByFile(String fname) {
        boolean extended = false;
        // "1.5.3.1.1.1aua._1.txt" to "d1aua_1"
        String newfname = fname.replace(".", ", ");
        System.out.println("Making id from newfname = "+newfname+" from "+fname);
        StringTokenizer st = new StringTokenizer(newfname, ",");
        // bypass 5 tokens
        st.nextToken();st.nextToken();st.nextToken();
        st.nextToken();st.nextToken();
        String id = "d";
        id += st.nextToken().trim();
        String tok = st.nextToken();
        if (tok.equals(" ")) {
            id += ".";
            extended = true;
        }
        else
            id += tok.trim();
        String last = st.nextToken();
        if (last != null) {
            last = last.trim();
            if (!"txt".equals(last)) id += last;
        }
        int maxlen = 7;
        if (extended) maxlen = 8;
        if (id.length() < maxlen) {
            for (int i=0; i<maxlen-id.length(); i++) {
                id += "_";
            }
        }
        // final modification
        if (extended) id = "e"+id.substring(1);
        return id;
    }

    private void loadMembershipMatrix(String fname) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fname));
            String line = "";
            // first line: header
            //ID 7.3.5.2 2.56.1.2 3.1.8.1 3.1.8.3 1.27.1.1 ...
            line = br.readLine();
            StringTokenizer st = new StringTokenizer(line);
            st.nextToken(); // bypass "ID"
            int idx = 0;
            while (st.hasMoreTokens()) {
                this.setmap[idx++] = st.nextToken();
            }
            System.out.println("Test set map contains "+idx+" entries.");
            idx = 0;
            while ((line=br.readLine()) != null) {
                StringTokenizer st2 = new StringTokenizer(line);
                this.idmap[idx] = st2.nextToken();
                int idx2 = 0;
                while (st2.hasMoreTokens()) {
                    membership[idx][idx2] = Integer.parseInt(st2.nextToken());
                    idx2++;
                }
                idx++;
            }
            System.out.println("ID map contains "+idx+" entries.");
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void copyFile(String fsrc, String fdst) {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fsrc));
            PrintWriter pw = new PrintWriter(new FileOutputStream(fdst));
            String line = "";
            while ((line=br.readLine()) != null) {
                pw.println(line);
            }
            br.close();
            pw.close();
        } catch (Exception e) {
            //e.printStackTrace();
            System.out.println("Error copying file from "+fsrc+" to "+
                    fdst+"...");
        }
    }

    public void makeTestSet() {
        // for each test set
        for (int i=0; i<this.setmap.length; i++) {
            String setname = "TestSet/"+this.setmap[i];
            String ptestname = setname+"/ptest/";
            File ptestdir = new File(ptestname);
            ptestdir.mkdirs();
            String ptrainname = setname+"/ptrain/";
            File ptraindir = new File(ptrainname);
            ptraindir.mkdirs();
            String ntestname = setname+"/ntest/";
            File ntestdir = new File(ntestname);
            ntestdir.mkdirs();
            String ntrainname = setname+"/ntrain/";
            File ntraindir = new File(ntrainname);
            ntraindir.mkdirs();
            for (int j=0; j<this.idmap.length; j++) {
                String id = this.idmap[j];
                String fname = (String)this.seqhash.get(id);
                String fsrc = this.seqpath+fname;
                String fdst = null;
                int set = this.membership[j][i];
                switch (set) {
                    case 1: // ptrain
                        fdst = ptrainname+fname;
                        break;
                    case 2: // ntrain
                        fdst = ntrainname+fname;
                        break;
                    case 3: // ptest
                        fdst = ptestname+fname;
                        break;
                    case 4: // ntest
                        fdst = ntestname+fname;
                        break;
                    case 0:
                    default:
                        System.out.println("membership("+j+","+j+")="+set+", fsrc="+fsrc);
                        break;
                }
                if (fdst != null) copyFile(fsrc, fdst);
            }
        }
    }
}

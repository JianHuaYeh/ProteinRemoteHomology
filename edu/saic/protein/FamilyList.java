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
public class FamilyList {
    private int level;
    private String fname;
    private TreeSet ts;
    
    public static void main(String[] args) {
        //FamilyList fl = new FamilyList(args[0], args[1]);
    	FamilyList fl = new FamilyList("scop.1.53.seq", "4");
        fl.listFamilies();
    }
    
    public FamilyList(String str, String si) {
        this.fname = str;
        this.level = Integer.parseInt(si);
        ts = new TreeSet();
    }

    private String getClass(String str) {
        String tmp = str;
        for (int i=0; i<5-this.level; i++) {
            int idx = tmp.lastIndexOf(".");
            tmp = tmp.substring(0, idx);
        }
        return tmp;
    }
        
    public void listFamilies() {
        try {
            BufferedReader br= new BufferedReader(new FileReader(fname));
            String line="";
            while((line=br.readLine())!= null) {
                //generateFile(line);
                StringTokenizer st = new StringTokenizer(line);
                String classification="";
                if (st.hasMoreElements()) st.nextToken();
                if (st.hasMoreElements()) st.nextToken();
                if (st.hasMoreElements()) classification = st.nextToken();
                
                String cl = this.getClass(classification);
                ts.add(cl);
            }
            for (Iterator it=ts.iterator(); it.hasNext(); ) {
                System.out.println("# "+it.next());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

}

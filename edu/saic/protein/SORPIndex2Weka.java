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
public class SORPIndex2Weka {
    private HashMap nmap, pmap;

    public static void main(String[] args) {
        // ntrain map & ptrain map
        SORPIndex2Weka weka = new SORPIndex2Weka(args[0], args[1]);
        weka.doOutput();
    }

    public SORPIndex2Weka(String pmapf, String nmapf) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                                     new FileInputStream(pmapf));
            pmap = (HashMap)ois.readObject();
            ois.close();
            ObjectInputStream ois2 = new ObjectInputStream(
                                     new FileInputStream(nmapf));
            nmap = (HashMap)ois2.readObject();
            ois2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doOutput() {
        System.out.println("@relation training");
        System.out.println("@attribute class {1,2}");
        for (int i=0; i<100; i++) {
            System.out.println("@attribute C"+i+" real");
        }
        System.out.println("@data");
        for (Iterator it=pmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])pmap.get(key);
            String result = "1,";
            for (int i=0; i<vec.length; i++) {
                result += vec[i];
                if (i < vec.length-1)
                    result += ",";
            }
            System.out.println(result);
        }
        for (Iterator it=nmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])nmap.get(key);
            String result = "2,";
            for (int i=0; i<vec.length; i++) {
                result += vec[i];
                if (i < vec.length-1)
                    result += ",";
            }
            System.out.println(result);
        }
    }
}

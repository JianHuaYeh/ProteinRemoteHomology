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
public class SORPIndex2NTUSVM {
    private HashMap nmap, pmap;

    public static void main(String[] args) {
        // ntrain map & ptrain map
        SORPIndex2NTUSVM weka = new SORPIndex2NTUSVM(args[0], args[1]);
        weka.doOutput();
    }

    public SORPIndex2NTUSVM(String pmapf, String nmapf) {
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
        for (Iterator it=pmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])pmap.get(key);
            String result = "+1 ";
            for (int i=0; i<vec.length; i++) {
                result += i+":"+vec[i]+" ";
            }
            System.out.println(result.trim());
        }
        for (Iterator it=nmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])nmap.get(key);
            String result = "-1 ";
            for (int i=0; i<vec.length; i++) {
                result += i+":"+vec[i]+" ";
            }
            System.out.println(result.trim());
        }
    }
}

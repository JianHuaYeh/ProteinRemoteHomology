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
public class SORPClassifier {
    private HashMap nmap, pmap, testmap;
    private double[] pvec, nvec;

    public static void main(String[] args) {
        SORPClassifier cl = new SORPClassifier(args[0], args[1], args[2]);
        cl.doClassify();
    }

    public SORPClassifier(String pmapf, String nmapf, String ptestf) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                                     new FileInputStream(pmapf));
            pmap = (HashMap)ois.readObject();
            ois.close();
            ObjectInputStream ois2 = new ObjectInputStream(
                                     new FileInputStream(nmapf));
            nmap = (HashMap)ois2.readObject();
            ois2.close();
            ObjectInputStream ois3 = new ObjectInputStream(
                                     new FileInputStream(ptestf));
            testmap = (HashMap)ois3.readObject();
            ois3.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        pvec = getMapVector(pmap);
        nvec = getMapVector(nmap);
    }

    private double[] getMapVector(HashMap map) {
        double[] result = null;
        for (Iterator it=map.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])map.get(key);
            if (result == null) {
                result = new double[vec.length];
                for (int i=0; i<result.length; i++) result[i] = 0.0;
            }
            result = addVec(result, vec);
        }
        return result;
    }

    private double[] addVec(double[] v1, double[] v2) {
        if ((v1 == null) && (v2 == null)) return null;
        if (v1 == null) return v2;
        if (v2 == null) return v1;
        double[] result = new double[v1.length];
        for (int i=0; i<v1.length; i++) {
            result[i] = v1[i] + v2[i];
        }
        return result;
    }

    public void doClassify() {
        int pcount = 0;
        int total = 0;
        for (Iterator it=testmap.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])testmap.get(key);
            double d1 = cosine(vec, pvec);
            double d2 = cosine(vec, nvec);
            if (d1 > d2) {
                System.out.println(key+" is correctly classified.");
                System.out.println("("+d1+">"+d2+")");
                pcount++;
            }
            else {
                System.out.println(key+" is not correctly classified.");
                System.out.println("("+d1+"<"+d2+")");
            }
            total++;
        }
        System.out.println("Correctness = "+pcount+"/"+total+", rate="+
                (100.0*pcount/total));
    }

    private double cosine(double[] d1, double[] d2) {
        double ip;
        double norm1, norm2;
        norm1 = norm2 = ip = 0.0;
        for (int i=0; i<d1.length; i++) {
            ip += d1[i]*d2[i];
            norm1 += d1[i]*d1[i];
            norm2 += d2[i]*d2[i];
        }
        norm1 = Math.sqrt(norm1);
        norm2 = Math.sqrt(norm2);
        return ip/(norm1*norm2);
    }
}

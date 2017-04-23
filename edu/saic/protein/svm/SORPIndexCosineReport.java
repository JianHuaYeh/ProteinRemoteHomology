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
public class SORPIndexCosineReport {
    private HashMap idx_train, idx_test;
    private String pfamily;

    public static void main(String[] args) {
        SORPIndexCosineReport rep = new SORPIndexCosineReport(args[0], args[1], args[2]);
        rep.doReport();
    }

    public SORPIndexCosineReport(String str, String str2, String str3) {
        this.idx_train = loadFeatureIndex(str);
        this.idx_test = loadFeatureIndex(str2);
        this.pfamily = str3;
    }

    private HashMap loadFeatureIndex(String str) {
        try {
            ObjectInputStream ois = new ObjectInputStream(
                                      new FileInputStream(str));
            return (HashMap)ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void doReport() {
        double psum = 0.0;
        double nsum = 0.0;
        int pcount = 0;
        int ncount = 0;
        for (Iterator it=idx_test.keySet().iterator(); it.hasNext(); ) {
            double[] vec = (double[])idx_test.get(it.next());
            for (Iterator it2=idx_train.keySet().iterator(); it2.hasNext(); ) {
                String name = (String)it2.next();
                double[] vec2 = (double[])idx_train.get(name);
                double cosine = cosine(vec, vec2);
                if (name.startsWith(pfamily)) {
                    psum += cosine;
                    pcount++;
                }
                else {
                    nsum += cosine;
                    ncount++;
                }
            }
        }
        System.out.println("Average positive cosine = "+psum/pcount);
        System.out.println("Average negative cosine = "+nsum/ncount);
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

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
public class SORPIndex2SVM {
    private HashMap index;
    private String pfamily;

    public static void main(String[] args) {
        SORPIndex2SVM sis = new SORPIndex2SVM(args[0], args[1]);
        sis.doRenderSVMData();
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

    public SORPIndex2SVM(String idx, String pfamily) {
        this.index = loadFeatureIndex(idx);
        this.pfamily = pfamily;
    }

    public void doRenderSVMData() {
        String plabel = "+1";
        String nlabel = "-1";
        for (Iterator it=index.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            double[] vec = (double[])index.get(key);
            String outstr = "";
            if (key.startsWith(pfamily)) outstr += plabel+" ";
            else outstr += nlabel+" ";
            for (int i=0; i<vec.length; i++) {
                outstr += ""+i+":"+vec[i]+" ";
            }
            System.out.println(outstr.trim());
        }
    }

}

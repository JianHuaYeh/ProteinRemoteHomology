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
public class SeqPatConversion {
    private String fname;
    private HashMap map;

    public static void main(String[] args) {
        SeqPatConversion spc = new SeqPatConversion(args[1]);
        if (args[0].equals("0")) {
            spc.toPat();
        }
        else {
            spc.toSeq();
        }
    }

    public SeqPatConversion(String fname) {
        this.fname = fname;
    }

    private HashMap loadMapping(String fname, int dir) {
        HashMap result = new HashMap();
        try {
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(
                                  new FileInputStream(fname), "utf-8"));
            String line="";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                if (st.hasMoreTokens()) {
                    String key = st.nextToken();
                    String val = st.nextToken();
                    if (dir == 0)
                        result.put(key, val);
                    else
                        result.put(val, key);
                }
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public void toPat() {
        map = loadMapping("mapping.txt", 0);
        try {
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(
                                  new FileInputStream(fname), "utf-8"));
            BufferedWriter bw = new BufferedWriter(
                                 new OutputStreamWriter(
                                   new FileOutputStream(fname+".out"), "big5"));
            String line="";
            while ((line=br.readLine()) != null) {
                String result = "";
                for (int i=0; i<line.length(); i++) {
                    String key = ""+line.charAt(i);
                    String ch = (String)map.get(key);
                    if (ch != null)
                        result += ch;
                }
                bw.write(result);
                bw.newLine();
            }
            bw.flush();
            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void toSeq() {
        map = loadMapping("mapping.txt", 1);
        try {
            BufferedReader br = new BufferedReader(
                                 new InputStreamReader(
                                  new FileInputStream(fname), "big5"));
            BufferedWriter bw = new BufferedWriter(
                                 new OutputStreamWriter(
                                   new FileOutputStream(fname+".out"), "utf-8"));
            String line="";
            while ((line=br.readLine()) != null) {
                String result = "";
                for (int i=0; i<line.length(); i++) {
                    String key = ""+line.charAt(i);
                    String ch = (String)map.get(key);
                    if (ch != null)
                        result += ch;
                }
                bw.write(result);
                bw.newLine();
            }
            bw.flush();
            br.close();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

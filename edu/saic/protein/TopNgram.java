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
public class TopNgram {
    private HashMap map;
    private String path;
    private String outfname;

    public static void main(String[] args) {
        TopNgram tng = new TopNgram(args[0], args[1]);
        tng.doStatistics();
    }

    public TopNgram(String path, String outfname) {
        this.path = path.endsWith("/")?path:path+"/";
        this.outfname = outfname;
        map = new HashMap();
    }

    private void putInMap(String str) {
        Object obj = map.get(str);
        if (obj == null) {
            map.put(str, new Integer(1));
        }
        else {
            int count = ((Integer)obj).intValue()+1;
            map.put(str, new Integer(count));
        }
    }

    private void doStatistics(String fname) {
        try {
            BufferedReader br = new BufferedReader(
                                  new FileReader(this.path+fname));
            String line;
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                while (st.hasMoreTokens()) {
                    putInMap(st.nextToken());
                }
            }
        } catch (Exception e) {
            System.out.println(fname+" processing error.");
        }
    }

    private void doReport() {
        int max=-1;
        String maxKey="N/A";
        int sum = 0;
        for (Iterator it=map.keySet().iterator(); it.hasNext(); ) {
            String key = (String)it.next();
            int count = ((Integer)map.get(key)).intValue();
            if (count > max) {
                max = count;
                maxKey = key;
            }
            sum += count;
        }
        System.out.println("Maximum pattern = "+maxKey+", count = "+max);
        System.out.println("Average pattern count = "+(double)sum/map.size());
    }

    public void doStatistics() {
        try {
            File dir = new File(this.path);
            String[] flist = dir.list();
            for (int i=0; i<flist.length; i++) {
                if (!flist[i].endsWith(".txt")) continue;
                doStatistics(flist[i]);
            }
            doReport();
            ObjectOutputStream oos = new ObjectOutputStream(
                                       new FileOutputStream(this.outfname));
            oos.writeObject(map);
            oos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

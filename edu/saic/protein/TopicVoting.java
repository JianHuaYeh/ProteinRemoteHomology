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
public class TopicVoting {
    private String fname;
    private double threshold;
    private HashMap map;

    public static void main(String[] args) {
        double threshold = Double.parseDouble(args[1]);
        TopicVoting tv = new TopicVoting(args[0], threshold);
        tv.doVote();
    }

    public TopicVoting(String s, double d) {
        this.fname = s;
        this.threshold = d;
        this.map = new HashMap();
    }

    public void doVote() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(this.fname));
            String line="";
            while ((line=br.readLine()) != null) {
                StringTokenizer st = new StringTokenizer(line);
                st.nextToken();
                double acc = 0.0;
                ArrayList al = new ArrayList();
                ArrayList al2 = new ArrayList();
                ArrayList al3 = null;
                while (st.hasMoreTokens()) {
                    StringTokenizer st2 = new StringTokenizer(st.nextToken(), ":");
                    String topic = st2.nextToken();
                    Double d = new Double(st2.nextToken());
                    if (acc+d.doubleValue() >= this.threshold) {
                        al3 = new ArrayList();
                        for (Iterator it=al2.iterator(); it.hasNext(); ) {
                            double d2 = ((Double)it.next()).doubleValue()/acc;
                            al3.add(new Double(d2));
                        }
                        break;
                    }
                    acc += d.doubleValue();
                    al.add(topic);
                    al2.add(d);
                }
                this.addToHash(al, al3);
            }
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // transform hash to array
        TopicObject[] tolist = new TopicObject[map.size()];
        int count = 0;
        for (Iterator it=map.keySet().iterator(); it.hasNext(); ) {
            String topic = (String)it.next();
            double val = ((Double)map.get(topic)).doubleValue();
            TopicObject to = new TopicObject(topic, val);
            tolist[count++] = to;
        }
        Arrays.sort(tolist);
        for (int i=0; i<tolist.length; i++) {
            System.out.print(tolist[i].topic+":"+tolist[i].vote+" ");
        }
        System.out.println();
    }

    private void addToHash(ArrayList al, ArrayList al3) {
        for (int i=0; i<al.size(); i++) {
            String topic = (String)al.get(i);
            Double d = (Double)al3.get(i);
            Object obj = map.get(topic);
            if (obj == null) {
                map.put(topic, d);
            }
            else {
                double val = ((Double)obj).doubleValue()+d.doubleValue();
                map.put(topic, new Double(val));
            }
        }
    }

}

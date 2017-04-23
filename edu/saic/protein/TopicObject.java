package edu.saic.protein;
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jhyeh
 */
public class TopicObject implements Comparable {
        public String topic;
        public double vote;

        public TopicObject(String s, double d) {
            this.topic = s;
            this.vote = d;
        }

        public int compareTo(Object obj) {
            TopicObject other = (TopicObject)obj;
            if (this.vote < other.vote) return 1;
            else if (this.vote > other.vote) return -1;
            else return 0;
        }

}

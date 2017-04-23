/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package edu.saic.protein.svm;

import java.util.*;
import java.io.*;
//import org.apache.commons.httpclient.*;
//import org.apache.commons.httpclient.methods.*;
//import org.apache.commons.httpclient.methods.multipart.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.client.methods.multipart.*;
/**
 *
 * @author jhyeh
 */
public class GistSubmitter {
    private String trainFile;
    private String classFile;
    private String testFile;
    private String[] cgiFields;
    private Properties props;

    public static void main(String[] args) {
        GistSubmitter gs = new GistSubmitter(args[0], args[1], args[2]);
        gs.doSubmit();
    }

    public GistSubmitter(String str1, String str2, String str3) {
        this.trainFile = str1;
        this.classFile = str2;
        this.testFile = str3;
        this.props = new Properties();
        try {
            // load property file
            this.props.load(new FileInputStream("GistSubmitter.prop"));
        }catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
        String cf = (String)props.get(".cgifields");
        StringTokenizer st = new StringTokenizer(cf, "|");
        int size = st.countTokens();
        this.cgiFields = new String[size];
        int count = 0;
        while (st.hasMoreTokens())
            this.cgiFields[count++] = st.nextToken();
    }

    public void doSubmit() {
        try {
            PostMethod filePost = new PostMethod("http://svm.sdsc.edu/cgi-bin/nph-SVMsubmit.cgi");
            File f1 = new File(this.trainFile);
            File f2 = new File(this.classFile);
            File f3 = new File(this.testFile);

            Part[] parts = new Part[24]; // total 24 parameters required
            int count = 0;
            parts[count++] = new FilePart("trainfile", f1);
            parts[count++] = new FilePart("classfile", f2);
            parts[count++] = new FilePart("testfile", f3);
            for (Iterator it=this.props.keySet().iterator(); it.hasNext(); ) {
                String key = (String)it.next();
                if (key.equals(".cgifields")) continue; // ignore it
                String val = (String)this.props.getProperty(key);
                //System.out.println("String property: ["+key+","+val+"]");
                StringPart sp = new StringPart(key, val);
                parts[count++] = sp;
            }
            // now ".cgifields"
            for (int i=0; i<this.cgiFields.length; i++) {
                StringPart sp = new StringPart(".cgifields", this.cgiFields[i]);
                //System.out.println("String property: [.cgifields,"+
                //        this.cgiFields[i]+"]");
                parts[count++] = sp;
            }
            
            filePost.setRequestEntity(new MultipartRequestEntity(parts, filePost.getParams()));
            HttpClient client = new HttpClient();
            int status = client.executeMethod(filePost);
            //System.out.println("Server response:");
            //System.out.println("--------------------------------------------");
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(filePost.getResponseBodyAsStream()));
            String line = "";
            //while ((line=br.readLine()) != null) {
            while (true) {
                System.out.println(line);
            }
            //br.close();
            //filePost.releaseConnection();
        } catch (Exception e) {
            System.out.println("Fail to submit: "+e);
            e.printStackTrace();
        }

    }
}

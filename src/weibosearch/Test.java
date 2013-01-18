/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package weibosearch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 *
 * @author admin
 */
public class Test {
     public static void main(String[] args) {
         
        String s = "abc";
        int a = 3;
        Object o;
        ArrayList<Object> l = new ArrayList<Object>();
        l.add(s);
        l.add(a);
        l.add(null);
        if((o = l.get(0)) instanceof String){
            System.out.println("It's String");
        }
        if((o = l.get(1)) instanceof Integer){
            int b = (int) o;
            System.out.println("It's Integer");
            System.out.println(b);
        }
        if(l.get(2) == null){
            System.out.println("It's Null");
        }
     }
}

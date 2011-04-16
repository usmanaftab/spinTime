/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timetable.utilz;

import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Utilz {

    public static ArrayList<String> getTokens(String input, String delm) {
        ArrayList<String> results = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(input, delm);
        while (tokenizer.hasMoreTokens()) {
            results.add(tokenizer.nextToken().trim());
        }
        return results;
    }

    public static Object getClass(String className){
        Object object = null;
        try {
            Class cls = Class.forName(className);

            object = cls.newInstance();
        } catch (ClassNotFoundException ex) {
            System.out.println("Class not found.!!");
        } catch (InstantiationException ex) {
            System.out.println("InstantiationException.!!");
        } catch (IllegalAccessException ex) {
            System.out.println("IllegalAccessException.!!");
        }
        return object;
    }
}

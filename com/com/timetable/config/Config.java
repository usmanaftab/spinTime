/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timetable.config;

import com.timetable.utilz.Utilz;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Config {

    public static HashMap<String, String> properties;

    private static void init() {
        properties = new HashMap<String, String>();
        String fileName = "config.txt";
        try {
            LineNumberReader reader = new LineNumberReader(new FileReader(new File(fileName)));
            String input = null;
            for (int index = 0; ((input = reader.readLine()) != null); index++) {
                ArrayList<String> tokens = Utilz.getTokens(input, "=");

                if (tokens.size() == 0) {
                    continue;
                } else if (tokens.size() == 1) {
                    properties.put(tokens.get(0), null);
                } else {
                    properties.put(tokens.get(0), tokens.get(1));
                }
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Could not open config file.");
        } catch (IOException ex) {
            System.out.println("Could not read properties.");
        }


    }

    public static String getProperty(String propertyName) {
        if (properties == null) {
            init();
        }

        return properties.get(propertyName);
    }

    public static int getPropertyInt(String propertyName) {
        return Integer.parseInt(getProperty(propertyName));
    }

    public static float getPropertyFloat(String propertyName) {
        return Float.parseFloat(getProperty(propertyName));
    }

    public static double getPropertyDouble(String propertyName) {
        return Double.parseDouble(getProperty(propertyName));
    }

    public static boolean getPropertyBoolean(String propertyName) {
        return Boolean.parseBoolean(getProperty(propertyName));
    }
}

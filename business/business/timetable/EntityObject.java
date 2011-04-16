/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class EntityObject<T> implements Comparable<T> {

    private HashMap<String, String> properties;
    private HashMap<String, HashMap<Integer, Integer>> multiValueProperties;

    public EntityObject(HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValueProperties) {
        this.properties = properties;
        this.multiValueProperties = new HashMap<String, HashMap<Integer, Integer>>();
        for(String key: multiValueProperties.keySet()){
            ArrayList<Integer> list = multiValueProperties.get(key);
            this.multiValueProperties.put(key, getHashMapList(list));
        }
    }

    public String getProperty(String propertyName){
        return properties.get(propertyName);
    }

    public void setProperty(String propertyName, String str){
        properties.put(propertyName, str);
    }

    public HashMap<Integer, Integer> getMultiValueProperty(String propertyName){
        return multiValueProperties.get(propertyName);
    }

    public void setMultiValueProperty(String propertyName, HashMap<Integer, Integer> list){
        multiValueProperties.put(propertyName, list);
    }

    protected HashMap<Integer, Integer> getHashMapList(ArrayList<Integer> list){
        HashMap<Integer, Integer> hashMapList = new HashMap<Integer, Integer>();
        for(Integer i : list){
            hashMapList.put(i, i);
        }
        return hashMapList;
    }
}

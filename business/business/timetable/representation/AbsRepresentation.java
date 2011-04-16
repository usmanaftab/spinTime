/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

import business.timetable.*;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class AbsRepresentation<K1Type, K1 extends Keyable<K1Type>, K2Type, K2 extends Keyable<K2Type>, VType, V extends Valueable<VType>> {

    protected HashMap<K1Type, HashMap<K2Type, V>> hashMap;
    protected Hashmap<K1Type, K1> keys1;
    protected Hashmap<K2Type, K2> keys2;
    protected HashMap<VType, Keys<K1, K2>> keys;

    public AbsRepresentation(Hashmap<K1Type, K1> keys1, Hashmap<K2Type, K2> keys2) {
        hashMap = new HashMap<K1Type, HashMap<K2Type, V>>();
        this.keys1 = keys1;
        this.keys2 = keys2;
        keys = new HashMap<VType, Keys<K1, K2>>();
    }

    public Hashmap<K1Type, K1> getKeys1() {
        return keys1;
    }

    public Hashmap<K2Type, K2> getKeys2() {
        return keys2;
    }

    public void put(K1 key1, K2 key2, V value) {

        if(key1 == null || key2 == null){
            throw new IncompatibleKeysException();
        }

        HashMap<K2Type, V> map2 = hashMap.get(key1.getID());
        if (map2 == null) {
            map2 = new HashMap<K2Type, V>();
            hashMap.put(key1.getID(), map2);
        }

        map2.put(key2.getID(), value);
        if (value != null) {
            keys.put(value.getID(), new Keys<K1, K2>(key1, key2));
        }
    }

    public V get(K1 key1, K2 key2) {
        HashMap<K2Type, V> map2 = hashMap.get(key1.getID());
        if (map2 == null) {
            return null;
        }
        return map2.get(key2.getID());
    }

    public Keys<K1, K2> getKeys(V value) {
        if (value != null) {
            return keys.get(value.getID());
        }
        return null;
    }
}

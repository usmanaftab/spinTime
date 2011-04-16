/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class Hashmap<K, V> extends HashMap<K, V> implements IHashmap<K, V>, Iterable<V> {

    public K[] getAllKeys() {
        return (K[]) keySet().toArray();
    }

    public ArrayList<V> getAllValues() {
        return new ArrayList<V>(values());
    }

    public K getKeyAt(int index) {
        return getAllKeys()[index];
    }

    public V getValueAt(int index) {
        return get(getKeyAt(index));
    }

    public Iterator<V> iterator() {
        return values().iterator();
    }

    public List<V> getList() {
        return getAllValues();
    }
}

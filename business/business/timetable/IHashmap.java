/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface IHashmap<K, V> {

    public K[] getAllKeys();

    public ArrayList<V> getAllValues();

    public K getKeyAt(int index);

    public V getValueAt(int index);

    public List<V> getList();

    public Iterator<V> iterator();
}

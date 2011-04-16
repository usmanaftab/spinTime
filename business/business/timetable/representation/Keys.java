/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Keys<K1, K2> {

    K1 key1;
    K2 key2;

    public Keys(K1 key1, K2 key2) {
        this.key1 = key1;
        this.key2 = key2;
    }

    public K1 getK1() {
        return key1;
    }

    public void setK1(K1 k1) {
        this.key1 = k1;
    }

    public K2 getK2() {
        return key2;
    }

    public void setK2(K2 k2) {
        this.key2 = k2;
    }
}

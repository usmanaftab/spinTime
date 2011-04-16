/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.constraints.gap;

import java.util.Comparator;

/**
 *
 * @author Usman Aftab (08-0964)
 */
class DayComparator implements Comparator<Object> {

    public DayComparator() {
    }

    public int compare(Object o1, Object o2) {
        Integer i1 = (Integer) o1;
        Integer i2 = (Integer) o2;

        return i1.compareTo(i2);
    }

}

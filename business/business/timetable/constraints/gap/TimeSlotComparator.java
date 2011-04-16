/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.timeslots.*;
import java.util.Comparator;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlotComparator implements Comparator<Object> {

    public int compare(Object o1, Object o2) {
        Time t1 = new Time((String) o1);
        Time t2 = new Time((String) o2);
        return t1.compareTo(t2);
    }
}

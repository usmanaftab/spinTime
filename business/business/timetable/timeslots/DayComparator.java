/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.timeslots;

import java.util.Comparator;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class DayComparator implements Comparator<String> {

    public Integer compare(Day day1, Day day2) {
        Integer d1 = day1.getDayNo();
        Integer d2 = day2.getDayNo();

        return d1.compareTo(d2);
    }

    public int compare(String o1, String o2) {
        Integer d1 = Day.valueOf(o1).getDayNo();
        Integer d2 = Day.valueOf(o2).getDayNo();

        return d1.compareTo(d2);
    }

}

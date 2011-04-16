/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.timeslots.TimeSlot;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class DayGap extends AbsGap {

    @Override
    void setTimeSlotEnteries(ArrayList<TimeSlot> timeSlots) {
        for (TimeSlot timeSlot : timeSlots) {
            addTimeSlot(1, timeSlot.getDay().getDayNo(), timeSlot);
        }
    }

    @Override
    int getGap(int row, int col1, int col2) {
        Integer day1 = (Integer) getSecondIndexMapping(row, col1);
        Integer day2 = (Integer) getSecondIndexMapping(row, col2);

        return day2 - day1 - 1;
    }

    @Override
    Comparator getComparator() {
        return new DayComparator();
    }
}

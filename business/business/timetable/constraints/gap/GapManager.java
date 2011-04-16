/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.constraints.TimeSlotType;
import business.timetable.timeslots.TimeSlot;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapManager {

    private AbsGap gap;

    public GapManager(TimeSlotType timeSlotType) {
        switch (timeSlotType) {
            case TimeSlot:
                gap = new TimeSlotGap();
                break;
            case Day:
                gap = new DayGap();
                break;

            case Week:
                break;
        }
    }

    public void setTimeSlotEnteries(ArrayList<TimeSlot> timeSlots) {
        gap.reset();
        gap.setTimeSlotEnteries(timeSlots);
        gap.sortKeys();
//        gap.printKeys();
    }

    public int rowSize() {
        return gap.timeSlotEnteriesSize();
    }

    public int colSize(int row) {
        return gap.getColSize(row);
    }

    public int getGap(int row, int col1, int col2) {
        return gap.getGap(row, col1, col2);
    }

    public ArrayList<TimeSlot> getTimeSlotsAt(int row, int col) {
        return gap.timeSlotsAt(row, col);
    }
}

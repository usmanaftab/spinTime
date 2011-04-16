/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.timeslots.Time;
import business.timetable.timeslots.TimeSlot;
import com.timetable.config.Config;
import java.util.ArrayList;
import java.util.Comparator;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlotGap extends AbsGap {

    private static long diffBtwTimeSlots;
    private static long minTimeSlotDuration;
    private static long gapBtwTimeSlots;

    @Override
    void setTimeSlotEnteries(ArrayList<TimeSlot> timeSlots) {
        for (TimeSlot timeSlot : timeSlots) {
            addTimeSlot(timeSlot.getDay().getDayNo(), timeSlot.getBeginTime().toString(), timeSlot);
        }
    }

    @Override
    int getGap(int row, int col1, int col2) {
        if (col1 > col2) {
            int temp = col1;
            col1 = col2;
            col2 = temp;
        }

        ArrayList<TimeSlot> timeSlots1 = timeSlotsAt(row, col1);
        ArrayList<TimeSlot> timeSlots2 = timeSlotsAt(row, col2);
        Time maxEndTime = getMaxEndTime(timeSlots1);
        Time beginTime = timeSlots2.get(0).getBeginTime();
        if (beginTime.compareTo(maxEndTime) == -1) {
            return 0;
        }

        return getDifference(maxEndTime, beginTime);
    }

    @Override
    Comparator getComparator() {
        return new TimeSlotComparator();
    }

    private int getDifference(Time beginTime, Time endTime) {
        if (diffBtwTimeSlots == 0) {
            Time t = new Time(Config.getProperty("business.timetable.constraints.gap.GapManager.minTimeSlotDuration"));
            minTimeSlotDuration = t.getLongTime();
            t = new Time(Config.getProperty("business.timetable.constraints.gap.GapManager.gapBtwTimeSlots"));
            gapBtwTimeSlots = t.getLongTime();

            diffBtwTimeSlots = minTimeSlotDuration + gapBtwTimeSlots;
        }

        long diff = endTime.getLongTime() - (beginTime.getLongTime() + gapBtwTimeSlots);
        return (int) (diff / diffBtwTimeSlots);
    }

    private Time getMaxEndTime(ArrayList<TimeSlot> timeslots) {
        Time maxEndTime = timeslots.get(0).getEndTime();
        for (TimeSlot timeSlot : timeslots) {
            if (maxEndTime.compareTo(timeSlot.getEndTime()) == -1) {
                maxEndTime = timeSlot.getEndTime();
            }
        }
        return maxEndTime;
    }
}

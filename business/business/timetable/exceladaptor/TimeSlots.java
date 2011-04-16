/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.exceladaptor;

import business.timetable.timeslots.Day;
import business.timetable.timeslots.DayComparator;
import business.timetable.timeslots.TimeSlot;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlots {

    private ArrayList<TimeSlot> timeSlots;
    private ArrayList<String> map;
    private ArrayList<String> days;

    public TimeSlots(ArrayList<TimeSlot> timeSlots) {
        map = new ArrayList<String>();
        days = new ArrayList<String>();
        this.timeSlots = timeSlots;

        for (TimeSlot timeSlot : timeSlots) {
            String timeString = getTimeString(timeSlot);
            if (!map.contains(timeString)) {
                map.add(timeString);
            }

            String dayString = timeSlot.getDay().toString();
            if (!days.contains(dayString)) {
                days.add(dayString);
            }
        }

        Collections.sort(map);
        Collections.sort(days, new DayComparator());
    }

    int getCol(TimeSlot timeSlot) {
        return map.indexOf(getTimeString(timeSlot));
    }

    public ArrayList<String> getDays() {
        return days;
    }

    public ArrayList<String> getList() {
        return map;
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return timeSlots;
    }

    int getDay(Day day) {
        return days.indexOf(day.toString());
    }

    private String getTimeString(TimeSlot timeSlot){
        return timeSlot.getBeginTime().toString() + " - " + timeSlot.getEndTime().toString();
    }
}

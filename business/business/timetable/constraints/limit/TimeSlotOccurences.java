/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.limit;

import business.timetable.constraints.TimeSlotType;
import business.timetable.timeslots.TimeSlot;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlotOccurences {

    private HashMap<Integer, ArrayList<TimeSlot>> timeSlotOccurences;
    private ArrayList<Integer> indexMap;
    private TimeSlotType timeSlotType;

    public TimeSlotOccurences(TimeSlotType timeSlotType) {
        this.timeSlotType = timeSlotType;
    }

    private void reset() {
        timeSlotOccurences = new HashMap<Integer, ArrayList<TimeSlot>>();
        indexMap = new ArrayList<Integer>();
    }

    public void setTimeSlotOccurences(ArrayList<TimeSlot> timeSlots) {
        reset();

        switch (timeSlotType) {
            case TimeSlot:
                for (int i = 0; i < timeSlots.size(); i++) {
                    TimeSlot timeSlot1 = timeSlots.get(i);
                    addTimeSlot(i, timeSlot1);
                    for (int j = 0; j < timeSlots.size(); j++) {
                        if(i == j){
                            continue;
                        }

                        TimeSlot timeSlot2 = timeSlots.get(j);
                        if(timeSlot1 == timeSlot2 || timeSlot1.isOverlapping(timeSlot2)){
                            addTimeSlot(i, timeSlot2);
                        }
                    }
                }
                break;

            case Day:
                for (TimeSlot timeSlot : timeSlots) {
                    addTimeSlot(timeSlot.getDay().getDayNo(), timeSlot);
                }
                break;

            case Week:
                for (TimeSlot timeSlot : timeSlots) {
                    addTimeSlot(1, timeSlot);
                }
                break;
        }
    }

    private void addTimeSlot(int index, TimeSlot timeSlot) {
        ArrayList<TimeSlot> timeSlots = timeSlotOccurences.get(index);
        if (timeSlots == null) {
            timeSlots = new ArrayList<TimeSlot>();
            timeSlotOccurences.put(index, timeSlots);
            indexMap.add(index);
        }
        timeSlots.add(timeSlot);
    }

    public int getSize() {
        return indexMap.size();
    }

    public int getOccurencesAt(int index) {
        ArrayList<TimeSlot> timeSlots = timeSlotOccurences.get(indexMap.get(index));
        if (timeSlots == null) {
            return 0;
        }
        return timeSlots.size();
    }

    public ArrayList<TimeSlot> getTimeSlotsAt(int index) {
        return timeSlotOccurences.get(indexMap.get(index));
    }
}

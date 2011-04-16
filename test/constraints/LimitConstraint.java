/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constraints;

import business.timetable.constraints.TimeSlotType;
import business.timetable.constraints.limit.TimeSlotOccurences;
import business.timetable.semester.Semesters;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class LimitConstraint {

    public static void main(String[] agrs) {
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            TimeSlots timeSlots = TimeSlots.getData(session, semesters.getValueAt(0));
            ArrayList<TimeSlot> b = new ArrayList<TimeSlot>();
            TimeSlotOccurences timeSlotOccurences = new TimeSlotOccurences(TimeSlotType.TimeSlot);
            ArrayList<TimeSlot> t = new ArrayList<TimeSlot>();
            t.add(timeSlots.get(1));
            t.add(timeSlots.get(5));
//        t.add(timeSlots.get(5));
//        t.add(timeSlots.get(6));
//        t.add(timeSlots.get(4));
//        t.add(timeSlots.get(14));
//        t.add(timeSlots.get(13));
//        t.add(timeSlots.get(8));
//        t.add(timeSlots.get(15));
//        t.add(timeSlots.get(18));
            for (TimeSlot timeSlot : t) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
            System.out.println();
            int minLimit = -1;
            int maxLimit = 1;
            timeSlotOccurences.setTimeSlotOccurences(t);
            for (int i = 0; i < timeSlotOccurences.getSize(); i++) {
                int occurences = timeSlotOccurences.getOccurencesAt(i);
                if (minLimit != -1 && occurences < minLimit) {
                    b.addAll(timeSlotOccurences.getTimeSlotsAt(i));
                }
                if (maxLimit != -1 && maxLimit < occurences) {
                    b.addAll(timeSlotOccurences.getTimeSlotsAt(i));
                }
            }
            for (TimeSlot timeSlot : b) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
        } catch (BasicException ex) {
            Logger.getLogger(LimitConstraint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

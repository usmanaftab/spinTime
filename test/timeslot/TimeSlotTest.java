/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package timeslot;

import business.timetable.semester.Semesters;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlotTest {

    public static void main(String[] args) {
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            TimeSlots timeSlots = TimeSlots.getData(session, semesters.getValueAt(0));
            ArrayList<TimeSlot> ts = timeSlots.getAllValues();
            for (TimeSlot timeSlot : ts) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
            System.out.println();
            System.out.println();
            Collections.sort(ts);
            for (TimeSlot timeSlot : ts) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
        } catch (BasicException ex) {
            Logger.getLogger(TimeSlotTest.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

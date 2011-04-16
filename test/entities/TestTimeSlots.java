/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import business.timetable.semester.Semesters;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TestTimeSlots {

    public static void main(String[] args) {
        try {
        Session session = new Session();
        Semesters semesters = Semesters.getData(session);
        TimeSlots timeSlots = TimeSlots.getData(session, semesters.getValueAt(0));
        timeSlots.printAll();
        } catch (BasicException ex) {
            Logger.getLogger(TestTimeSlots.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

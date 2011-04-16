/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.simulatedannealingttg.clashes;

import business.timetable.TimeTable;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import dal.timetable.db.Session;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Clashes {
    private AbstractClashes absClashes;

    public Clashes(Session session, TimeTable timeTable, ClashType clashType) {
        switch(clashType){
            case HardClashes:
                absClashes = new HardClashes(session, timeTable);
                break;
            case SoftClashes:
                absClashes = new SoftClashes(session, timeTable);
                break;
        }
    }

    public int getBadTimeSlotsSize() {
        return absClashes.badTimeSlotsSize();
    }

    public int count() {
        return absClashes.count();
    }

    public TimeSlot getBadTimeSlotAt(int index) {
        return absClashes.getBadTimeSlotAt(index);
    }

    private void updateClashes(Representable representation) throws BasicException {
        absClashes.updateClashes(representation);
    }

    public static Clashes getClashes(Session session, TimeTable timeTable, Representable representation, ClashType clashType) throws BasicException {
        Clashes clashes = new Clashes(session, timeTable, clashType);
        clashes.updateClashes(representation);
        return clashes;
    }
}
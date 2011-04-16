/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.simulatedannealingttg.clashes;

import business.timetable.TimeTable;
import business.timetable.constraints.Constraints;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class AbstractClashes {

    protected TimeTable timeTable;
    protected TimeSlots badTimeSlots;
    protected int count;
    private Session session;

    public AbstractClashes(Session session, TimeTable timeTable) {
        this.timeTable = timeTable;
        this.session = session;
        badTimeSlots = new TimeSlots();
        count = 0;
    }

    int badTimeSlotsSize() {
        return badTimeSlots.size();
    }

    TimeSlot getBadTimeSlotAt(int index) {
        return badTimeSlots.getValueAt(index);
    }

    private void updateBadTimeSlots(TimeSlots timeSlots) {
        for (TimeSlot timeSlot : timeSlots) {
            if (badTimeSlots.get(timeSlot.getID()) == null) {
                badTimeSlots.put(timeSlot.getID(), timeSlot);
            }
        }
    }

    int count() {
        return count;
    }

    private void updateCount(int count) {
        this.count += count;
    }

    void updateClashes(Representable representation) throws BasicException {
        for (Constraints constraints : getConstraints()) {
            constraints.checkConstraints(timeTable, representation);
            updateBadTimeSlots(constraints.getBadTimeSlots());
            updateCount(constraints.getClashCount());
        }
    }

    public Session getSession() {
        return session;
    }

    abstract ClashType getType();

    abstract ArrayList<Constraints> getConstraints() throws BasicException;
}

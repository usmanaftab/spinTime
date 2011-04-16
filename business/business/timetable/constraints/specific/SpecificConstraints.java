/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.specific;

import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import business.timetable.constraints.Constraints;
import business.timetable.courses.Course;
import business.timetable.courses.Courses;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.specific.SpecificConstraintDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SpecificConstraints extends Constraints<SpecificConstraint> {

    public SpecificConstraints() {
        super();
    }

    private static SpecificConstraints getResult(Session session, HashMap<Integer, HashMap<String, Object>> data) throws BasicException {
        SpecificConstraints specificConstraints = new SpecificConstraints();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Course course = Courses.getByID(session, (Integer) tokens.get("course"));
            TimeSlot timeSlot = TimeSlots.getByID(session, (Integer) tokens.get("timeslot"));
            ClassRoom classRoom = ClassRooms.getByID(session, (Integer) tokens.get("classroom"));
            SpecificConstraint specificConstraint = SpecificConstraint.createContraint(tokens, course, timeSlot, classRoom);

            specificConstraints.add(specificConstraint);
        }
        return specificConstraints;
    }

    public static SpecificConstraints getConstraints(Session session, boolean apply) throws BasicException {
        SpecificConstraintDataLogic constraints = new SpecificConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints(apply);
        return getResult(session, data);
    }

    public static SpecificConstraints getConstraints(Session session) throws BasicException {
        SpecificConstraintDataLogic constraints = new SpecificConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints();
        return getResult(session, data);
    }
}

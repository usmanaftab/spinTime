/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import business.timetable.TimeTable;
import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class Constraints<T extends Constraint> extends ArrayList<Constraint> {

    public Constraints() {
    }

    public TimeSlots getBadTimeSlots() {
        TimeSlots timeSlots = new TimeSlots();
        for (Constraint constraint : this) {
            for (TimeSlot timeSlot : constraint.getBadTimeSlots()) {
                if (timeSlots.get(timeSlot.getID()) == null) {
                    timeSlots.put(timeSlot.getID(), timeSlot);
                }
            }
        }
        return timeSlots;
    }

    public ClassRooms getBadClassRooms() {
        ClassRooms classRooms = new ClassRooms();
        for (Constraint constraint : this) {
            for (ClassRoom classRoom : constraint.getBadClassRooms()) {
                if (classRooms.get(classRoom.getID()) == null) {
                    classRooms.put(classRoom.getID(), classRoom);
                }
            }
        }
        return classRooms;
    }

    public void applyConstraints(TimeTable timeTable, Representable representation) {
        for (Constraint constraint : this) {
            constraint.applyConstraint(timeTable, representation);
        }
    }

    public void checkConstraints(TimeTable timeTable, Representable representation) {
        for (Constraint constraint : this) {
            constraint.checkConstraint(timeTable, representation);
        }
    }

    public int getClashCount() {
        int count = 0;
        for (Constraint constraint : this) {
            count += constraint.getClashCount();
        }
        return count;
    }
}

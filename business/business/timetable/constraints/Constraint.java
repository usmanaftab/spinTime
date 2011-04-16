/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import business.timetable.TimeTable;
import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import com.timetable.ValidationException;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class Constraint {

    private int ID;
    private String name;
    private boolean hardConstraint;
    private boolean apply;
    private int clashCount;
    private TimeSlots badTimeSlots;
    private ClassRooms badClassRooms;

    public Constraint(){
        ID = -1;
        name = "new Constraint";
        hardConstraint = true;
        apply = true;
    }

    public Constraint(int ID, String name, boolean hardConstraint, boolean apply) {
        setID(ID);
        setName(name);
        setHardConstraint(hardConstraint);
        setApply(apply);
        badTimeSlots = new TimeSlots();
        badClassRooms = new ClassRooms();
        clashCount = 0;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isHardConstraint() {
        return hardConstraint;
    }

    public void setHardConstraint(boolean hardConstraint) {
        this.hardConstraint = hardConstraint;
    }

    public boolean isApply() {
        return apply;
    }

    public void setApply(boolean apply) {
        this.apply = apply;
    }

    public void checkConstraint(TimeTable timeTable, Representable representation) {
        reset();
    }

    public void applyConstraint(TimeTable timeTable, Representable representation) {}

    protected void addBadTimeSlots(ArrayList<TimeSlot> timeSlots) {
        for (TimeSlot timeSlot : timeSlots) {
            if (badTimeSlots.get(timeSlot.getID()) == null) {
                badTimeSlots.put(timeSlot.getID(), timeSlot);
            }
            clashCount++;
        }
    }

    protected void addBadClassRooms(ArrayList<ClassRoom> classRooms) {
        for (ClassRoom classRoom : classRooms) {
            if (badClassRooms.get(classRoom.getID()) == null) {
                badClassRooms.put(classRoom.getID(), classRoom);
            }
            clashCount++;
        }
    }

    protected void reset(){
        badTimeSlots = new TimeSlots();
        clashCount = 0;
    }

    public TimeSlots getBadTimeSlots() {
        return badTimeSlots;
    }

    public ClassRooms getBadClassRooms() {
        return badClassRooms;
    }

    public int getClashCount(){
        return clashCount;
    }

    @Override
    public String toString() {
        return getName();
    }

    public static boolean validate(Constraint constraint) throws ValidationException{
        if(constraint.name.isEmpty()){
            throw new ValidationException("Constraint's name cannot be empty.");
        }
      
        return true;
    }
}

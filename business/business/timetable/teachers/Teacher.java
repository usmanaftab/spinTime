/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.teachers;

import business.timetable.EntityObject;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.courses.Courses;
import business.timetable.timeslots.TimeSlots;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Teacher extends EntityObject<Teacher> {

    private int ID;
    private String name;
    private Courses courses;
    private TimeSlots prevTimeSlots;

    Teacher(int ID, String name, Courses courses, TimeSlots prevTimeSlots,
            HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValProperties) {
        super(properties, multiValProperties);
        setID(ID);
        setName(name);
        setCourses(courses);
        setPrevTimeSlots(prevTimeSlots);
    }

    public Integer getID() {
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

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    public TimeSlots getPrevTimeSlots() {
        return prevTimeSlots;
    }

    public void setPrevTimeSlots(TimeSlots prevTimeSlots) {
        this.prevTimeSlots = prevTimeSlots;
    }

    @Override
    public String getProperty(String propertyName) {
        if (propertyName.equals("ID")) {
            return getID().toString();
        } else if (propertyName.equals("name")) {
            return getName();
        } else {
            return super.getProperty(propertyName);
        }
    }

    @Override
    public HashMap<Integer, Integer> getMultiValueProperty(String propertyName) {
        return super.getMultiValueProperty(propertyName);
    }

    @Override
    public String toString() {
        return getID() + " " + getName();
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Teacher other = (Teacher) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }

    public int compareTo(Teacher o) {
        if (ID > o.ID) {
            return 1;
        }
        if (ID < o.ID) {
            return -1;
        }
        return 0;
    }

    public static Teacher createTeacher(HashMap<String, Object> tokens, Courses courses, TimeSlots prevTimeSlots) {
        return new Teacher((Integer) tokens.get("ID"), (String) tokens.get("name"), courses, prevTimeSlots, (HashMap<String, String>) tokens.get("properties"), (HashMap<String, ArrayList<Integer>>) tokens.get("multivalueproperties"));
    }

    public static ArrayList<ConstraintProperty> getDefualtConstProperties(Session session) {
        return null;
    }
}

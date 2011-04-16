/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.timeslots;

import business.timetable.EntityObject;
import business.timetable.constraints.AttributeDType;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.representation.Keyable;
import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlot extends EntityObject<TimeSlot> implements Keyable<Integer> {

    private int ID;
    private Time beginTime;
    private Time endTime;
    private Day day;
    private Time duration;
    private Semester semester;

    public TimeSlot(int ID, String beginTime, String endTime, String day, Semester semester
            , HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValProperties) {
        super(properties, multiValProperties);
        setID(ID);
        this.beginTime = new Time(beginTime);
        this.endTime = new Time(endTime);
        this.day = Day.valueOf(day);
        this.semester = semester;
        duration = Time.getDifference(beginTime, endTime);
    }

    public Integer getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Time getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime.setTime(beginTime);
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndtime(String endTime) {
        this.endTime.setTime(endTime);
    }

    public Day getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = Day.valueOf(day);
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public Time getDuration() {
        return duration;
    }

    public String getTimeSlot() {
        return toString();
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    @Override
    public String getProperty(String propertyName) {
        if (propertyName.equals("ID")) {
            return getID().toString();
        } else if (propertyName.equals("begintime")) {
            return getBeginTime().toString();
        } else if (propertyName.equals("endtime")) {
            return getEndTime().toString();
        } else if (propertyName.equals("day")) {
            return getDay().toString();
        } else if (propertyName.equals("duration")) {
            return getDuration().toString();
        } else {
            return super.getProperty(propertyName);
        }
    }

    @Override
    public String toString() {
        return getDay() + " " + getBeginTime() + " " + getEndTime();
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
        final TimeSlot other = (TimeSlot) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }

    public int compareTo(TimeSlot o) {

        if (day.compareTo(o.day) != 0) {
            return day.compareTo(o.day);
        }

        if (beginTime.compareTo(o.beginTime) != 0) {
            return beginTime.compareTo(o.beginTime);
        }

        if (endTime.compareTo(o.endTime) != 0) {
            return endTime.compareTo(o.endTime);
        }

        return 0;
    }

    public boolean isOverlapping(TimeSlot o) {
        if (ID == o.ID) {
            return false;
        }

        if (day != o.day) {
            return false;
        }

        if (beginTime.compareTo(o.beginTime) <= 0 && endTime.compareTo(o.beginTime) >= 0) {
            return true;
        }

        if (o.beginTime.compareTo(beginTime) <= 0 && o.endTime.compareTo(beginTime) >= 0) {
            return true;
        }

        return false;
    }

    public static TimeSlot createTimeSlot(HashMap<String, Object> tokens, Semester semester) {
        return new TimeSlot((Integer) tokens.get("ID"), (String) tokens.get("begintime")
                , (String) tokens.get("endtime"), (String) tokens.get("day"), semester
                , (HashMap<String, String>) tokens.get("properties")
                , (HashMap<String, ArrayList<Integer>>) tokens.get("multivalueproperties"));
    }

    public static ArrayList<ConstraintProperty> getDefualtConstProperties(Session session) throws BasicException {
        ArrayList<ConstraintProperty> result = new ArrayList<ConstraintProperty>();
        AttributeDType attributeDType = AttributeDType.getAttributeDType(session, "Time");
        ConstraintProperty cp = new ConstraintProperty("duration", attributeDType);
        result.add(cp);

        return result;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.ConstraintDataLogic;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public enum TimeSlotType {

    TimeSlot,
    Day,
    Week;
    private int ID;
    private String name;

    public static ArrayList<TimeSlotType> getConstraintTimeSlotTypes(Session session, ConstraintType constraintType) throws BasicException {
        ArrayList<TimeSlotType> results = new ArrayList<TimeSlotType>();
        EntityDataLogic entityDataLogic = new EntityDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = entityDataLogic.getContraintTimeSlotTypes(constraintType.toString());
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            TimeSlotType timeSlotType = valueOf((Integer) tokens.get("ID"), (String) tokens.get("name"));
            results.add(timeSlotType);
        }
        return results;
    }

    public static TimeSlotType valueOf(int ID, String name) {
        TimeSlotType timeSlotType = TimeSlotType.valueOf(name);
        timeSlotType.setID(ID);
        timeSlotType.setName(name);
        return timeSlotType;
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

    public static ArrayList<TimeSlotType> getTimeSlotTypes(Session session, ConstraintType constraintType) throws BasicException {
        ArrayList<TimeSlotType> result = new ArrayList<TimeSlotType>();
        ConstraintDataLogic constraintDataLogic = new ConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraintDataLogic.getTimeSlotType(constraintType.getID());
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            TimeSlotType timeSlotType = TimeSlotType.valueOf((Integer) tokens.get("ID"), (String) tokens.get("name"));
            result.add(timeSlotType);
        }
        return result;
    }
}

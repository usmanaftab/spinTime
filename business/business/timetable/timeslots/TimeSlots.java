/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.timeslots;

import business.timetable.EntityHashMap;
import business.timetable.Hashmap;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.timeslot.TimeSlotDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlots extends Hashmap<Integer, TimeSlot> implements EntityHashMap<Integer, TimeSlot> {

    public static TimeSlot getByID(Session session, Integer ID) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        TimeSlots timeSlots = getByIDs(session, IDs);
        return (timeSlots.size() == 0) ? null : timeSlots.getValueAt(0);
    }

    public static TimeSlots getByIDs(Session session, Integer[] IDs) throws BasicException {
        TimeSlots timeSlots = new TimeSlots();
        TimeSlotDataLogic timeSlotDataLogic = new TimeSlotDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = timeSlotDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Semester semester = Semesters.getByID(session, (Integer) tokens.get("semester_ID"));
            TimeSlot timeSlot = TimeSlot.createTimeSlot(tokens, semester);
            timeSlots.put(timeSlot.getID(), timeSlot);
        }
        return timeSlots;
    }

    public static TimeSlots getData(Session session, Semester semester) throws BasicException {
        TimeSlots timeSlots = new TimeSlots();
        TimeSlotDataLogic timeSlotDataLogic = new TimeSlotDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = timeSlotDataLogic.getData(semester.getID());
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            TimeSlot timeSlot = TimeSlot.createTimeSlot(tokens, semester);
            timeSlots.put(timeSlot.getID(), timeSlot);
        }
        return timeSlots;
    }

    public EntityHashMap<Integer, TimeSlot> findBy(Semester semester, Departments departments) {
        TimeSlots timeSlots = new TimeSlots();
        for(TimeSlot timeSlot : this){
            if (timeSlot.getSemester().getID() == semester.getID()){
                timeSlots.put(timeSlot.getID(), timeSlot);
            }
        }
        return timeSlots;
    }

    public TimeSlots getTimeSlotsInList(ArrayList<Integer> arrayList) {
        TimeSlots timeSlotsList = new TimeSlots();
        for (int ID : arrayList) {
            TimeSlot timeSlot = get(ID);
            if (timeSlot != null) {
                timeSlotsList.put(timeSlot.getID(), timeSlot);
            }
        }
        return timeSlotsList;
    }

    public void printAll() {
        System.out.println("-----------TimeSlots------------");
        for (TimeSlot timeSlot : this) {
            System.out.println("TimeSlot ID: " + timeSlot.getID());
            System.out.println("TimeSlot Day: " + timeSlot.getDay());
            System.out.println("TimeSlot Begin Time: " + timeSlot.getBeginTime().toString());
            System.out.println("TimeSlot End Time: " + timeSlot.getEndTime().toString());
            System.out.println("Semester: " + timeSlot.getSemester().getName());
        }
        System.out.println();
    }
}

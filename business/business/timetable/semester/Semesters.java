/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.semester;

import business.timetable.Hashmap;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.semester.SemesterDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Semesters extends Hashmap<Integer, Semester> {

    public static Semester getByID(Session session, Integer ID) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        Semesters semesters = getByIDs(session, IDs);
        return (semesters.size() == 0) ? null : semesters.getValueAt(0);
    }

    public static Semesters getByIDs(Session session, Integer[] IDs) throws BasicException {
        Semesters semesters = new Semesters();
        SemesterDataLogic semesterDataLogic = new SemesterDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = semesterDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Semester s = Semester.createSemester(tokens);
            semesters.put(s.getID(), s);
        }
        return semesters;
    }

    public static Semester getByName(Session session, String semesterName) throws BasicException {
        SemesterDataLogic semesterDataLogic = new SemesterDataLogic(session);
        HashMap<String, Object> data = semesterDataLogic.getDataByName(semesterName);
        if(data == null){
            return null;
        }

        return Semester.createSemester(data);
    }

    public static Semesters getData(Session session) throws BasicException {
        Semesters semesters = new Semesters();
        SemesterDataLogic semesterDataLogic = new SemesterDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = semesterDataLogic.getData();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Semester s = Semester.createSemester(tokens);
            semesters.put(s.getID(), s);
        }
        return semesters;
    }
}

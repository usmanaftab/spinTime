/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.semester;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.semester.SemesterDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Semester {

    public static Semester getByID(Object get) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
    private int ID;
    private String name;

    public Semester(int ID, String name) {
        this.ID = ID;
        this.name = name;
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

    @Override
    public String toString() {
        return getName();
    }

    public static Semester createSemester(HashMap<String, Object> tokens) {
        return new Semester((Integer) tokens.get("ID"), (String) tokens.get("name"));
    }

    public static int insert(Session session, String semesterName) throws BasicException {
        SemesterDataLogic semesterDataLogic = new SemesterDataLogic(session);
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", semesterName);
        return semesterDataLogic.insert(data);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.department;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.department.DepartmentDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Department {

    private int ID;
    private String name;

    public Department(int ID, String name) {
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

    public static Department createDepartment(HashMap<String, Object> tokens) {
        return new Department((Integer) tokens.get("ID"), (String) tokens.get("name"));
    }

    public static void deleteAll(Session session) throws BasicException {
        DepartmentDataLogic departmentDataLogic = new DepartmentDataLogic(session);
        departmentDataLogic.deleteAll();
    }
}

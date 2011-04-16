/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.department;

import business.timetable.Hashmap;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.department.DepartmentDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Departments extends Hashmap<Integer, Department> {

    public static Department getByID(Session session, Integer ID) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        Departments departments = getByIDs(session, IDs);
        return (departments.size() == 0)? null : departments.getValueAt(0);
    }

    private static Departments getByIDs(Session session, Integer[] IDs) throws BasicException {
        Departments departments = new Departments();
        DepartmentDataLogic departmentDataLogic = new DepartmentDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = departmentDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department d = Department.createDepartment(tokens);
            departments.put(d.getID(), d);
        }
        return departments;
    }

    public static Departments getData(Session session) throws BasicException {
        Departments departments = new Departments();
        DepartmentDataLogic departmentDataLogic = new DepartmentDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = departmentDataLogic.getData();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department d = Department.createDepartment(tokens);
            departments.put(d.getID(), d);
        }
        return departments;
    }
}

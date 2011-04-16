/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.classrooms;

import business.timetable.EntityHashMap;
import business.timetable.Hashmap;
import business.timetable.department.Department;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.classroom.ClassRoomDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ClassRooms extends Hashmap<Integer, ClassRoom> implements EntityHashMap<Integer, ClassRoom> {

    public static ClassRoom getByID(Session session, Integer ID) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        ClassRooms classRooms = getByIDs(session, IDs);
        return (classRooms.size() == 0)? null : classRooms.getValueAt(0);
    }

    public static ClassRooms getByIDs(Session session, Integer[] IDs) throws BasicException {
        ClassRooms classRooms = new ClassRooms();
        ClassRoomDataLogic classRoomDataLogic = new ClassRoomDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = classRoomDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department department = Departments.getByID(session, (Integer) tokens.get("departmentID"));
            Semester semester = Semesters.getByID(session, (Integer) tokens.get("semesterID"));
            ClassRoom c = ClassRoom.createClassRoom(tokens, department, semester);
            classRooms.put(c.getID(), c);
        }
        return classRooms;
    }

    public static ClassRooms getData(Session session, Departments departments, Semester semester) throws BasicException {
        ClassRooms classRooms = new ClassRooms();
        ClassRoomDataLogic classRoomDataLogic = new ClassRoomDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = classRoomDataLogic.getData(departments.getAllKeys(), semester.getID());
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department department = departments.get((Integer) tokens.get("departmentID"));
            ClassRoom c = ClassRoom.createClassRoom(tokens, department, semester);
            classRooms.put(c.getID(), c);
        }

        return classRooms;
    }

    public EntityHashMap<Integer, ClassRoom> findBy(Semester semester, Departments departments) {
        ClassRooms classRooms = new ClassRooms();
        for(ClassRoom classRoom : this){
            if(departments.get(classRoom.getDepartment().getID()) != null) {
                if(classRoom.getSemester().getID() == semester.getID()){
                    classRooms.put(classRoom.getID(), classRoom);
                }
            }
        }
        return classRooms;
    }

    public void printAll() {
        System.out.println("-----------ClassRooms------------");
        for (ClassRoom c : this) {
            System.out.println("Class ID: " + c.getID());
            System.out.println("Class Name: " + c.getName());
            System.out.println("Class capacity: " + c.getCapacity());
        }
        System.out.println();
    }
}

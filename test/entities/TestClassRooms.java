/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import business.timetable.classrooms.ClassRooms;
import business.timetable.department.Departments;
import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TestClassRooms {

    public static void main(String[] args) {
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            Departments departments = Departments.getData(session);
            ClassRooms classRooms = ClassRooms.getData(session, departments, semesters.getValueAt(0));
            classRooms.printAll();
        } catch (BasicException ex) {
            Logger.getLogger(TestClassRooms.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

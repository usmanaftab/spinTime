/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import business.timetable.courses.Courses;
import business.timetable.department.Departments;
import business.timetable.semester.Semesters;
import business.timetable.teachers.Teachers;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TestTeachers {

    public static void main(String[] args) {
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            Departments departments = Departments.getData(session);
            TimeSlots timeSlots = TimeSlots.getData(session, semesters.getValueAt(0));
            Courses courses = Courses.getData(session, departments, semesters.getValueAt(0));
            Teachers teachers = Teachers.getData(session, departments, semesters.getValueAt(0), courses, timeSlots);
            teachers.printAll();
        } catch (BasicException ex) {
            Logger.getLogger(TestStudents.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

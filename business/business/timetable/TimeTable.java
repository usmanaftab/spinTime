/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable;

import business.timetable.classrooms.ClassRooms;
import business.timetable.courses.Courses;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.students.Students;
import business.timetable.teachers.Teachers;
import business.timetable.timeslots.TimeSlots;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface TimeTable {

    public ClassRooms getClassRooms();

    public Courses getCourses();

    public Teachers getTeachers();

    public TimeSlots getTimeSlots();

    public Students getStudents();

    public Departments getDepartments();

    public Semester getSemester();
}

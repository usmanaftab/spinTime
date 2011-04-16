/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.courses;

import business.timetable.EntityHashMap;
import business.timetable.Hashmap;
import business.timetable.department.Department;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.course.CourseDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Courses extends Hashmap<Integer, Course> implements EntityHashMap<Integer, Course> {

    public static Course getByID(Session session, Integer ID) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        Courses courses = getByIDs(session, IDs);
        return (courses.size() == 0)? null : courses.getValueAt(0);
    }

    public static Courses getByIDs(Session session, Integer[] IDs) throws BasicException {
        Courses courses = new Courses();
        CourseDataLogic courseDataLogic = new CourseDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = courseDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department department = Departments.getByID(session, (Integer) tokens.get("departmentID"));
            Semester semester = Semesters.getByID(session, (Integer) tokens.get("semesterID"));
            Course course = Course.createCourse(tokens, department, semester);
            courses.put(course.getID(), course);
        }
        return courses;
    }

    public static Courses getData(Session session, Departments departments, Semester semester) throws BasicException {
        Courses courses = new Courses();
        CourseDataLogic courseDataLogic = new CourseDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = courseDataLogic.getData(departments.getAllKeys(), semester.getID());
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department department = departments.get((Integer) tokens.get("departmentID"));
            Course course = Course.createCourse(tokens, department, semester);
            courses.put(course.getID(), course);
        }

        return courses;
    }

    public EntityHashMap<Integer, Course> findBy(Semester semester, Departments departments) {
        Courses coursesList = new Courses();
        for (Course course : this) {
            if (departments.get(course.getDepartment().getID()) != null) {
                if (course.getSemester().getID() == semester.getID()) {
                    coursesList.put(course.getID(), course);
                }
            }
        }
        return coursesList;
    }

    public Courses getCourseInList(ArrayList<Integer> arrayList) {
        Courses coursesList = new Courses();
        for (int ID : arrayList) {
            Course course = get(ID);
            if (course != null) {
                coursesList.put(course.getID(), course);
            }
        }
        return coursesList;
    }

    public void printAll() {
        System.out.println("-----------Courses------------");
        for (Course course : this) {
            System.out.println("Course ID: " + course.getID());
            System.out.println("Course Name: " + course.getName());
            System.out.println("Course Section: " + course.getSection());
            System.out.println("Course Capacity: " + course.getCapacity());
            System.out.println("Course Duration: " + course.getDuration());
            System.out.println("Course Lesson Count: " + course.getLessonCount());
        }
        System.out.println();
    }
}

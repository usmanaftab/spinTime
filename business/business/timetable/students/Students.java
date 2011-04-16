/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.students;

import business.timetable.EntityHashMap;
import business.timetable.courses.Courses;
import business.timetable.courses.Course;
import business.timetable.Hashmap;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.student.StudentDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Students extends Hashmap<String, Student> implements EntityHashMap<String, Student> {

    public static Students getData(Session session, Departments departments, Semester semester, Courses coursesList) throws BasicException {
        Students students = new Students();
        StudentDataLogic studentDataLogic = new StudentDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = studentDataLogic.getData(departments.getAllKeys(), semester.getID());
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Courses courses = coursesList.getCourseInList((ArrayList<Integer>) tokens.get("courselist"));
            Student student = Student.createStudent(tokens, courses);
            for (Course course : courses) {
                course.getStudents().put(student.getID(), student);
            }
            students.put(student.getID(), student);
        }
        return students;
    }

    public EntityHashMap<String, Student> findBy(Semester semester, Departments departments) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Students getStudentInList(ArrayList<String> arrayList) {
        Students studentsList = new Students();
        for (String ID : arrayList) {
            Student student = get(ID);
            if (student != null) {
                studentsList.put(student.getID(), student);
            }
        }
        return studentsList;
    }

    public void printAll() {
        System.out.println("-----------Students------------");
        for (Student student : this) {
            System.out.println("Student ID: " + student.getID());
            System.out.println("Student Name: " + student.getName());
            student.getCourses().printAll();
        }
        System.out.println();
    }
}

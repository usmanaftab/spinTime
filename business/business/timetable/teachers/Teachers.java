/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.teachers;

import business.timetable.EntityHashMap;
import business.timetable.courses.Course;
import business.timetable.courses.Courses;
import business.timetable.Hashmap;
import business.timetable.department.Departments;
import business.timetable.timeslots.TimeSlots;
import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.teacher.TeacherDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Teachers extends Hashmap<Integer, Teacher> implements EntityHashMap<Integer, Teacher> {

    public static Teacher getByID(Session session, Integer ID, Courses coursesList, TimeSlots timeSlotsList) throws BasicException {
        Integer[] IDs = new Integer[1];
        IDs[0] = ID;
        Teachers teachers = getByIDs(session, IDs, coursesList, timeSlotsList);
        return (teachers.size() == 0) ? null : teachers.getValueAt(0);
    }

    public static Teachers getByIDs(Session session, Integer[] IDs, Courses coursesList, TimeSlots timeSlotsList) throws BasicException {
        Teachers teachers = new Teachers();
        TeacherDataLogic teacherDataLogic = new TeacherDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = teacherDataLogic.getDataByIDs(IDs);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Courses courses = coursesList.getCourseInList((ArrayList<Integer>) tokens.get("courselist"));
            TimeSlots prevTimeSlots = timeSlotsList.getTimeSlotsInList((ArrayList<Integer>) tokens.get("previoustimeslots"));

            Teacher teacher = Teacher.createTeacher(tokens, courses, prevTimeSlots);
            for (Course course : courses) {
                course.addTeacher(teacher);
            }
            teachers.put(teacher.getID(), teacher);
        }
        return teachers;
    }

    public static Teachers getData(Session session, Departments departments, Semester semester, Courses coursesList, TimeSlots timeSlotsList) throws BasicException {
        Teachers teachers = new Teachers();
        TeacherDataLogic teacherDataLogic = new TeacherDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = teacherDataLogic.getData(departments.getAllKeys(), semester.getID());
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Courses courses = coursesList.getCourseInList((ArrayList<Integer>) tokens.get("courselist"));
            TimeSlots prevTimeSlots = timeSlotsList.getTimeSlotsInList((ArrayList<Integer>) tokens.get("previoustimeslots"));

            Teacher teacher = Teacher.createTeacher(tokens, courses, prevTimeSlots);
            for (Course course : courses) {
                course.addTeacher(teacher);
            }
            teachers.put(teacher.getID(), teacher);
        }
        return teachers;
    }

    public EntityHashMap<Integer, Teacher> findBy(Semester semester, Departments departments) {
        Teachers teachers = new Teachers();
        for (Teacher teacher : this) {
            for (Course course : teacher.getCourses()) {
                if (departments.get(course.getDepartment().getID()) != null) {
                    if (course.getSemester().getID() == semester.getID()) {
                        teachers.put(teacher.getID(), teacher);
                        continue;
                    }
                }
            }
        }
        return teachers;
    }

    public Teachers getTeacherInList(ArrayList<Integer> arrayList) {
        Teachers teachersList = new Teachers();
        for (int ID : arrayList) {
            Teacher teacher = get(ID);
            if (teacher != null) {
                teachersList.put(teacher.getID(), teacher);
            }
        }
        return teachersList;
    }

    public void printAll() {
        System.out.println("-----------Teacher------------");
        for (Teacher teacher : this) {
            System.out.println("Teacher ID: " + teacher.getID());
            System.out.println("Teacher Name: " + teacher.getName());
            teacher.getCourses().printAll();
        }
        System.out.println();
    }
}

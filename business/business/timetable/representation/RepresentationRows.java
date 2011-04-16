/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

import business.timetable.Hashmap;
import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import business.timetable.courses.Course;
import business.timetable.courses.Courses;
import business.timetable.department.Department;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.teachers.Teacher;
import business.timetable.teachers.Teachers;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.representation.RepresentationDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class RepresentationRows extends Hashmap<Integer, RepresentationRow> {

    public RepresentationRows findBy(Semester semester, Departments departments) {
        RepresentationRows representationRows = new RepresentationRows();
        for(RepresentationRow representationRow : this){
            if(representationRow.getSemester().getID() == semester.getID()){
                if(departments.get(representationRow.getDepartment().getID()) != null){
                    representationRows.put(representationRow.getID(), representationRow);
                }
            }
        }
        return representationRows;
    }

    public static RepresentationRows getData(Session session, Departments departments, Semester semester) throws BasicException {
        RepresentationRows representationRows = new RepresentationRows();
        RepresentationDataLogic representationDataLogic = new RepresentationDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data =
                representationDataLogic.getData(departments.getAllKeys(), semester.getID());
        ClassRooms classRooms = ClassRooms.getData(session, departments, semester);
        TimeSlots timeSlots = TimeSlots.getData(session, semester);
        Courses courses = Courses.getData(session, departments, semester);
        Teachers teachers = Teachers.getData(session, departments, semester, courses, timeSlots);
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            Department department = departments.get((Integer) tokens.get("departmentID"));
            ClassRoom classRoom = classRooms.get((Integer) tokens.get("classroomID"));
            TimeSlot timeSlot = timeSlots.get((Integer) tokens.get("timeslotID"));
            Teacher teacher = teachers.get((Integer) tokens.get("teacherID"));
            Course course = courses.get((Integer) tokens.get("courseID"));
            int lessonNo = (Integer) tokens.get("lessonno");
            int ID = (Integer) tokens.get("ID");

            RepresentationRow representationRow = new RepresentationRow(ID, course, lessonNo, teacher
                    , classRoom, timeSlot, semester, department);
            representationRows.put(ID, representationRow);
        }
        return representationRows;
    }
}

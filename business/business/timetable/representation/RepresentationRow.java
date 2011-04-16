/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.representation;

import business.timetable.classrooms.ClassRoom;
import business.timetable.courses.Course;
import business.timetable.department.Department;
import business.timetable.semester.Semester;
import business.timetable.teachers.Teacher;
import business.timetable.timeslots.TimeSlot;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class RepresentationRow {

    private int ID;
    private Course course;
    private int lessonNo;
    private Teacher teacher;
    private ClassRoom classRoom;
    private TimeSlot timeSlot;
    private Semester semester;
    private Department department;

    public RepresentationRow(int ID, Course course, int lessonNo, Teacher teacher, ClassRoom classRoom, TimeSlot timeSlot, Semester semester, Department department) {
        this.ID = ID;
        this.course = course;
        this.lessonNo = lessonNo;
        this.teacher = teacher;
        this.classRoom = classRoom;
        this.timeSlot = timeSlot;
        this.semester = semester;
        this.department = department;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public ClassRoom getClassRoom() {
        return classRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.classRoom = classRoom;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.timeSlot = timeSlot;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return timeSlot.toString() + " \n" + classRoom.toString()
                + " \n" + course.toString() + " " + lessonNo + 1;
    }

}

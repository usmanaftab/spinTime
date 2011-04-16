/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.courses;

import business.timetable.representation.Valueable;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class CourseLesson implements Valueable<String> {

    private String ID;
    private int lessonNo;
    private boolean fixedTimeSlot;
    private boolean fixedClassRoom;
    private Course course;

    public CourseLesson(String ID, int lessonNo, Course course) {
        this.ID = ID;
        this.lessonNo = lessonNo;
        this.fixedTimeSlot = false;
        this.fixedClassRoom = false;
        this.course = course;
    }

    public String getID() {
        return ID;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    public boolean isFixedTimeSlot() {
        return fixedTimeSlot;
    }

    public void setFixedTimeSlot(boolean fixed) {
        this.fixedTimeSlot = fixed;
    }

    public boolean isFixedClassRoom() {
        return fixedClassRoom;
    }

    public void setFixedClassRoom(boolean fixedClassRoom) {
        this.fixedClassRoom = fixedClassRoom;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    @Override
    public int hashCode() {
        return getID().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CourseLesson other = (CourseLesson) obj;
        if ((this.ID == null) ? (other.ID != null) : !this.ID.equals(other.ID)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return course.toString();
    }
}

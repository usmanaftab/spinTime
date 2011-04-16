/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.specific;

import business.timetable.TimeTable;
import business.timetable.classrooms.ClassRoom;
import com.timetable.ValidationException;
import business.timetable.constraints.*;
import business.timetable.courses.Course;
import business.timetable.courses.CourseLesson;
import business.timetable.representation.Keys;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.specific.SpecificConstraintDataLogic;
import java.util.HashMap;
import java.util.Random;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SpecificConstraint extends Constraint {

    private Course contraintCourse;
    private TimeSlot contraintTimeSlot;
    private ClassRoom constraintClassRoom;
    private Integer lessonNo;

    public SpecificConstraint() {
    }

    public SpecificConstraint(int ID, String name, boolean hardConstraint, boolean apply,
            Course course, TimeSlot timeSlot, ClassRoom classRoom, int lessonNo) {
        super(ID, name, hardConstraint, apply);

        this.contraintCourse = course;
        this.contraintTimeSlot = timeSlot;
        this.constraintClassRoom = classRoom;
        this.lessonNo = lessonNo;
    }

    public Course getCourse() {
        return contraintCourse;
    }

    public void setCourse(Course course) {
        this.contraintCourse = course;
    }

    public TimeSlot getTimeSlot() {
        return contraintTimeSlot;
    }

    public void setTimeSlot(TimeSlot timeSlot) {
        this.contraintTimeSlot = timeSlot;
    }

    public ClassRoom getClassRoom() {
        return constraintClassRoom;
    }

    public void setClassRoom(ClassRoom classRoom) {
        this.constraintClassRoom = classRoom;
    }

    public int getLessonNo() {
        return lessonNo;
    }

    public void setLessonNo(int lessonNo) {
        this.lessonNo = lessonNo;
    }

    @Override
    public void applyConstraint(TimeTable timeTable, Representable representation) {
        super.applyConstraint(timeTable, representation);

        TimeSlot destTimeSlot = null;
        if (contraintTimeSlot != null) {
            destTimeSlot = timeTable.getTimeSlots().get(contraintTimeSlot.getID());
        }
        ClassRoom destClassRoom = null;
        if (constraintClassRoom != null) {
            destClassRoom = timeTable.getClassRooms().get(constraintClassRoom.getID());
        }
        Course course = timeTable.getCourses().get(contraintCourse.getID());
        CourseLesson desCourseLesson = course.getCourseLessonAt(lessonNo);

        Keys<TimeSlot, ClassRoom> keys = representation.getKeys(desCourseLesson);
        CourseLesson oldCourseLesson = null;

        if (destTimeSlot != null && destClassRoom != null) {
            oldCourseLesson = representation.get(destTimeSlot, destClassRoom);

            if (oldCourseLesson != null) {
                if (oldCourseLesson.isFixedClassRoom() || oldCourseLesson.isFixedTimeSlot()) {
                    throw new SpecificConstraintException("Clash, Timeslot and classroom already bound to a fixed course.");
                }
            }

            desCourseLesson.setFixedTimeSlot(true);
            desCourseLesson.setFixedClassRoom(true);

        } else if (destTimeSlot != null) {
            destClassRoom = getClassRoom(timeTable, representation, destTimeSlot);
            oldCourseLesson = representation.get(destTimeSlot, destClassRoom);
            desCourseLesson.setFixedTimeSlot(true);

        } else if (destClassRoom != null) {
            destTimeSlot = getTimeSlot(timeTable, representation, destClassRoom);
            oldCourseLesson = representation.get(destTimeSlot, destClassRoom);
            desCourseLesson.setFixedClassRoom(true);

        } else {
            throw new SpecificConstraintException("Timeslot ID and Classroom ID both cannot be null.");
        }

        representation.put(destTimeSlot, destClassRoom, desCourseLesson);
        representation.put(keys.getK1(), keys.getK2(), oldCourseLesson);
    }

    private ClassRoom getClassRoom(TimeTable timeTable, Representable representation, TimeSlot timeSlot) {
        Random random = new Random();
        ClassRoom classRoom = null;

        while (classRoom == null) {
            int index = random.nextInt(timeTable.getClassRooms().size());
            classRoom = timeTable.getClassRooms().getValueAt(index);
            CourseLesson courseLesson = representation.get(timeSlot, classRoom);

            if (courseLesson == null) {
                break;
            }

            if (courseLesson.isFixedClassRoom()) {
                classRoom = null;
            }
        }
        return classRoom;
    }

    private TimeSlot getTimeSlot(TimeTable timeTable, Representable representation, ClassRoom classRoom) {
        Random random = new Random();
        TimeSlot timeSlot = null;

        while (timeSlot == null) {
            int index = random.nextInt(timeTable.getTimeSlots().size());
            timeSlot = timeTable.getTimeSlots().getValueAt(index);
            CourseLesson courseLesson = representation.get(timeSlot, classRoom);

            if (courseLesson == null) {
                break;
            }

            if (courseLesson.isFixedTimeSlot()) {
                timeSlot = null;
            }
        }
        return timeSlot;
    }

    public static SpecificConstraint createContraint(HashMap<String, Object> tokens,
            Course course, TimeSlot timeSlot, ClassRoom classRoom) {

        return new SpecificConstraint((Integer) tokens.get("ID"), (String) tokens.get("name"),
                (Boolean) tokens.get("hardconstraint"), (Boolean) tokens.get("apply"),
                course, timeSlot, classRoom, (Integer) tokens.get("lessonno"));
    }

    public static SpecificConstraint createDefaultConstraint() {
        SpecificConstraint specificConstraint = new SpecificConstraint();
        specificConstraint.setName("Specific Constraint");
        specificConstraint.setLessonNo(0);
        return specificConstraint;
    }

    public static int save(Session session, SpecificConstraint specificConstraint) throws ValidationException, BasicException {
        if (!validate(specificConstraint)) {
            return -1;
        }

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("ID", specificConstraint.getID());
        data.put("name", specificConstraint.getName());
        data.put("apply", specificConstraint.isApply());
        data.put("hardconstraint", specificConstraint.isHardConstraint());
        data.put("coursesectionID", specificConstraint.contraintCourse.getID());
        data.put("lessonno", specificConstraint.lessonNo);

        if (specificConstraint.constraintClassRoom != null) {
            data.put("classroomID", specificConstraint.constraintClassRoom.getID());
        }

        if (specificConstraint.contraintTimeSlot != null) {
            data.put("timeslotID", specificConstraint.contraintTimeSlot.getID());
        }

        SpecificConstraintDataLogic specificConstraintDataLogic = new SpecificConstraintDataLogic(session);
        if (specificConstraint.getID() == -1) {
            return specificConstraintDataLogic.insert(data);
        } else {
            return specificConstraintDataLogic.update(data);
        }
    }

    public static boolean validate(SpecificConstraint specificConstraint) throws ValidationException {
        if (!Constraint.validate(specificConstraint)) {
            return false;
        }

        if (specificConstraint.contraintCourse == null) {
            throw new ValidationException("Course ID must be specified in Specific Constraints.");
        }

        if (specificConstraint.lessonNo == null || specificConstraint.lessonNo < 0) {
            throw new ValidationException("Course lesson no. must be specified in Specific Constraints.");
        }

        if (specificConstraint.constraintClassRoom == null && specificConstraint.contraintTimeSlot == null) {
            throw new ValidationException("Either class room or timeslot or both must be specified in Specific Constraints.");
        }

        return true;
    }
}
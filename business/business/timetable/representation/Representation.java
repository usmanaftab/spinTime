/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

import business.timetable.TimeTable;
import business.timetable.timeslots.TimeSlot;
import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import business.timetable.courses.CourseLesson;
import business.timetable.semester.Semester;
import business.timetable.teachers.Teacher;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.representation.RepresentationDataLogic;
/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Representation
        extends AbsRepresentation<Integer, TimeSlot, Integer, ClassRoom, String, CourseLesson>
        implements Cloneable, Representable {

    private TimeTable timeTable;

    public Representation(TimeTable timeTable, TimeSlots keys1, ClassRooms keys2) {
        super(keys1, keys2);

        this.timeTable = timeTable;
    }

    @Override
    public Representation clone() {
        Representation clone = new Representation(timeTable, (TimeSlots) getKeys1(), (ClassRooms) getKeys2());
        for (TimeSlot timeSlot : clone.getKeys1()) {
            for (ClassRoom classRoom : clone.getKeys2()) {
                CourseLesson courseLesson = get(timeSlot, classRoom);
                if (courseLesson != null) {
                    clone.put(timeSlot, classRoom, courseLesson);
                }
            }
        }
        return clone;
    }

    public ClassRoom getClassRoomAt(int index) {
        return getKeys2().getValueAt(index);
    }

    public TimeSlot getTimeSlotAt(int index) {
        return getKeys1().getValueAt(index);
    }

    public void save(Session session) throws BasicException {
        try {
            RepresentationDataLogic repDataLogic = new RepresentationDataLogic(session);
            session.beginTransaction();

            int semesterID = timeTable.getSemester().getID();
            repDataLogic.deleteTimeTable(timeTable.getDepartments().getAllKeys(), semesterID);

            for (TimeSlot timeSlot : getKeys1()) {
                for (ClassRoom classRoom : getKeys2()) {
                    CourseLesson courseLesson = get(timeSlot, classRoom);
                    if (courseLesson == null) {
                        continue;
                    }

                    for (Teacher teacher : courseLesson.getCourse().getTeachers()) {
                        Object[] data = new Object[7];
                        data[0] = semesterID;
                        data[1] = courseLesson.getCourse().getDepartment().getID();
                        data[2] = courseLesson.getCourse().getID();
                        data[3] = courseLesson.getLessonNo();
                        data[4] = teacher.getID();
                        data[5] = classRoom.getID();
                        data[6] = timeSlot.getID();

                        repDataLogic.insert(data);
                    }
                }
            }

            session.endTransaction();
        } catch (BasicException ex) {
            session.rollBack();
            throw new BasicException(Representation.class.getName(), ex);
        }
    }

    public static void deleteTimeTable(Session session, Semester semester) throws BasicException{
        RepresentationDataLogic repDataLogic = new RepresentationDataLogic(session);
        repDataLogic.deleteTimeTable(semester.getID());
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

import business.timetable.classrooms.ClassRoom;
import business.timetable.courses.CourseLesson;
import business.timetable.timeslots.TimeSlot;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface Representable {

    public void put(TimeSlot key1, ClassRoom key2, CourseLesson value);

    public CourseLesson get(TimeSlot key1, ClassRoom key2);

    public Keys<TimeSlot, ClassRoom> getKeys(CourseLesson value);
}

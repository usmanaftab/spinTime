/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable;

import business.timetable.classrooms.ClassRoom;
import business.timetable.constraints.ConstraintMultiValProperty;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.constraints.ConstraintType;
import business.timetable.courses.Course;
import business.timetable.courses.CourseLesson;
import business.timetable.representation.Keyable;
import business.timetable.representation.Keys;
import business.timetable.representation.Representable;
import business.timetable.students.Student;
import business.timetable.teachers.Teacher;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public enum Entity {

    Course,
    Teacher,
    Student,
    ClassRoom,
    TimeSlot;
    private int ID;
    private String name;
    private String displayName;
    private int position;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public static Entity valueOf(int ID, String name, String displayName, int position) {
        Entity entity = Entity.valueOf(name);
        entity.setID(ID);
        entity.setName(name);
        entity.setDisplayName(displayName);
        entity.setPosition(position);
        return entity;
    }

    public static ArrayList<Entity> getConstraintEntities(Session session, ConstraintType constraintType) throws BasicException {
        ArrayList<Entity> results = new ArrayList<Entity>();
        EntityDataLogic entityDataLogic = new EntityDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = entityDataLogic.getConstraintEntities(constraintType.toString());
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            Entity entity = valueOf((Integer) tokens.get("ID"),
                    (String) tokens.get("name"),
                    (String) tokens.get("displayname"),
                    (Integer) tokens.get("position"));
            results.add(entity);
        }
        return results;
    }

    public static Entity getEntity(Session session, String name) throws BasicException {
        EntityDataLogic entityDataLogic = new EntityDataLogic(session);
        HashMap<String, Object> data = entityDataLogic.getEntity(name);
        return valueOf((Integer) data.get("ID"), (String) data.get("name"), (String) data.get("displayname"), 0);
    }

    public static ArrayList<Entity> getEntitiesAtPosition(ArrayList<Entity> entities, int position) {
        ArrayList<Entity> result = new ArrayList<Entity>();
        for (Entity entity : entities) {
            if (entity.getPosition() == position) {
                result.add(entity);
            }
        }
        return result;
    }

    public static HashMap<String, ArrayList<ConstraintProperty>> getAllProperties(Session session) throws BasicException {
        EntityDataLogic entityDataLogic = new EntityDataLogic(session);
        HashMap<String, ArrayList<ConstraintProperty>> results = new HashMap<String, ArrayList<ConstraintProperty>>();

        ArrayList<ConstraintProperty> cps =
                business.timetable.classrooms.ClassRoom.getDefualtConstProperties(session);
        results.put(ClassRoom.toString(), cps);

        cps = business.timetable.timeslots.TimeSlot.getDefualtConstProperties(session);
        results.put(TimeSlot.toString(), cps);

        cps = business.timetable.courses.Course.getDefualtConstProperties(session);
        results.put(Course.toString(), cps);

        cps = business.timetable.teachers.Teacher.getDefualtConstProperties(session);
        results.put(Teacher.toString(), cps);

        cps = business.timetable.students.Student.getDefualtConstProperties(session);
        results.put(Student.toString(), cps);

        HashMap<Integer, HashMap<String, Object>> data = entityDataLogic.getProperties();
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            String entityName = (String) tokens.get("constraintentityname");
            ConstraintProperty constraintProperty = ConstraintProperty.createConstraintProperty(tokens);
            ArrayList<ConstraintProperty> result = results.get(entityName);
            if (result == null) {
                result = new ArrayList<ConstraintProperty>();
                results.put(entityName, result);
            }
            result.add(constraintProperty);
        }

        return results;
    }

    public static HashMap<String, ArrayList<ConstraintMultiValProperty>> getAllMultiValProperties(Session session) throws BasicException {
        EntityDataLogic entityDataLogic = new EntityDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = entityDataLogic.getMultiValProperties();
        HashMap<String, ArrayList<ConstraintMultiValProperty>> results = new HashMap<String, ArrayList<ConstraintMultiValProperty>>();
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            String entityName = (String) tokens.get("leftconstraintentityname");
            ConstraintMultiValProperty constraintMultiValProperty = ConstraintMultiValProperty.createConstraintMultiValProperty(tokens);
            ArrayList<ConstraintMultiValProperty> result = results.get(entityName);
            if (result == null) {
                result = new ArrayList<ConstraintMultiValProperty>();
                results.put(entityName, result);
            }
            result.add(constraintMultiValProperty);
        }
        return results;
    }

    public ArrayList<? extends EntityObject> getEntities(TimeTable timeTable) {
        switch (this) {
            case Course:
                return timeTable.getCourses().getAllValues();
            case Teacher:
                return timeTable.getTeachers().getAllValues();
            case Student:
                return timeTable.getStudents().getAllValues();
            case ClassRoom:
                return timeTable.getClassRooms().getAllValues();
            case TimeSlot:
                return timeTable.getTimeSlots().getAllValues();
            default:
                return null;
        }
    }

    public ArrayList<? extends Keyable> getRightEntityObject(TimeTable timeTable, Representable representation, EntityObject leftEntityObject, Entity rightEntity) {
        switch (rightEntity) {
            case TimeSlot:
                return getTimeSlots(timeTable, representation, leftEntityObject);

            case ClassRoom:
                return getClassRooms(timeTable, representation, leftEntityObject);

            default:
                return null;
        }
    }

    public ArrayList<? extends Keyable> getTimeSlots(TimeTable timeTable, Representable representation, EntityObject entityObject) {

        ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
        ArrayList<TimeSlot> subTimeSlots;
        switch (this) {
            case Course:
                subTimeSlots = getTimeSlot(representation, (Course) entityObject);
                timeSlots.addAll(subTimeSlots);
                break;

            case Teacher:
                Teacher teacher = (Teacher) entityObject;
                for (Course course : teacher.getCourses()) {
                    subTimeSlots = getTimeSlot(representation, (Course) course);
                    timeSlots.addAll(subTimeSlots);
                    timeSlots.addAll(teacher.getPrevTimeSlots().getAllValues());
                }
                break;

            case Student:
                Student student = (Student) entityObject;
                for (Course course : student.getCourses()) {
                    subTimeSlots = getTimeSlot(representation, (Course) course);
                    timeSlots.addAll(subTimeSlots);
                }
                break;

            case ClassRoom:
                ClassRoom classRoom = (ClassRoom) entityObject;
                for (TimeSlot timeSlot1 : timeTable.getTimeSlots()) {
                    if (representation.get(timeSlot1, classRoom) != null) {
                        timeSlots.add(timeSlot1);
                    }
                }
                break;

            case TimeSlot:
                timeSlots = timeTable.getTimeSlots().getAllValues();
                break;

            default:
                break;

        }

        return timeSlots;
    }

    private ArrayList<? extends Keyable> getClassRooms(TimeTable timeTable, Representable representation, EntityObject entityObject) {
        ArrayList<ClassRoom> classRooms = new ArrayList<ClassRoom>();
        ArrayList<ClassRoom> subClassRooms;
        switch (this) {
            case Course:
                subClassRooms = getClassRoom(representation, (Course) entityObject);
                classRooms.addAll(subClassRooms);
                break;

            case Teacher:
                Teacher teacher = (Teacher) entityObject;
                for (Course course : teacher.getCourses()) {
                    subClassRooms = getClassRoom(representation, (Course) course);
                    classRooms.addAll(subClassRooms);
                }
                break;

            case Student:
                Student student = (Student) entityObject;
                for (Course course : student.getCourses()) {
                    subClassRooms = getClassRoom(representation, (Course) course);
                    classRooms.addAll(subClassRooms);
                }
                break;

            case ClassRoom:
                classRooms = timeTable.getClassRooms().getAllValues();
                break;

            case TimeSlot:
                TimeSlot timeSlot = (TimeSlot) entityObject;
                for (ClassRoom classRoom1 : timeTable.getClassRooms()) {
                    if (representation.get(timeSlot, classRoom1) != null) {
                        classRooms.add(classRoom1);
                    }
                }
                break;

            default:
                break;

        }

        return classRooms;
    }

    private ArrayList<TimeSlot> getTimeSlot(Representable representation, Course course) {
        ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
        for (CourseLesson courseLesson : course.getCourseLessons()) {
            Keys<TimeSlot, ClassRoom> keys = representation.getKeys(courseLesson);
            timeSlots.add(keys.getK1());
        }
        return timeSlots;
    }

    private ArrayList<ClassRoom> getClassRoom(Representable representation, Course course) {
        ArrayList<ClassRoom> classRooms = new ArrayList<ClassRoom>();
        for (CourseLesson courseLesson : course.getCourseLessons()) {
            Keys<TimeSlot, ClassRoom> keys = representation.getKeys(courseLesson);
            classRooms.add(keys.getK2());
        }
        return classRooms;
    }
}

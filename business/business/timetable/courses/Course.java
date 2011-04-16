/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.courses;

import business.timetable.EntityObject;
import business.timetable.constraints.AttributeDType;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.department.Department;
import business.timetable.semester.Semester;
import business.timetable.students.Students;
import business.timetable.teachers.Teacher;
import business.timetable.teachers.Teachers;
import business.timetable.timeslots.Time;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Course extends EntityObject<Course> {

    private int ID;
    private String name;
    private String section;
    private int capacity;
    private Time duration;
    private int lessonCount;
    private Teachers teachers;
    private Students students;
    private Department department;
    private Semester semester;
    private ArrayList<CourseLesson> courseLessons;

    Course(int ID, String name, String section, int capacity, String duration, int lessonCount,
            Department department, Semester semester,
            HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValProperties) {
        super(properties, multiValProperties);
        setID(ID);
        setName(name);
        setSection(section);
        setCapacity(capacity);
        this.duration = new Time(duration);
        setLessonCount(lessonCount);
        setDepartment(department);
        setSemester(semester);
        students = new Students();
        teachers = new Teachers();
        courseLessons = new ArrayList<CourseLesson>();

        for (int i = 0; i < lessonCount; i++) {
            CourseLesson courseLesson = new CourseLesson(getID() + "," + i, i, this);
            courseLessons.add(i, courseLesson);
        }
    }

    public Integer getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name + " (" + getSection() + ")";
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSection() {
        return section;
    }

    public void setSection(String section) {
        this.section = section;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Time getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration.setTime(duration);
    }

    public int getLessonCount() {
        return lessonCount;
    }

    public void setLessonCount(int lessonCount) {
        this.lessonCount = lessonCount;
    }

    public Teachers getTeachers() {
        return teachers;
    }

    public void addTeacher(Teacher teacher) {
        this.teachers.put(teacher.getID(), teacher);
    }

    public Students getStudents() {
        return students;
    }

    public void setStudents(Students students) {
        this.students = students;
    }

    public ArrayList<CourseLesson> getCourseLessons() {
        return courseLessons;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public CourseLesson getCourseLessonAt(int index) {
        return courseLessons.get(index);
    }


    @Override
    public String getProperty(String propertyName) {
        if (propertyName.equals("ID")) {
            return getID().toString();
        } else if (propertyName.equals("name")) {
            return getName();
        } else if (propertyName.equals("section")) {
            return String.valueOf(getSection());
        } else if (propertyName.equals("capacity")) {
            return String.valueOf(getCapacity());
        } else if (propertyName.equals("duration")) {
            return getDuration().toString();
        } else if (propertyName.equals("lessoncount")) {
            return String.valueOf(getLessonCount());
        } else {
            return super.getProperty(propertyName);
        }
    }

    @Override
    public String toString() {
        return getName() + " " + getSection();
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
        final Course other = (Course) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }

    public int compareTo(Course o) {
        if (ID > o.ID) {
            return 1;
        }
        if (ID < o.ID) {
            return -1;
        }
        return 0;
    }

    public static Course createCourse(HashMap<String, Object> tokens, Department department, Semester semester) {
        return new Course((Integer) tokens.get("ID")
                        , (String) tokens.get("name"), (String) tokens.get("section")
                        , (Integer) tokens.get("capacity"), (String) tokens.get("duration")
                        , (Integer) tokens.get("lessoncount")
                        , department, semester, (HashMap<String, String>) tokens.get("properties")
                        , (HashMap<String, ArrayList<Integer>>) tokens.get("multivalueproperties"));
    }

    public static ArrayList<ConstraintProperty> getDefualtConstProperties(Session session) throws BasicException {
        ArrayList<ConstraintProperty> result = new ArrayList<ConstraintProperty>();
        AttributeDType attributeDType = AttributeDType.getAttributeDType(session, "Integer");
        ConstraintProperty cp = new ConstraintProperty("capacity", attributeDType);
        result.add(cp);

        attributeDType = AttributeDType.getAttributeDType(session, "Time");
        cp = new ConstraintProperty("duration", attributeDType);
        result.add(cp);

        return result;
    }
}

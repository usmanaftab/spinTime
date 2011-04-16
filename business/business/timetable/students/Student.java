/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.students;

import business.timetable.EntityObject;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.courses.Courses;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Student extends EntityObject<Student> {

    private String ID;
    private String name;
    private Courses courses;

    Student(String code, String name, Courses courses, HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValProperties) {
        super(properties, multiValProperties);
        setCode(code);
        setName(name);
        setCourses(courses);
    }

    public String getID() {
        return ID;
    }

    public void setCode(String code) {
        this.ID = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Courses getCourses() {
        return courses;
    }

    public void setCourses(Courses courses) {
        this.courses = courses;
    }

    @Override
    public String getProperty(String propertyName) {
        if (propertyName.equals("ID")) {
            return getID().toString();
        } else if (propertyName.equals("name")) {
            return getName();
        } else {
            return super.getProperty(propertyName);
        }
    }

    @Override
    public String toString() {
        return ID;
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
        final Student other = (Student) obj;
        if ((this.ID == null) ? (other.ID != null) : !this.ID.equals(other.ID)) {
            return false;
        }
        return true;
    }

    public int compareTo(Student o) {
        return ID.compareTo(o.ID);
    }

    public static Student createStudent(HashMap<String, Object> tokens, Courses courses) {
        return new Student((String) tokens.get("ID"), (String) tokens.get("name"), courses
                        , (HashMap<String, String>) tokens.get("properties")
                        , (HashMap<String, ArrayList<Integer>>) tokens.get("multivalueproperties"));
    }

    public static ArrayList<ConstraintProperty> getDefualtConstProperties(Session session) {
        return null;
    }
}


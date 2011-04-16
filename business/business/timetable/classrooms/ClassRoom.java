/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.classrooms;

import business.timetable.EntityObject;
import business.timetable.constraints.AttributeDType;
import business.timetable.constraints.ConstraintProperty;
import business.timetable.department.Department;
import business.timetable.representation.Keyable;
import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ClassRoom extends EntityObject<ClassRoom> implements Keyable<Integer> {

    private int ID;
    private String name;
    private int capacity;
    private Department department;
    private Semester semester;

    ClassRoom(int ID, String name, int capacity, Department department, Semester semester,
            HashMap<String, String> properties, HashMap<String, ArrayList<Integer>> multiValProperties) {
        super(properties, multiValProperties);
        setID(ID);
        setName(name);
        setCapacity(capacity);
        setDepartment(department);
        setSemester(semester);
    }

    public Integer getID() {
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
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

    @Override
    public String getProperty(String propertyName) {
        if(propertyName.equals("ID")){
            return getID().toString();
        } else if( propertyName.equals("capacity")){
            return String.valueOf(getCapacity());
        } else {
            return super.getProperty(propertyName);
        }
    }

    @Override
    public String toString() {
        return getName();
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
        final ClassRoom other = (ClassRoom) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }

    public int compareTo(ClassRoom o) {
        if (ID > o.ID) {
            return 1;
        }
        if (ID < o.ID) {
            return -1;
        }
        return 0;
    }

    public static ClassRoom createClassRoom(HashMap<String, Object> tokens, Department department, Semester semester){
        return new ClassRoom((Integer) tokens.get("ID"), (String) tokens.get("name")
                        , (Integer) tokens.get("capacity"), department, semester
                        , (HashMap<String, String>) tokens.get("properties")
                        , (HashMap<String, ArrayList<Integer>>) tokens.get("multivalueproperties"));
    }

    public static ArrayList<ConstraintProperty> getDefualtConstProperties(Session session) throws BasicException {
        ArrayList<ConstraintProperty> result = new ArrayList<ConstraintProperty>();
        AttributeDType attributeDType = AttributeDType.getAttributeDType(session, "Integer");
        ConstraintProperty cp = new ConstraintProperty("capacity", attributeDType);
        result.add(cp);

        return result;
    }
}

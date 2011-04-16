/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.teacher;

import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import dal.timetable.db.representation.RepresentationDataLogic;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TeacherDataLogic extends EntityDataLogic {

    private String selectQuery;
    private PreparedStatement insertTeacherPS;
    private PreparedStatement insertTeacherSecPS;

    public TeacherDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT t.ID, t.name, cs.ID, cs.department_ID, cs.semester_ID";
        selectQuery += " FROM teacher t, teacher_course_section tcs, course c, course_section cs";
        selectQuery += " WHERE t.ID = tcs.teacher_ID";
        selectQuery += " AND tcs.course_section_ID = cs.ID";
        selectQuery += " AND cs.course_ID = c.ID";

        try {
            String sql = "INSERT INTO teacher (name) VALUES (?)";
            insertTeacherPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "INSERT INTO teacher_course_section (teacher_ID, course_section_ID, semester_ID) VALUES (?, ?, ?);";
            insertTeacherSecPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }

    private HashMap<Integer, HashMap<String, Object>> getResult(String sql) throws BasicException {
        try {
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, ArrayList<String>> teachers = new HashMap<Integer, ArrayList<String>>();
            HashMap<Integer, ArrayList<Integer>> courses = new HashMap<Integer, ArrayList<Integer>>();
            HashMap<Integer, ArrayList<Integer>> departments = new HashMap<Integer, ArrayList<Integer>>();
            HashMap<Integer, Integer> semester = new HashMap<Integer, Integer>();
            while (rs.next()) {
                int ID = rs.getInt("t.ID");
                if (teachers.get(ID) == null) {
                    ArrayList<String> teacher = new ArrayList<String>();
                    teacher.add(rs.getString("t.name"));
                    teachers.put(ID, teacher);
                    courses.put(ID, new ArrayList<Integer>());
                    departments.put(ID, new ArrayList<Integer>());
                    semester.put(ID, rs.getInt("cs.semester_ID"));
                }

                int courseID = rs.getInt("cs.ID");
                if (!courses.get(ID).contains(courseID)) {
                    courses.get(ID).add(rs.getInt("cs.ID"));
                }

                int departmentID = rs.getInt("cs.department_ID");
                if (!departments.get(ID).contains(departmentID)) {
                    departments.get(ID).add(departmentID);
                }
            }

            HashMap<String, Object> teacherEntity = getEntity("Teacher");
            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            for (int ID : teachers.keySet()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", ID);
                result.put("name", teachers.get(ID).get(0));
                result.put("courselist", courses.get(ID));

                result.put("properties", getProperties((Integer) teacherEntity.get("ID"), String.valueOf(ID)));
                result.put("multivalueproperties", getMultiValProperties((Integer) teacherEntity.get("ID"), String.valueOf(ID)));
                result.put("previoustimeslots", getPreviousTimeSlots(departments.get(ID).toArray(), semester.get(ID), ID));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getData(Object[] departmentIDs, int semesterID) throws BasicException {

        String sql = selectQuery;
        if (departmentIDs.length > 0) {
            sql += " AND cs.department_ID IN (" + getIntegerList(departmentIDs) + ")";
        }
        sql += " AND cs.semester_ID = " + semesterID;
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getData() throws BasicException {
        return getResult(selectQuery);
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = selectQuery;
        if (IDs.length > 0) {
            sql += " AND t.ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public int isAlreadyPresent(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "SELECT ID, name FROM teacher WHERE name = '" + data.get("name") + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getInt("ID");
            }

            return -1;
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }

    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            String name = (String) data.get("name");
            insertTeacherPS.setString(1, name);
            insertTeacherPS.execute();
            return (Integer) getLastInsertValue(insertTeacherPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }

    public int insertTeacherCourse(HashMap<String, Object> data) throws BasicException {
        try {
            insertTeacherSecPS.setInt(1, (Integer) data.get("ID"));
            insertTeacherSecPS.setInt(2, (Integer) data.get("courseID"));
            insertTeacherSecPS.setInt(3, (Integer) data.get("semesterID"));
            insertTeacherSecPS.execute();
            return (Integer) getLastInsertValue(insertTeacherSecPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }

    private ArrayList<Integer> getPreviousTimeSlots(Object[] departments, int semesterID, int teacherID) throws BasicException {
        RepresentationDataLogic repDataLogic = new RepresentationDataLogic(getSession());
        HashMap<Integer, HashMap<String, Object>> data = repDataLogic.getTeacherPrevData(departments, semesterID, teacherID);
        ArrayList<Integer> timeSlotIDs = new ArrayList<Integer>();
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            timeSlotIDs.add((Integer) tokens.get("timeslotID"));
        }

        return timeSlotIDs;
    }

    public void deleteSemesterData(int semesterID) throws BasicException {
        try {
            String sql = "DELETE FROM teacher_course_section WHERE semester_ID = " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(TeacherDataLogic.class.getName(), ex);
        }
    }
}

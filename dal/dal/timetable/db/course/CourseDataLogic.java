/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.course;

import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class CourseDataLogic extends EntityDataLogic {

    private String selectQuery;
    private PreparedStatement insertPS;
    private PreparedStatement updatePS;
    private PreparedStatement insertCourseSecPS;

    public CourseDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT cs.ID, c.name, cs.section_name, c.capacity, c.duration, c.lesson_count,";
        selectQuery += " cs.department_ID, cs.semester_ID";
        selectQuery += " FROM course c, course_section cs";
        selectQuery += " WHERE c.ID = cs.course_ID";

        try {
            String sql = "INSERT INTO course (ID, name, duration, capacity, lesson_count) VALUES (?, ?, ?, ?, ?)";
            insertPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "UPDATE course SET ID = ?, name = ?, duration = ?, capacity = ?, lesson_count = ?";
            sql += " WHERE ID = ?";
            updatePS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "INSERT INTO course_section (semester_ID, department_ID, course_ID, section_name) VALUES (?, ?, ?, ?)";
            insertCourseSecPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    private HashMap<Integer, HashMap<String, Object>> getResult(String sql) throws BasicException {
        try {
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("cs.ID"));
                result.put("name", rs.getString("c.name"));
                result.put("section", rs.getString("cs.section_name"));
                result.put("capacity", rs.getInt("c.capacity"));
                result.put("duration", rs.getString("c.duration"));
                result.put("lessoncount", rs.getInt("c.lesson_count"));
                result.put("departmentID", rs.getInt("cs.department_ID"));
                result.put("semesterID", rs.getInt("cs.semester_ID"));

                result.put("properties", getProperties(3, rs.getString("cs.ID")));
                result.put("multivalueproperties", getMultiValProperties(3, rs.getString("cs.ID")));

                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getData(Object[] departmentIDs, int semesteID) throws BasicException {

        String sql = selectQuery;
        if (departmentIDs.length > 0) {
            sql += " AND cs.department_ID IN (" + getIntegerList(departmentIDs) + ")";
        }
        sql += " AND cs.semester_ID = " + semesteID;
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getData() throws BasicException {
        return getResult(selectQuery);
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = selectQuery;
        if (IDs.length > 0) {
            sql += " AND cs.ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public String isAlreadyExist(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "SELECT ID, name, capacity, duration, lesson_count FROM course";
            sql += " WHERE ID = '" + data.get("ID") + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getString("ID");
            }
            return null;
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public void setParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            String ID = String.valueOf(data.get("ID"));
            String name = (String) data.get("name");
            int lessonCount = (Integer) data.get("lessoncount");
            int capacity = (Integer) data.get("capacity");
            String duration = (String) data.get("duration");

            ps.setString(1, ID);
            ps.setString(2, name);
            ps.setString(3, duration);
            ps.setInt(4, capacity);
            ps.setInt(5, lessonCount);

        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public void update(HashMap<String, Object> data) throws BasicException {
        try {
            setParams(updatePS, data);
            updatePS.setString(6, (String) data.get("ID"));
            updatePS.execute();
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public String insert(HashMap<String, Object> data) throws BasicException {
        try {
            setParams(insertPS, data);
            insertPS.execute();
            return (String) data.get("ID");
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public int insertCourseSection(HashMap<String, Object> data) throws BasicException {
        try {
            String ID = String.valueOf(data.get("ID"));
            String section = (String) data.get("section");
            int deptID = (Integer) data.get("departmentID");
            int semesterID = (Integer) data.get("semesterID");

            insertCourseSecPS.setInt(1, semesterID);
            insertCourseSecPS.setInt(2, deptID);
            insertCourseSecPS.setString(3, ID);
            insertCourseSecPS.setString(4, section);
            insertCourseSecPS.execute();
            return (Integer) getLastInsertValue(insertCourseSecPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public HashMap<String, Object> getCourseSectionRow(HashMap<String, Object> data) throws BasicException {
        try {
            String courseID = String.valueOf(data.get("ID"));
            String section = String.valueOf(data.get("section"));
            int deptID = (Integer) data.get("departmentID");
            int semesterID = (Integer) data.get("semesterID");


            String sql = "SELECT cs.ID, c.name, cs.section_name, c.capacity, c.duration, c.lesson_count";
            sql += " FROM course c, course_section cs";
            sql += " WHERE c.ID = cs.course_ID";
            sql += " AND c.ID = '" + courseID + "'";
            sql += " AND section_name = '" + section + "'";
            sql += " AND cs.department_ID = " + deptID;
            sql += " AND cs.semester_ID = " + semesterID;

            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<String, Object> result = null;
            if (rs.next()) {
                result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("cs.ID"));
                result.put("name", rs.getString("c.name"));
                result.put("section", rs.getString("cs.section_name"));
                result.put("capacity", rs.getInt("c.capacity"));
                result.put("duration", rs.getString("c.duration"));
                result.put("lessoncount", rs.getInt("c.lesson_count"));
            }
            return result;
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }

    public void deleteCourseSection(int semesterID) throws BasicException {
        try {
            String sql = "DELETE FROM course_section WHERE semester_ID = " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(CourseDataLogic.class.getName(), ex);
        }
    }
}

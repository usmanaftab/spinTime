/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.student;

import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
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
public class StudentDataLogic extends EntityDataLogic {

    private String selectQuery;
    private PreparedStatement insertStudentPS;
    private PreparedStatement insertStudentSecPS;

    public StudentDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT st.ID, st.name, cs.ID, scs.semester_ID";
        selectQuery += " FROM student st, student_course_section scs, course c, course_section cs";
        selectQuery += " WHERE st.ID = scs.student_ID";
        selectQuery += " AND scs.course_section_ID = cs.ID";
        selectQuery += " AND cs.course_ID = c.ID";

        try {
            String sql = "INSERT INTO student (ID) VALUES (?)";
            insertStudentPS = getSession().getConnection().prepareStatement(sql);

            sql = "INSERT INTO student_course_section (student_ID, course_section_ID, semester_ID) VALUES (?, ?, ?)";
            insertStudentSecPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
        }
    }

    private HashMap<Integer, HashMap<String, Object>> getResult(String sql) throws BasicException {
        try {
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<String, String> students = new HashMap<String, String>();
            HashMap<String, ArrayList<Integer>> courses = new HashMap<String, ArrayList<Integer>>();
            while (rs.next()) {
                String ID = rs.getString("st.ID");
                if (students.get(ID) == null) {
                    students.put(ID, rs.getString("st.name"));
                    courses.put(ID, new ArrayList<Integer>());
                }

                courses.get(ID).add(rs.getInt("cs.ID"));
            }

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            for (String ID : students.keySet()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", ID);
                result.put("name", students.get(ID));
                result.put("courselist", courses.get(ID));

                result.put("properties", getProperties(1, String.valueOf(ID)));
                result.put("multivalueproperties", getMultiValProperties(1, String.valueOf(ID)));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
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
            sql += " AND st.ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public boolean isAlreadyExist(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "SELECT ID, name FROM student WHERE ID = '" + data.get("ID") + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
        }
    }

    public String insert(HashMap<String, Object> data) throws BasicException {
        try {
            String ID = String.valueOf(data.get("ID"));
            insertStudentPS.setString(1, ID);
            insertStudentPS.execute();
            return ID;
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
        }
    }

    public int insertStudentCourse(HashMap<String, Object> data) throws BasicException {
        try {
            insertStudentSecPS.setString(1, (String) data.get("ID"));
            insertStudentSecPS.setInt(2, (Integer) data.get("coursesectionID"));
            insertStudentSecPS.setInt(3, (Integer) data.get("semesterID"));
            insertStudentSecPS.execute();
            return (Integer) getLastInsertValue(insertStudentSecPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
        }
    }

    public void deleteStudentSections(int semesterID) throws BasicException {
        try {
            String sql = "DELETE FROM student_course_section WHERE semester_ID = " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(StudentDataLogic.class.getName(), ex);
        }
    }
}

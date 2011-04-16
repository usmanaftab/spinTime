/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.representation;

import com.timetable.BasicException;
import dal.timetable.db.DataLogic;
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
public class RepresentationDataLogic extends DataLogic {

    private String selectQuery;
    private String deleteQuery;
    private PreparedStatement insertPS;

    public RepresentationDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT ID, course_section_ID, lesson_no, teacher_ID, classroom_ID, timeslot_ID";
        selectQuery += ", semester_ID, department_ID FROM timetable";

        deleteQuery = "DELETE FROM timetable";

        try {
            String sql = "INSERT INTO timetable (semester_ID, department_ID, course_section_ID, lesson_no, teacher_ID, classroom_ID, timeslot_ID)";
            sql += " VALUES (?, ?, ?, ?, ?, ?, ?)";
            insertPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
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
                result.put("ID", rs.getInt("ID"));
                result.put("courseID", rs.getInt("course_section_ID"));
                result.put("lessonno", rs.getInt("lesson_no"));
                result.put("teacherID", rs.getInt("teacher_ID"));
                result.put("classroomID", rs.getInt("classroom_ID"));
                result.put("timeslotID", rs.getInt("timeslot_ID"));
                result.put("semesterID", rs.getInt("semester_ID"));
                result.put("departmentID", rs.getInt("department_ID"));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getData(Object[] departmentIDs, int semesterID) throws BasicException {
        String sql = selectQuery;
        sql += " WHERE semester_ID = " + semesterID;
        if (departmentIDs.length > 0) {
            sql += " AND department_ID IN (" + getIntegerList(departmentIDs) + ")";
        }
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = selectQuery;
        if (IDs.length > 0) {
            sql += " Where ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public void insert(Object[] data) throws BasicException {
        try {
            int semesterID = Integer.parseInt(String.valueOf(data[0]));
            int departmentID = Integer.parseInt(String.valueOf(data[1]));
            int courseSecID = Integer.parseInt(String.valueOf(data[2]));
            int lessonNo = Integer.parseInt(String.valueOf(data[3]));
            int teacherID = Integer.parseInt(String.valueOf(data[4]));
            int classRoomID = Integer.parseInt(String.valueOf(data[5]));
            int timeSlotID = Integer.parseInt(String.valueOf(data[6]));

            insertPS.setInt(1, semesterID);
            insertPS.setInt(2, departmentID);
            insertPS.setInt(3, courseSecID);
            insertPS.setInt(4, lessonNo);
            insertPS.setInt(5, teacherID);
            insertPS.setInt(6, classRoomID);
            insertPS.setInt(7, timeSlotID);

            insertPS.execute();
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
        }

    }

    public void deleteTimeTable(int semesterID) throws BasicException {
        try {
            String sql = deleteQuery;
            sql += " WHERE semester_ID = + " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
        }
    }

    public void deleteTimeTable(Object[] departmentIDs, int semesterID) throws BasicException {
        try {
            String sql = deleteQuery;
            sql += " WHERE semester_ID = + " + semesterID;
            if (departmentIDs.length > 0) {
                sql += " AND department_ID IN (" + getIntegerList(departmentIDs) + ")";
            }
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getTeacherPrevData(Object[] departmentIDs, int semesterID, int teacherID) throws BasicException {
        try {
            String sql = selectQuery;
            sql += " WHERE semester_ID = " + semesterID;
            if (departmentIDs.length > 0) {
                sql += " AND department_ID NOT IN (" + getIntegerList(departmentIDs) + ")";
            }
            sql += " AND teacher_ID = " + teacherID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();

                result.put("courseID", rs.getInt("course_section_ID"));
                result.put("lessonno", rs.getInt("lesson_no"));
                result.put("timeslotID", rs.getInt("timeslot_ID"));
                result.put("classroomID", rs.getInt("classroom_ID"));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(RepresentationDataLogic.class.getName(), ex);
        }
    }
}

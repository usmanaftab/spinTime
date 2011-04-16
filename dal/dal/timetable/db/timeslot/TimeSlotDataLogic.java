/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.timeslot;

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
public class TimeSlotDataLogic extends EntityDataLogic {

    private String selectQuery;
    private PreparedStatement insertPS;
    private PreparedStatement insertTSSemesterPS;

    public TimeSlotDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT t.ID, t.timeslot_day, t.begintime, t.endtime, ts.semester_ID FROM timeslot t, timeslot_semester ts";
        selectQuery += " WHERE t.ID = ts.timeslot_ID";

        try {
            String sql = "INSERT INTO timeslot (timeslot_day, begintime, endtime) VALUES (?, ?, ?)";
            insertPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "INSERT INTO timeslot_semester (timeslot_ID, semester_ID) VALUES (?, ?)";
            insertTSSemesterPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
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
                result.put("ID", rs.getInt("t.ID"));
                result.put("day", rs.getString("t.timeslot_day"));
                result.put("begintime", rs.getString("t.begintime"));
                result.put("endtime", rs.getString("t.endtime"));
                result.put("semester_ID", rs.getInt("ts.semester_ID"));

                result.put("properties", getProperties(3, rs.getString("t.ID")));
                result.put("multivalueproperties", getMultiValProperties(3, rs.getString("t.ID")));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = selectQuery;
        if (IDs.length > 0) {
            sql += " AND t.ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getData(int semesterID) throws BasicException {
        String sql = selectQuery;
        sql += " AND semester_ID = " + semesterID;
        return getResult(selectQuery);
    }

    public int isAlreadyExist(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "SELECT ID, timeslot_day, begintime, endtime FROM timeslot WHERE timeslot_day = '" + data.get("day") + "'";
            sql += " AND begintime = '" + data.get("begintime") + "'";
            sql += " AND endtime = '" + data.get("endtime") + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getInt("ID");
            }

            return -1;
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }

    private void setParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            ps.setString(1, (String) data.get("day"));
            ps.setString(2, (String) data.get("begintime"));
            ps.setString(3, (String) data.get("endtime"));
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }

    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            setParams(insertPS, data);
            insertPS.execute();
            return (Integer) getLastInsertValue(insertPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }

    public void insertTimeslotSemester(HashMap<String, Object> data) throws BasicException {
        try {
            insertTSSemesterPS.setInt(1, (Integer) data.get("timeslotID"));
            insertTSSemesterPS.setInt(2, (Integer) data.get("semesterID"));
            insertTSSemesterPS.execute();
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }

    public void deleteSemesterData(int semesterID) throws BasicException {
        try {
            String sql = "DELETE FROM timeslot_semester WHERE semester_ID = " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(TimeSlotDataLogic.class.getName(), ex);
        }
    }
}

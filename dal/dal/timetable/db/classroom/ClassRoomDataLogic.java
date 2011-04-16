/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.classroom;

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
public class ClassRoomDataLogic extends EntityDataLogic {

    private String selectQuery;
    private PreparedStatement insertPS;
    private PreparedStatement updatePS;
    private PreparedStatement insertClassRoomDept;

    public ClassRoomDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT cr.ID, cr.name, cr.capacity, crd.department_ID, crd.semester_ID";
        selectQuery += " FROM classroom cr, classroom_department crd";
        selectQuery += " WHERE cr.ID = crd.classroom_ID";

        try {
            String sql = "INSERT INTO classroom (name, capacity) VALUES (?, ?)";
            insertPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "INSERT INTO classroom_department (classroom_ID, department_ID, semester_ID) VALUES (?, ?, ?)";
            insertClassRoomDept = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

            sql = "UPDATE classroom SET name = ?, capacity = ? WHERE name = ?";
            updatePS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
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
                result.put("ID", rs.getInt("cr.ID"));
                result.put("name", rs.getString("cr.name"));
                result.put("capacity", rs.getInt("cr.capacity"));
                result.put("departmentID", rs.getInt("crd.department_ID"));
                result.put("semesterID", rs.getInt("crd.semester_ID"));

                result.put("properties", getProperties(4, rs.getString("cr.ID")));
                result.put("multivalueproperties", getMultiValProperties(4, rs.getString("cr.ID")));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getData(Object[] departmentIDs, int semesterID) throws BasicException {
        String sql = selectQuery;
        if (departmentIDs.length > 0) {
            sql += " AND crd.department_ID IN (" + getIntegerList(departmentIDs) + ")";
        }
        sql += " AND crd.semester_ID  = " + semesterID;
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getData() throws BasicException {
        return getResult(selectQuery);
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = "SELECT cr.ID, cr.name, cr.capacity, crd.department_ID, crd.semester_ID";
        sql += " FROM classroom cr, classroom_department crd";
        sql += " WHERE cr.ID = crd.classroom_ID";
        if (IDs.length > 0) {
            sql += " AND cr.ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public int isAlreadyPresent(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "SELECT ID, name, capacity FROM classroom ";
            sql += " WHERE name = '" + data.get("name") + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getInt("ID");
            }

            return -1;
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    private void setParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            ps.setString(1, (String) data.get("name"));
            if (data.get("capacity") != null) {
                ps.setInt(2, (Integer) data.get("capacity"));
            } else {
                ps.setInt(2, 50);
            }
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            setParams(insertPS, data);
            insertPS.execute();
            return (Integer) getLastInsertValue(insertPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    public void update(HashMap<String, Object> data) throws BasicException {
        try {
            setParams(updatePS, data);
            updatePS.setString(3, (String) data.get("name"));
            updatePS.execute();
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    public int insertClassRoomDept(HashMap<String, Object> data) throws BasicException {
        try {
            insertClassRoomDept.setInt(1, (Integer) data.get("classroomID"));
            insertClassRoomDept.setInt(2, (Integer) data.get("departmentID"));
            insertClassRoomDept.setInt(3, (Integer) data.get("semesterID"));
            insertClassRoomDept.execute();
            return (Integer) getLastInsertValue(insertClassRoomDept, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }

    public void deleteSemesterData(int semesterID) throws BasicException {
        try {
            String sql = "DELETE FROM classroom_department WHERE semester_ID = " + semesterID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(ClassRoomDataLogic.class.getName(), ex);
        }
    }
}

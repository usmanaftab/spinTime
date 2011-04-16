/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.semester;

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
public class SemesterDataLogic extends DataLogic {

    private String selectQuery;
    private PreparedStatement insertPS;

    public SemesterDataLogic(Session session) throws BasicException {
        super(session);

        selectQuery = "SELECT ID, name FROM semester";

        try {
            String sql = "INSERT INTO semester (name) VALUES (?)";
            insertPS = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        } catch (SQLException ex) {
            throw new BasicException(SemesterDataLogic.class.getName(), ex);
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
                result.put("name", rs.getString("name"));

                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(SemesterDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getDataByIDs(Object[] IDs) throws BasicException {
        String sql = selectQuery;
        if (IDs.length > 0) {
            sql += " WHERE ID IN (" + getIntegerList(IDs) + ")";
        }
        return getResult(sql);
    }

    public HashMap<String, Object> getDataByName(String semesterName) throws BasicException {
        String sql = selectQuery;
        sql += " WHERE name = '" + semesterName + "'";
        HashMap<Integer, HashMap<String, Object>> result = getResult(sql);
        if (result.size() > 0) {
            return result.get(0);
        }

        return null;
    }

    public HashMap<Integer, HashMap<String, Object>> getData() throws BasicException {
        return getResult(selectQuery);
    }

    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            String name = (String) data.get("name");
            insertPS.setString(1, name);
            insertPS.execute();
            return (Integer) getLastInsertValue(insertPS, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(SemesterDataLogic.class.getName(), ex);
        }
    }

    public Integer getID(String name) throws BasicException {
        try {
            String sql = selectQuery;
            sql += " WHERE name = '" + name + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                return rs.getInt("ID");
            }
            return null;
        } catch (SQLException ex) {
            throw new BasicException(SemesterDataLogic.class.getName(), ex);
        }
    }
}

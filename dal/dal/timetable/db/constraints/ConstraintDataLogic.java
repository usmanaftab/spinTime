/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.constraints;

import com.timetable.BasicException;
import dal.timetable.db.DataLogic;
import dal.timetable.db.Session;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ConstraintDataLogic extends DataLogic {

    private String selectAttribType;

    public ConstraintDataLogic(Session session) {
        super(session);

        selectAttribType = "SELECT ID, name FROM constraint_attribute_type";
    }

    private HashMap<Integer, HashMap<String, Object>> getAttibuteDTypesResult(String sql) throws BasicException {
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
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getAttributeDTypes() throws BasicException {
        return getAttibuteDTypesResult(selectAttribType);
    }

    public HashMap<String, Object> getAttributeDType(String name) throws BasicException {
        String sql = selectAttribType;
        sql += " WHERE name = '" + name + "'";
        return getAttibuteDTypesResult(sql).get(0);
    }

    public HashMap<Integer, HashMap<String, Object>> getConstTypeOperators(String constraintTypeName) throws BasicException {
        try {
            String sql = "SELECT conto.ID, conto.name, conto.operator_sign, cont.ID, cont.name";
            sql += " FROM constraint_type_operator conto, constraint_type cont";
            sql += " WHERE conto.constraint_type_ID = cont.ID";
            sql += " AND cont.name = '" + constraintTypeName + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("ID"));
                result.put("name", rs.getString("name"));
                result.put("sign", rs.getString("operator_sign"));
                result.put("constrainttypeID", rs.getInt("cont.ID"));
                result.put("constrainttypename", rs.getString("cont.name"));
                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    public HashMap<String, Object> getConstraintType(String name) throws BasicException {
        try {
            String sql = "SELECT ID, name FROM constraint_type WHERE name = '" + name + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<String, Object> result = new HashMap<String, Object>();
            if (rs.next()) {
                result.put("ID", rs.getInt("ID"));
                result.put("name", rs.getString("name"));
            }
            return result;
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getTimeSlotType(int constraintTypeID) throws BasicException {
        try {
            String sql = "SELECT ID, constraint_type_ID, name FROM constraint_timeslot_type ";
            sql += "WHERE constraint_type_ID = " + constraintTypeID;
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("ID"));
                result.put("name", rs.getString("name"));
                result.put("constrainttypeID", rs.getInt("constraint_type_ID"));
                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    private void setParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            ps.setString(1, (String) data.get("name"));
            ps.setBoolean(2, (Boolean) data.get("apply"));
            ps.setBoolean(3, (Boolean) data.get("hardconstraint"));
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "INSERT INTO constrainttimetable (name, apply, ishard)";
            sql += " VALUES(?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(ps, data);
            ps.execute();
            return (Integer) getLastInsertValue(ps, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }

    public int update(HashMap<String, Object> data) throws BasicException {
        try {
            int ID = (Integer) data.get("ID");
            String sql = "UPDATE constrainttimetable SET name = ?, apply = ?, ishard = ? WHERE ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setParams(ps, data);
            ps.setInt(4, ID);
            ps.execute();
            return ID;
        } catch (SQLException ex) {
            throw new BasicException(ConstraintDataLogic.class.getName(), ex);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.constraints.gap;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.ConstraintDataLogic;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapConstraintDataLogic extends ConstraintDataLogic {

    private String selectQuery;

    public GapConstraintDataLogic(Session session) {
        super(session);

        /*
         * ID apply hardconstraint mingap maxgap minsuc maxsuc entityID entityname entitydisplay timeslottypeID timeslottypename
         */

        selectQuery = "SELECT con.ID, con.name, con.apply, con.ishard, conte.ID, cone.name, cone.display_name, contt.ID, contt.name,";
        selectQuery += " conte.position, gcon.mingap, gcon.maxgap, gcon.minsuc, gcon.maxsuc";
        selectQuery += " FROM constrainttimetable con, gap_constraint gcon, constraint_type_entity conte,";
        selectQuery += " constraint_entity cone, constraint_timeslot_type contt";
        selectQuery += " WHERE gcon.constrainttimetable_ID = con.ID";
        selectQuery += " AND gcon.constraint_type_entity_ID = conte.ID";
        selectQuery += " AND gcon.constraint_timeslot_type_ID = contt.ID";
        selectQuery += " AND conte.constraint_entity_ID = cone.ID";
    }

    public HashMap<Integer, HashMap<String, Object>> getResult(String sql) throws BasicException {
        try {
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("con.ID"));
                result.put("name", rs.getString("con.name"));
                result.put("hardconstraint", rs.getBoolean("con.ishard"));
                result.put("apply", rs.getBoolean("con.apply"));
                result.put("entityID", rs.getInt("conte.ID"));
                result.put("entityname", rs.getString("cone.name"));
                result.put("entitydisplayname", rs.getString("cone.display_name"));
                result.put("entityposition", rs.getInt("conte.position"));
                result.put("timeslottypeID", rs.getInt("contt.ID"));
                result.put("timeslottypename", rs.getString("contt.name"));
                result.put("mingap", rs.getInt("gcon.mingap"));
                result.put("maxgap", rs.getInt("gcon.maxgap"));
                result.put("minsuc", rs.getInt("gcon.minsuc"));
                result.put("maxsuc", rs.getInt("gcon.maxsuc"));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(GapConstraintDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getConstraints(boolean hard, boolean apply) throws BasicException {
        String sql = selectQuery;
        sql += " AND con.apply = " + apply;
        sql += " AND con.ishard = " + hard;
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getConstraints() throws BasicException {
        return getResult(selectQuery);
    }

    private void setParams(PreparedStatement ps, HashMap<String, Object> data, int constraintTimeTableID) throws BasicException {
        try {
            ps.setInt(1, constraintTimeTableID);
            ps.setInt(2, (Integer) data.get("constrainttypeentityID"));
            ps.setInt(3, (Integer) data.get("constrainttimeslottypeID"));
            if(data.get("mingap") == null){
                ps.setInt(4, -1);
            } else {
                ps.setInt(4, (Integer) data.get("mingap"));
            }
            if(data.get("maxgap") == null){
                ps.setInt(5, -1);
            } else {
                ps.setInt(5, (Integer) data.get("maxgap"));
            }
            if(data.get("minsuc") == null){
                ps.setInt(6, -1);
            } else {
                ps.setInt(6, (Integer) data.get("minsuc"));
            }
            if(data.get("maxsuc") == null){
                ps.setInt(7, -1);
            } else {
                ps.setInt(7, (Integer) data.get("maxsuc"));
            }
        } catch (SQLException ex) {
            throw new BasicException(GapConstraintDataLogic.class.getName(), ex);
        }
    }

    @Override
    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.insert(data);
            String sql = "INSERT INTO gap_constraint (constrainttimetable_ID, constraint_type_entity_ID,";
            sql += "  constraint_timeslot_type_ID, mingap, maxgap, minsuc, maxsuc) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(ps, data, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(GapConstraintDataLogic.class.getName(), ex);
        }
    }

    @Override
    public int update(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.update(data);
            String sql = "UPDATE gap_constraint SET constrainttimetable_ID = ?, constraint_type_entity_ID = ?,";
            sql +=  " constraint_timeslot_type_ID = ?, mingap = ?, maxgap = ?, minsuc = ?, maxsuc = ?";
            sql += " WHERE constrainttimetable_ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setParams(ps, data, constraintTimeTableID);
            ps.setInt(8, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(GapConstraintDataLogic.class.getName(), ex);
        }
    }
}

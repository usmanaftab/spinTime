/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.constraints.general;

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
public class GeneralConstraintDataLogic extends ConstraintDataLogic {

    private String selectQuery;

    public GeneralConstraintDataLogic(Session session) {
        super(session);

        /*
         * ID leftattribname rightattribname leftentityID leftentityname leftentitydisplayname rightentityID rightentityname rightentitydisplayname
         * attribdtypeID attribdtypename constrainttypeID constrainttypename optID optname optsign
         */

        selectQuery = "SELECT con.ID, con.name, con.apply, con.ishard, cont.ID, cont.name, conto.ID, conto.name, conto.operator_sign,";
        selectQuery += " left_conte.ID, left_cone.name, left_cone.display_name, right_conte.ID, right_cone.name, right_cone.display_name, conat.ID, conat.name,";
        selectQuery += " left_conte.position, right_conte.position, gcon.left_attribute_name, gcon.right_attribute_name";
        selectQuery += " FROM constrainttimetable con, general_constraint gcon, constraint_type cont, constraint_type_entity left_conte, constraint_type_entity right_conte,";
        selectQuery += " constraint_entity left_cone, constraint_entity right_cone, constraint_attribute_type conat, constraint_type_operator conto";
        selectQuery += " WHERE gcon.constrainttimetable_ID = con.ID";
        selectQuery += " AND gcon.constraint_type_operator_ID = conto.ID";
        selectQuery += " AND gcon.left_constraint_type_entity_ID = left_conte.ID";
        selectQuery += " AND gcon.right_constraint_type_entity_ID = right_conte.ID";
        selectQuery += " AND gcon.constraint_attribute_type_ID = conat.ID";
        selectQuery += " AND left_conte.constraint_entity_ID = left_cone.ID";
        selectQuery += " AND right_conte.constraint_entity_ID = right_cone.ID";
        selectQuery += " AND conto.constraint_type_ID = cont.ID";
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
                result.put("constrainttypeID", rs.getInt("cont.ID"));
                result.put("constrainttypename", rs.getString("cont.name"));
                result.put("leftentityID", rs.getInt("left_conte.ID"));
                result.put("leftentityname", rs.getString("left_cone.name"));
                result.put("leftentitydisplayname", rs.getString("left_cone.display_name"));
                result.put("leftentityposition", rs.getInt("left_conte.position"));
                result.put("rightentityID", rs.getInt("right_conte.ID"));
                result.put("rightentityname", rs.getString("right_cone.name"));
                result.put("rightentitydisplayname", rs.getString("right_cone.display_name"));
                result.put("rightentityposition", rs.getInt("right_conte.position"));
                result.put("optID", rs.getInt("conto.ID"));
                result.put("optname", rs.getString("conto.name"));
                result.put("optsign", rs.getString("conto.operator_sign"));
                result.put("attribdtypeID", rs.getInt("conat.ID"));
                result.put("attribdtypename", rs.getString("conat.name"));
                result.put("leftattribname", rs.getString("gcon.left_attribute_name"));
                result.put("rightattribname", rs.getString("gcon.right_attribute_name"));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(GeneralConstraintDataLogic.class.getName(), ex);
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
            ps.setInt(2, (Integer) data.get("constrainttypeoperatorID"));
            ps.setInt(3, (Integer) data.get("leftconstrainttypeentityID"));
            ps.setInt(4, (Integer) data.get("rightconstrainttypeentityID"));
            ps.setString(5, (String) data.get("leftattributename"));
            ps.setString(6, (String) data.get("rightattributename"));
            ps.setInt(7, (Integer) data.get("constraintattributetypeID"));
        } catch (SQLException ex) {
            throw new BasicException(GeneralConstraintDataLogic.class.getName(), ex);
        }
    }

    @Override
    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.insert(data);
            String sql = "INSERT INTO general_constraint (constrainttimetable_ID, constraint_type_operator_ID,";
            sql += " left_constraint_type_entity_ID, right_constraint_type_entity_ID, left_attribute_name,";
            sql += " right_attribute_name, constraint_attribute_type_ID )";
            sql += " VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(ps, data, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(GeneralConstraintDataLogic.class.getName(), ex);
        }
    }

    @Override
    public int update(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.update(data);
            String sql = "UPDATE general_constraint SET constrainttimetable_ID = ?, constraint_type_operator_ID = ?,";
            sql +=  " left_constraint_type_entity_ID = ?, right_constraint_type_entity_ID = ?, left_attribute_name = ?,";
            sql += " right_attribute_name = ?, constraint_attribute_type_ID = ?";
            sql += " WHERE constrainttimetable_ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setParams(ps, data, constraintTimeTableID);
            ps.setInt(8, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(GeneralConstraintDataLogic.class.getName(), ex);
        }
    }
}

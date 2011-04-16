/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db;

import com.timetable.BasicException;
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
public class EntityDataLogic extends DataLogic {

    public EntityDataLogic(Session session) {
        super(session);
    }

    public HashMap<Integer, HashMap<String, Object>> getConstraintEntities(String constraintName) throws BasicException {
        try {
            String sql = "SELECT contte.ID, contte.constraint_type_ID, contte.constraint_entity_ID, contte.position,";
            sql += " cont.name, conte.ID, conte.name, conte.display_name";
            sql += " FROM constraint_type_entity contte, constraint_type cont, constraint_entity conte";
            sql += " WHERE contte.constraint_type_ID = cont.ID";
            sql += " AND contte.constraint_entity_ID = conte.ID";
            sql += " AND cont.name = '" + constraintName + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("contte.ID"));
                result.put("contratinttypeID", rs.getInt("contte.constraint_type_ID"));
                result.put("position", rs.getInt("contte.position"));
                result.put("contrainttypename", rs.getString("cont.name"));
                result.put("contraintentityID", rs.getInt("conte.ID"));
                result.put("name", rs.getString("conte.name"));
                result.put("dispalyname", rs.getString("conte.display_name"));
                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public HashMap<String, Object> getEntity(String name) throws BasicException {
        try {
            String sql = "SELECT ID, name, display_name FROM constraint_entity  WHERE name = '" + name + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();
            if (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("ID"));
                result.put("name", rs.getString("name"));
                result.put("displayname", rs.getString("display_name"));
                return result;
            }
            return null;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getContraintTimeSlotTypes(String constraintName) throws BasicException {
        try {
            String sql = "SELECT contt.ID, contt.name, cont.ID, cont.name";
            sql += " FROM constraint_timeslot_type contt, constraint_type cont";
            sql += " WHERE contt.constraint_type_ID = cont.ID";
            sql += " AND cont.name = '" + constraintName + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while (rs.next()) {
                HashMap<String, Object> result = new HashMap<String, Object>();
                result.put("ID", rs.getInt("contt.ID"));
                result.put("name", rs.getString("contt.name"));
                result.put("constrainttypeID", rs.getInt("cont.ID"));
                result.put("contrainttypename", rs.getString("cont.name"));
                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getProperties() throws BasicException {
        try {
            String sql = "SELECT DISTINCT conp.attribute_name, conat.ID, conat.name, cone.ID, cone.name, cone.display_name";
            sql += " FROM constraint_property conp, constraint_entity cone, constraint_attribute_type conat";
            sql += " WHERE conp.constraint_entity_ID = cone.ID";
            sql += " AND conp.constraint_attribute_type_ID = conat.ID";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while(rs.next()){
                HashMap<String, Object> result = new HashMap<String, Object>();

                result.put("attributename", rs.getString("conp.attribute_name"));
                result.put("constraintattributetypeID", rs.getInt("conat.ID"));
                result.put("constraintattributename", rs.getString("conat.name"));
                result.put("constraintentityID", rs.getInt("cone.ID"));
                result.put("constraintentityname", rs.getString("cone.name"));
                result.put("constraintentitydisplayname", rs.getString("cone.display_name"));

                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    protected HashMap<String, String> getProperties(int entityID, String ID) throws BasicException {
        try {
            String sql = "SELECT conp.attribute_name, conp.attribute_value";
            sql += " FROM constraint_property conp";
            sql += " WHERE conp.constraint_entity_ID = " + entityID;
            sql += " AND conp.entity_ID = '" + ID + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<String, String> properties = new HashMap<String, String>();
            while (rs.next()) {
                properties.put(rs.getString("conp.attribute_name"), rs.getString("conp.attribute_value"));
            }
            return properties;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    private void setPropertyParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            ps.setInt(1, (Integer) data.get("constraintentityID"));
            ps.setString(2, (String) data.get("entityID"));
            ps.setString(3, (String) data.get("attributename"));
            ps.setString(4, (String) data.get("attributevalue"));
            ps.setInt(5, (Integer) data.get("constraintattributetypeID"));
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public int insertProperty(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "INSERT INTO constraint_property (constraint_entity_ID, entity_ID, attribute_name,";
            sql += " attribute_value, constraint_attribute_type_ID)";
            sql += " Values (?, ?, ?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setPropertyParams(ps, data);
            ps.execute();
            return (Integer) getLastInsertValue(ps, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public int updateProperty(HashMap<String, Object> data) throws BasicException {
        try {
            int ID = (Integer) data.get("ID");
            String sql = "UPDATE constraint_property set constraint_entity_ID = ?, entity_ID = ?,";
            sql += " attribute_name = ?, attribute_value = ?, constraint_attribute_type_ID = ?";
            sql += " WHERE ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setPropertyParams(ps, data);
            ps.setInt(6, ID);
            ps.execute();
            return ID;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public void deleteProperty(int constraintEntityID, String entityID ) throws BasicException {
        try {
            String sql = "DELETE FROM constraint_property";
            sql += " WHERE constraint_entity_ID = " + constraintEntityID;
            sql += " AND entity_ID = '" + entityID + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public void deleteProperty() throws BasicException {
        try {
            String sql = "DELETE FROM constraint_property";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getMultiValProperties() throws BasicException {
        try {
            String sql = "SELECT DISTINCT conmp.attribute_name, left_cone.ID, left_cone.name, left_cone.display_name,";
            sql += " right_cone.ID, right_cone.name, right_cone.display_name";
            sql += " FROM constraint_multivalue_property conmp, constraint_entity left_cone, constraint_entity right_cone";
            sql += " WHERE conmp.left_constraint_entity_ID = left_cone.ID";
            sql += " AND conmp.right_constraint_entity_ID = right_cone.ID";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<Integer, HashMap<String, Object>> results = new HashMap<Integer, HashMap<String, Object>>();
            int index = 0;
            while(rs.next()){
                HashMap<String, Object> result = new HashMap<String, Object>();

                result.put("attributename", rs.getString("conmp.attribute_name"));
                result.put("leftconstraintentityID", rs.getInt("left_cone.ID"));
                result.put("leftconstraintentityname", rs.getString("left_cone.name"));
                result.put("leftconstraintentitydisplayname", rs.getString("left_cone.display_name"));
                result.put("rightconstraintentityID", rs.getInt("right_cone.ID"));
                result.put("rightconstraintentityname", rs.getString("right_cone.name"));
                result.put("rightconstraintentitydisplayname", rs.getString("right_cone.display_name"));

                results.put(index++, result);
            }
            return results;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    protected HashMap<String, ArrayList<Integer>> getMultiValProperties(int entityID, String ID) throws BasicException {
        try {
            String sql = "SELECT conmp.attribute_name, conmp.attribute_value";
            sql += " FROM constraint_multivalue_property conmp";
            sql += " WHERE conmp.left_constraint_entity_ID = " + entityID;
            sql += " AND conmp.entity_ID = '" + ID + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
            ResultSet rs = st.getResultSet();

            HashMap<String, ArrayList<Integer>> multiValProperties = new HashMap<String, ArrayList<Integer>>();
            while (rs.next()) {
                String propertyName = rs.getString("conmp.attribute_name");
                int propertyValue = rs.getInt("conmp.attribute_value");
                ArrayList<Integer> list = multiValProperties.get(propertyName);
                if (list == null) {
                    list = new ArrayList<Integer>();
                    multiValProperties.put(propertyName, list);
                }
                list.add(propertyValue);
            }
            return multiValProperties;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    private void setMultiValuePropertyParams(PreparedStatement ps, HashMap<String, Object> data) throws BasicException {
        try {
            ps.setInt(1, (Integer) data.get("leftconstraintentityID"));
            ps.setInt(2, (Integer) data.get("rightconstraintentityID"));
            ps.setString(3, (String) data.get("entityID"));
            ps.setString(4, (String) data.get("attributename"));
            ps.setInt(5, (Integer) data.get("attributevalue"));
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public int insertMultiValueProperty(HashMap<String, Object> data) throws BasicException {
        try {
            String sql = "INSERT INTO constraint_multivalue_property (left_constraint_entity_ID, right_constraint_entity_ID, entity_ID,";
            sql += " attribute_name, attribute_value)";
            sql += " VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setMultiValuePropertyParams(ps, data);
            ps.execute();
            return (Integer) getLastInsertValue(ps, Integer.class);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public int updateMultiValueProperty(HashMap<String, Object> data) throws BasicException {
        try {
            int ID = (Integer) data.get("ID");
            String sql = "UPDATE constraint_multivalue_property set left_constraint_entity_ID = ?, right_constraint_entity_ID = ?,";
            sql += " entity_ID = ?, attribute_name = ?, attribute_value = ?";
            sql += " WHERE ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setPropertyParams(ps, data);
            ps.setInt(6, ID);
            ps.execute();
            return ID;
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public void deleteMultiValueProperty(int constraintLeftEntity, String entityID) throws BasicException {
        try {
            String sql = "DELETE FROM constraint_multivalue_property";
            sql += " WHERE left_constraint_entity_ID = " + constraintLeftEntity;
            sql += " AND  entity_ID = '" + entityID + "'";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }

    public void deleteMultiValueProperty() throws BasicException {
        try {
            String sql = "DELETE FROM constraint_multivalue_property";
            Statement st = getSession().getConnection().createStatement();
            st.execute(sql);
        } catch (SQLException ex) {
            throw new BasicException(EntityDataLogic.class.getName(), ex);
        }
    }
}

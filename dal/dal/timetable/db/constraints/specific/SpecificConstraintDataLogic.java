/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db.constraints.specific;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.ConstraintDataLogic;
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
public class SpecificConstraintDataLogic extends ConstraintDataLogic {

    private String selectQuery;

    public SpecificConstraintDataLogic(Session session) {
        super(session);

        selectQuery = "SELECT con.ID, con.name, con.apply, con.ishard, scon.course_section_ID, scon.timeslot_ID, scon.classroom_ID, scon.lesson_no";
        selectQuery += " FROM constrainttimetable con, specific_constraint scon";
        selectQuery += " WHERE scon.constrainttimetable_ID = con.ID";
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
                result.put("course", rs.getInt("scon.course_section_ID"));
                result.put("timeslot", rs.getInt("scon.timeslot_ID"));
                result.put("classroom", rs.getInt("scon.classroom_ID"));
                result.put("lessonno", rs.getInt("scon.lesson_no"));

                results.put(index++, result);
            }

            return results;
        } catch (SQLException ex) {
            throw new BasicException(SpecificConstraintDataLogic.class.getName(), ex);
        }
    }

    public HashMap<Integer, HashMap<String, Object>> getConstraints(boolean apply) throws BasicException {
        String sql = selectQuery;
        sql += " AND con.apply = " + apply;
        return getResult(sql);
    }

    public HashMap<Integer, HashMap<String, Object>> getConstraints() throws BasicException {
        return getResult(selectQuery);
    }

    private void setParams(PreparedStatement ps, HashMap<String, Object> data, int constraintTimeTableID) throws SQLException {
        ps.setInt(1, constraintTimeTableID);
        ps.setInt(2, (Integer) data.get("coursesectionID"));

        Integer timeSlotID = (Integer) data.get("timeslotID");
        if (timeSlotID == null) {
            ps.setNull(3, Types.INTEGER);
        } else {
            ps.setInt(3, timeSlotID);
        }

        Integer classRoomID = (Integer) data.get("classroomID");
        if (classRoomID == null) {
            ps.setNull(4, Types.INTEGER);
        } else {
            ps.setInt(4, classRoomID);
        }

        ps.setInt(5, (Integer) data.get("lessonno"));
    }

    @Override
    public int insert(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.insert(data);
            String sql = "INSERT INTO specific_constraint (constrainttimetable_ID, course_section_ID,";
            sql += " timeslot_ID, classroom_ID, lesson_no) VALUES(?, ?, ?, ?, ?)";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            setParams(ps, data, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(SpecificConstraintDataLogic.class.getName(), ex);
        }
    }

    @Override
    public int update(HashMap<String, Object> data) throws BasicException {
        try {
            int constraintTimeTableID = super.update(data);
            String sql = "UPDATE specific_constraint SET constrainttimetable_ID = ?, course_section_ID = ?,";
            sql += " timeslot_ID = ?, classroom_ID = ?, lesson_no = ? WHERE constrainttimetable_ID = ?";
            PreparedStatement ps = getSession().getConnection().prepareStatement(sql);
            setParams(ps, data, constraintTimeTableID);
            ps.setInt(6, constraintTimeTableID);
            ps.execute();
            return constraintTimeTableID;
        } catch (SQLException ex) {
            throw new BasicException(SpecificConstraintDataLogic.class.getName(), ex);
        }
    }
}

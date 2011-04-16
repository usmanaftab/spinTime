/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.ConstraintDataLogic;
import java.sql.SQLException;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public enum ConstraintType {

    Specific,
    Gap,
    Limit,
    General,
    Enclosure;
    private int ID;
    private String name;

    public static ConstraintType valueOf(int ID, String name) {
        ConstraintType constraintType = ConstraintType.valueOf(name);
        constraintType.setID(ID);
        constraintType.setName(name);
        return constraintType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static ConstraintType getConstraintType(Session session, String name) throws BasicException {
        ConstraintDataLogic constraintDataLogic = new ConstraintDataLogic(session);
        HashMap<String, Object> data = constraintDataLogic.getConstraintType(name);
        return ConstraintType.valueOf((Integer) data.get("ID"), (String) data.get("name"));
    }
}

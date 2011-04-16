/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.ConstraintDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public enum AttributeDType {

    Integer,
    Double,
    String,
    Time,
    Day;
    private int ID;
    private String name;

    public static AttributeDType valueOf(int ID, String name) {
        AttributeDType attributeDType = AttributeDType.valueOf(name);
        attributeDType.setID(ID);
        attributeDType.setName(name);
        return attributeDType;
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

    public static AttributeDType getAttributeDType(Session session, String attribTypeName) throws BasicException {
        ConstraintDataLogic constraintDataLogic = new ConstraintDataLogic(session);
        HashMap<String, Object> tokens = constraintDataLogic.getAttributeDType(attribTypeName);
        return AttributeDType.valueOf((Integer) tokens.get("ID"), (String) tokens.get("name"));
    }

    public static ArrayList<AttributeDType> getAttributeDTypes(Session session) throws BasicException {
        ArrayList<AttributeDType> result = new ArrayList<AttributeDType>();
        ConstraintDataLogic constraintDataLogic = new ConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraintDataLogic.getAttributeDTypes();
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> tokens = data.get(i);
            AttributeDType attributeDType = AttributeDType.valueOf((Integer) tokens.get("ID"), (String) tokens.get("name"));
            result.add(attributeDType);
        }
        return result;
    }
}

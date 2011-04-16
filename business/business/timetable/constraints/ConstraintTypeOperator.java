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
public enum ConstraintTypeOperator {

    Equal,
    NotEqual,
    LessThan,
    LessThanEqual,
    GreaterThan,
    GreaterThanEqual,
    Inclusion,
    Exclusion;
    private int ID;
    private ConstraintType constraintType;
    private String name;
    private String sign;

    public static ConstraintTypeOperator valueOf(int ID, ConstraintType constraintType, String name, String sign) {
        ConstraintTypeOperator consTypeOperator;

        if (name.equals("Not Equal")) {
            consTypeOperator = ConstraintTypeOperator.NotEqual;
        } else if (name.equals("Less Than")) {
            consTypeOperator = ConstraintTypeOperator.LessThan;
        } else if (name.equals("Less Than & Equal")) {
            consTypeOperator = ConstraintTypeOperator.LessThanEqual;
        } else if (name.equals("Greater Than")) {
            consTypeOperator = ConstraintTypeOperator.GreaterThan;
        } else if (name.equals("Greater Than & Equal")) {
            consTypeOperator = ConstraintTypeOperator.GreaterThanEqual;
        } else {
            consTypeOperator = ConstraintTypeOperator.valueOf(name);
        }

        consTypeOperator.setID(ID);
        consTypeOperator.setConstraintType(constraintType);
        consTypeOperator.setName(name);
        consTypeOperator.setSign(sign);
        return consTypeOperator;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public ConstraintType getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(ConstraintType constraintType) {
        this.constraintType = constraintType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public static ArrayList<ConstraintTypeOperator> getContTypeOperators(Session session, ConstraintType constraintType) throws BasicException {
        ArrayList<ConstraintTypeOperator> result = new ArrayList<ConstraintTypeOperator>();
        ConstraintDataLogic constraintDataLogic = new ConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraintDataLogic.getConstTypeOperators(constraintType.toString());
        for (int i = 0; i < data.size(); i++) {
            HashMap<String, Object> token = data.get(i);
            ConstraintTypeOperator constraintTypeOperator = valueOf((Integer) token.get("ID"), constraintType, (String) token.get("name"), (String) token.get("sign"));
            result.add(constraintTypeOperator);
        }
        return result;
    }
}
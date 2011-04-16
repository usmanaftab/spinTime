/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.enclosure;

import business.timetable.Entity;
import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.ConstraintTypeOperator;
import business.timetable.constraints.Constraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.enclosure.EnclosureConstraintDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class EnclosureConstraints extends Constraints<EnclosureConstraint> {

    public EnclosureConstraints() {
        super();
    }

    private static EnclosureConstraints getResult(HashMap<Integer, HashMap<String, Object>> data) {
        EnclosureConstraints enclosureConstraints = new EnclosureConstraints();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            EnclosureConstraint enclosureConstraint = EnclosureConstraint.createEnclosureConstraint(tokens);
            enclosureConstraints.add(enclosureConstraint);
        }

        return enclosureConstraints;
    }

    public static EnclosureConstraints getConstraints(Session session, boolean hardConstraint, boolean apply) throws BasicException {
        EnclosureConstraintDataLogic constraints = new EnclosureConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints(hardConstraint, apply);
        return getResult(data);        
    }

    public static EnclosureConstraints getConstraints(Session session) throws BasicException {
        EnclosureConstraintDataLogic constraints = new EnclosureConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints();
        return getResult(data);
    }
}

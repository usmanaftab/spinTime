/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.limit;

import business.timetable.constraints.Constraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.limit.LimitConstraintDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class LimitConstraints extends Constraints<LimitConstraint> {

    public LimitConstraints() {
        super();
    }

    private static LimitConstraints getResult(HashMap<Integer, HashMap<String, Object>> data) {
        LimitConstraints limitConstraints = new LimitConstraints();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            LimitConstraint limitConstraint = LimitConstraint.createLimitConstraint(tokens);

            limitConstraints.add(limitConstraint);
        }
        return limitConstraints;
    }

    public static LimitConstraints getConstraints(Session session, boolean hardConstraint, boolean apply) throws BasicException {
        LimitConstraintDataLogic constraints = new LimitConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints(hardConstraint, apply);
        return getResult(data);
    }

    public static LimitConstraints getConstraints(Session session) throws BasicException {
        LimitConstraintDataLogic constraints = new LimitConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints();
        return getResult(data);
    }
}

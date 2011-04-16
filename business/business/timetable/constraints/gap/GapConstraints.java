/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.constraints.Constraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.gap.GapConstraintDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapConstraints extends Constraints<GapConstraint> {

    public GapConstraints() {
        super();
    }

    private static GapConstraints getResult(HashMap<Integer, HashMap<String, Object>> data) {
        GapConstraints gapConstraints = new GapConstraints();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            GapConstraint gapConstraint = GapConstraint.createGapConstraint(tokens);
            gapConstraints.add(gapConstraint);
        }
        return gapConstraints;
    }

    public static GapConstraints getConstraints(Session session, boolean hardConstraint, boolean apply) throws BasicException {
        GapConstraintDataLogic constraints = new GapConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints(hardConstraint, apply);
        return getResult(data);
    }

    public static GapConstraints getConstraints(Session session) throws BasicException {
        GapConstraintDataLogic constraints = new GapConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints();
        return getResult(data);
    }
}

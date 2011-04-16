/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.general;
import business.timetable.constraints.Constraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.general.GeneralConstraintDataLogic;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GeneralConstraints extends Constraints<GeneralConstraint> {

    public GeneralConstraints() {
        super();
    }
    
    private static GeneralConstraints getResult(HashMap<Integer, HashMap<String, Object>> data) {
        GeneralConstraints generalConstraints = new GeneralConstraints();
        for (int index = 0; index < data.size(); index++) {
            HashMap<String, Object> tokens = data.get(index);
            GeneralConstraint generalConstraint = GeneralConstraint.createGeneralConstraint(tokens);

            generalConstraints.add(generalConstraint);
        }
        return generalConstraints;
    }

    public static GeneralConstraints getConstraints(Session session, boolean hardConstraint, boolean apply) throws BasicException {
        GeneralConstraintDataLogic constraints = new GeneralConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints(hardConstraint, apply);
        return getResult(data);        
    }

    public static GeneralConstraints getConstraints(Session session) throws BasicException {
        GeneralConstraintDataLogic constraints = new GeneralConstraintDataLogic(session);
        HashMap<Integer, HashMap<String, Object>> data = constraints.getConstraints();
        return getResult(data);
    }
}

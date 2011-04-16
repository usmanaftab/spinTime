/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.simulatedannealingttg.clashes;

import business.timetable.TimeTable;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.enclosure.EnclosureConstraints;
import business.timetable.constraints.gap.GapConstraints;
import business.timetable.constraints.general.GeneralConstraints;
import business.timetable.constraints.limit.LimitConstraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
class HardClashes extends AbstractClashes {

    private static ArrayList<Constraints> constraints;

    public HardClashes(Session session, TimeTable timeTable) {
        super(session, timeTable);
    }

    private void init() throws BasicException {
        constraints = new ArrayList<Constraints>();
        constraints.add(GapConstraints.getConstraints(getSession(), true, true));
        constraints.add(LimitConstraints.getConstraints(getSession(), true, true));
        constraints.add(GeneralConstraints.getConstraints(getSession(), true, true));
        constraints.add(EnclosureConstraints.getConstraints(getSession(), true, true));
    }

    @Override
    ClashType getType() {
        return ClashType.HardClashes;
    }

    @Override
    ArrayList<Constraints> getConstraints() throws BasicException {
        if(constraints == null){
            init();
        }
        return constraints;
    }
}

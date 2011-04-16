/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constraints;

import business.timetable.constraints.Constraint;
import business.timetable.constraints.enclosure.EnclosureConstraint;
import business.timetable.constraints.enclosure.EnclosureConstraints;
import business.timetable.constraints.gap.GapConstraint;
import business.timetable.constraints.gap.GapConstraints;
import business.timetable.constraints.general.GeneralConstraint;
import business.timetable.constraints.general.GeneralConstraints;
import business.timetable.constraints.limit.LimitConstraint;
import business.timetable.constraints.limit.LimitConstraints;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ConstraintLoading {

    public static void main(String[] args) {

        Session session;
        try {
            session = new Session();

            GapConstraints gcs = GapConstraints.getConstraints(session, true, true);
            for (Constraint c : gcs) {
                business.timetable.constraints.gap.GapConstraint gc = (GapConstraint) c;
                System.out.println();
            }

            gcs = GapConstraints.getConstraints(session, false, true);
            for (Constraint c : gcs) {
                business.timetable.constraints.gap.GapConstraint gc = (GapConstraint) c;
                System.out.println();
            }

            LimitConstraints lcs = LimitConstraints.getConstraints(session, true, true);
            for (Constraint c : lcs) {
                business.timetable.constraints.limit.LimitConstraint lc = (LimitConstraint) c;
                System.out.println();
            }

            lcs = LimitConstraints.getConstraints(session, false, true);
            for (Constraint c : lcs) {
                business.timetable.constraints.limit.LimitConstraint lc = (LimitConstraint) c;
                System.out.println();
            }

            EnclosureConstraints ecs = EnclosureConstraints.getConstraints(session, true, true);
            for (Constraint c : ecs) {
                business.timetable.constraints.enclosure.EnclosureConstraint ec = (EnclosureConstraint) c;
                System.out.println();
            }

            ecs = EnclosureConstraints.getConstraints(session, false, true);
            for (Constraint c : ecs) {
                business.timetable.constraints.enclosure.EnclosureConstraint ec = (EnclosureConstraint) c;
                System.out.println();
            }

            GeneralConstraints gencs = GeneralConstraints.getConstraints(session, true, true);
            for (Constraint c : gencs) {
                business.timetable.constraints.general.GeneralConstraint gc = (GeneralConstraint) c;
                System.out.println();
            }

            gencs = GeneralConstraints.getConstraints(session, false, true);
            for (Constraint c : gencs) {
                business.timetable.constraints.general.GeneralConstraint gc = (GeneralConstraint) c;
                System.out.println();
            }
        } catch (BasicException ex) {
            Logger.getLogger(ConstraintLoading.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

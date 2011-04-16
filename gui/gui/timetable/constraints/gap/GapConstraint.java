/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.constraints.gap;

import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.gap.GapConstraints;
import com.timetable.BasicException;
import gui.timetable.AppView;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapConstraint extends Constraint {

    private GapConstraintView view;

    public GapConstraint() {
        super(ConstraintType.Gap.toString());

        view = new GapConstraintView(this);
    }

    @Override
    public void init(AppView appView) {
        super.init(appView);
    }

    @Override
    public ConstraintView getConstraintView() {
        return view;
    }

    @Override
    protected Constraints<? extends business.timetable.constraints.Constraint> getConstraints() throws BasicException {
        return GapConstraints.getConstraints(getAppView().getSession());
    }

    @Override
    protected business.timetable.constraints.Constraint createNewConstraint() {
        return business.timetable.constraints.gap.GapConstraint.createDefaultConstraint();
    }

    @Override
    protected int saveConstraint(business.timetable.constraints.Constraint constraint) throws BasicException {
        business.timetable.constraints.gap.GapConstraint gapConstraint =
                (business.timetable.constraints.gap.GapConstraint) constraint;

        gapConstraint.setEntity(view.getEntity());
        gapConstraint.setMinGap(view.getMinGap());
        gapConstraint.setMaxGap(view.getMaxGap());
        gapConstraint.setMinSuccession(view.getMinSuccession());
        gapConstraint.setMaxSuccession(view.getMaxSuccession());
        gapConstraint.setTimeSlotType(view.getTimeSlotType());

        return business.timetable.constraints.gap.GapConstraint.save(getAppView().getSession(), gapConstraint);
    }
}

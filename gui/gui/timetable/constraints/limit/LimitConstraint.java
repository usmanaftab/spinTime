/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.constraints.limit;

import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.limit.LimitConstraints;
import com.timetable.BasicException;
import gui.timetable.AppView;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class LimitConstraint extends Constraint {

    public LimitConstraintView view;

    public LimitConstraint() {
        super(ConstraintType.Limit.toString());

        view = new LimitConstraintView(this);
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
        return LimitConstraints.getConstraints(getAppView().getSession());
    }

    @Override
    protected business.timetable.constraints.Constraint createNewConstraint() {
        return business.timetable.constraints.limit.LimitConstraint.createDefaultConstraint();
    }

    @Override
    protected int saveConstraint(business.timetable.constraints.Constraint constraint) throws BasicException {
        business.timetable.constraints.limit.LimitConstraint limitConstraint =
                (business.timetable.constraints.limit.LimitConstraint) constraint;

        limitConstraint.setEntity(view.getEntity());
        limitConstraint.setMinLimit(view.getMinLimit());
        limitConstraint.setMaxLimit(view.getMaxLimit());
        limitConstraint.setTimeSlotType(view.getTimeSlotType());

        return business.timetable.constraints.limit.LimitConstraint.save(getAppView().getSession(), limitConstraint);
    }
}

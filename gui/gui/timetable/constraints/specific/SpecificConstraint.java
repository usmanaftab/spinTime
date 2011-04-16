/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.constraints.specific;

import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.specific.SpecificConstraints;
import com.timetable.BasicException;
import gui.timetable.AppView;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SpecificConstraint extends Constraint {

    private SpecificConstraintView view;

    public SpecificConstraint() {
        super(ConstraintType.Specific.toString());

        view = new SpecificConstraintView(this);
    }

    @Override
    public void init(AppView appView) {
        super.init(appView);

        setHardConstCheckBox(false);
    }

    @Override
    public ConstraintView getConstraintView() {
        return view;
    }

    @Override
    protected Constraints<? extends business.timetable.constraints.Constraint> getConstraints() throws BasicException {
        return SpecificConstraints.getConstraints(getAppView().getSession());
    }

    @Override
    protected business.timetable.constraints.Constraint createNewConstraint() {
        return business.timetable.constraints.specific.SpecificConstraint.createDefaultConstraint();
    }

    @Override
    protected int saveConstraint(business.timetable.constraints.Constraint constraint) throws BasicException {
        business.timetable.constraints.specific.SpecificConstraint specificConstraint =
                (business.timetable.constraints.specific.SpecificConstraint) constraint;

        specificConstraint.setCourse(view.getCourse());
        specificConstraint.setClassRoom(view.getClassRoom());
        specificConstraint.setTimeSlot(view.getTimeSlot());
        specificConstraint.setLessonNo(view.getLessonNo());

        return business.timetable.constraints.specific.SpecificConstraint.save(getAppView().getSession(), specificConstraint);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.constraints.enclosure;

import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.enclosure.EnclosureConstraints;
import com.timetable.BasicException;
import com.timetable.ValidationException;
import gui.timetable.AppView;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class EnclosureConstraint extends Constraint {

    private EnclosureConstraintView view;

    public EnclosureConstraint() {
        super(ConstraintType.Enclosure.toString());

        view = new EnclosureConstraintView(this);
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
        return EnclosureConstraints.getConstraints(getAppView().getSession());
    }

    @Override
    protected business.timetable.constraints.Constraint createNewConstraint() {
        return business.timetable.constraints.enclosure.EnclosureConstraint.createDefaultConstraint();
    }

    @Override
    protected int saveConstraint(business.timetable.constraints.Constraint constraint) throws BasicException {
        business.timetable.constraints.enclosure.EnclosureConstraint enclosureConstraint =
                (business.timetable.constraints.enclosure.EnclosureConstraint) constraint;
        
        if(view.getRightEntity().getID() != view.getAttribute().getEntity().getID()) {
            throw new ValidationException("Left entity attribute type and right entity must be same.");
        }

        enclosureConstraint.setConstraintTypeOperator(view.getConstraintTypeOperator());
        enclosureConstraint.setLeftEntity(view.getLeftEntity());
        enclosureConstraint.setRightEntity(view.getRightEntity());
        enclosureConstraint.setAttribName(view.getAttribute().getName());

        return business.timetable.constraints.enclosure.EnclosureConstraint.save(getAppView().getSession(), enclosureConstraint);
    }
}

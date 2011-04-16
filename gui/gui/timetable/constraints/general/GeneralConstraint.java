/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.timetable.constraints.general;
import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.Constraints;
import business.timetable.constraints.general.GeneralConstraints;
import com.timetable.BasicException;
import com.timetable.ValidationException;
import gui.timetable.AppView;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GeneralConstraint extends Constraint {

    private GeneralConstraintView view;

    public GeneralConstraint() {
        super(ConstraintType.General.toString());

        view = new GeneralConstraintView(this);
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
        return GeneralConstraints.getConstraints(getAppView().getSession());
    }

    @Override
    protected business.timetable.constraints.Constraint createNewConstraint() {
        return business.timetable.constraints.general.GeneralConstraint.createDefaultConstraint();
    }

    @Override
    protected int saveConstraint(business.timetable.constraints.Constraint constraint) throws BasicException {
        business.timetable.constraints.general.GeneralConstraint generalConstraint =
                (business.timetable.constraints.general.GeneralConstraint) constraint;

        if(view.getLeftEntityAttrib().getAttributeDType().getID()
                != view.getRightEntityAttrib().getAttributeDType().getID()){
            throw new ValidationException("Data type of attributes for both entities should be same.");
        }

        generalConstraint.setConstraintTypeOperator(view.getConstraintTypeOperator());
        generalConstraint.setLeftEntity(view.getLeftEntity());
        generalConstraint.setLeftAttribName(view.getLeftEntityAttrib().getName());
        generalConstraint.setAttribDType(view.getLeftEntityAttrib().getAttributeDType());
        generalConstraint.setRightEntity(view.getRightEntity());
        generalConstraint.setRightAttribName(view.getRightEntityAttrib().getName());

        return business.timetable.constraints.general.GeneralConstraint.save(getAppView().getSession(), generalConstraint);
    }
}

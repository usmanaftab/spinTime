/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.constraints;

import gui.timetable.AppPanel;
import gui.timetable.AppView;
import javax.swing.JPanel;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class ConstraintView extends JPanel implements AppPanel {

    private Constraint constraint;
    private AppView appView;
    private boolean changing;

    public abstract void enableComponents(boolean b);

    public ConstraintView(Constraint constraint) {
        this.constraint = constraint;
        this.changing = false;
    }

    public void init(AppView appView) {
        this.appView = appView;
    }

    @Override
    public AppView getAppView() {
        return appView;
    }

    protected Constraint getConstraint() {
        return constraint;
    }

    public boolean isChanging() {
        return changing;
    }

    public void setChanging(boolean changing) {
        this.changing = changing;
    }

    protected void setDirty(boolean b) {
        if (!changing) {
            constraint.setDirty(b);
        }
    }

    public abstract void changeSelection(business.timetable.constraints.Constraint constraint);
}

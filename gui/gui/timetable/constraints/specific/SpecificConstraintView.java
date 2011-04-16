/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * SpecificConstraints.java
 *
 * Created on Feb 28, 2010, 6:33:24 PM
 */
package gui.timetable.constraints.specific;

import business.timetable.Entity;
import business.timetable.classrooms.ClassRoom;
import business.timetable.courses.Course;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import com.timetable.gui.JMessageDialog;
import com.timetable.gui.MessageInf;
import gui.timetable.AppView;
import gui.timetable.common.EntitySelectorListener;
import gui.timetable.common.SelectionChangedEvent;
import gui.timetable.constraints.Constraint;
import gui.timetable.constraints.ConstraintView;
import java.util.ArrayList;
import javax.swing.DefaultComboBoxModel;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SpecificConstraintView extends ConstraintView {

    /** Creates new form SpecificConstraints */
    public SpecificConstraintView(Constraint constraint) {
        super(constraint);

        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        courseSelector = new gui.timetable.common.EntitySelector();
        classRoomSelector = new gui.timetable.common.EntitySelector();
        timeSlotSelector = new gui.timetable.common.EntitySelector();
        lessonComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();

        lessonComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                lessonComboBoxActionPerformed(evt);
            }
        });

        jLabel1.setText("Lesson No.*");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(courseSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(timeSlotSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 97, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(28, 28, 28)
                        .addComponent(lessonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 143, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(classRoomSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(courseSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lessonComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGap(21, 21, 21)
                .addComponent(classRoomSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(timeSlotSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    @Override
    public void init(AppView appView) {
        super.init(appView);

        setName("Specific Constraints");
        try {
            Entity courseEntity = Entity.getEntity(getAppView().getSession(), Entity.Course.toString());
            Entity classRoomEntity = Entity.getEntity(getAppView().getSession(), Entity.ClassRoom.toString());
            Entity timeSlotEntity = Entity.getEntity(getAppView().getSession(), Entity.TimeSlot.toString());


            courseSelector.init(appView);
            courseSelector.setEntity(courseEntity);
            courseSelector.setNameLabel(courseEntity.getDisplayName() + "*");
            courseSelector.addEntitySelectorListerner(new EntitySelectorListener() {

                public void selectionChanged(SelectionChangedEvent evt) {
                    courseSelectionChanged(evt);
                    entitySelectionChanged(evt);
                }
            });

            classRoomSelector.init(appView);
            classRoomSelector.setEntity(classRoomEntity);
            classRoomSelector.addEntitySelectorListerner(new EntitySelectorListener() {

                public void selectionChanged(SelectionChangedEvent evt) {
                    entitySelectionChanged(evt);
                }
            });

            timeSlotSelector.init(appView);
            timeSlotSelector.setProperty1Name("ID");
            timeSlotSelector.setProperty2Name("timeSlot");
            timeSlotSelector.setEntity(timeSlotEntity);
            timeSlotSelector.addEntitySelectorListerner(new EntitySelectorListener() {

                public void selectionChanged(SelectionChangedEvent evt) {
                    entitySelectionChanged(evt);
                }
            });

        } catch (BasicException ex) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, ex.getMessage(), ex));
        }
    }

    private void courseSelectionChanged(SelectionChangedEvent evt) {
        SetLessonNo((Course) evt.getEntityObject(), 1);
    }

    private void SetLessonNo(Course course, int index) {
        ArrayList<Integer> list = new ArrayList<Integer>();
        if (course != null) {
            for (int i = 0; i < course.getLessonCount(); i++) {
                list.add(i + 1);
            }
        }
        lessonComboBox.setModel(new DefaultComboBoxModel(list.toArray()));
        lessonComboBox.setSelectedItem(index);
    }

    @Override
    public void enableComponents(boolean b) {
        classRoomSelector.enableComponent(b);
        courseSelector.enableComponent(b);
        timeSlotSelector.enableComponent(b);
        lessonComboBox.setEnabled(b);
    }

    @Override
    public void changeSelection(business.timetable.constraints.Constraint constraint) {
        setChanging(true);
        business.timetable.constraints.specific.SpecificConstraint specificConstraint =
                (business.timetable.constraints.specific.SpecificConstraint) constraint;

        courseSelector.setSelectedEntityObject(specificConstraint.getCourse());
        classRoomSelector.setSelectedEntityObject(specificConstraint.getClassRoom());
        timeSlotSelector.setSelectedEntityObject(specificConstraint.getTimeSlot());
        SetLessonNo(specificConstraint.getCourse(), specificConstraint.getLessonNo() + 1);
        setChanging(false);
    }

    private void entitySelectionChanged(SelectionChangedEvent evt) {
        setDirty(true);
    }

    public ClassRoom getClassRoom() {
        return (ClassRoom) classRoomSelector.getSelectedEntityObject();
    }

    public Course getCourse() {
        return (Course) courseSelector.getSelectedEntityObject();
    }

    public Integer getLessonNo() {
        Integer lessonNo = null;
        if (lessonComboBox.getModel() != null) {
            lessonNo = (Integer) lessonComboBox.getModel().getSelectedItem();
        }
        return (lessonNo == null) ? -1 : lessonNo - 1;
    }

    public TimeSlot getTimeSlot() {
        return (TimeSlot) timeSlotSelector.getSelectedEntityObject();
    }

    private void lessonComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_lessonComboBoxActionPerformed
        setDirty(true);
    }//GEN-LAST:event_lessonComboBoxActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private gui.timetable.common.EntitySelector classRoomSelector;
    private gui.timetable.common.EntitySelector courseSelector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JComboBox lessonComboBox;
    private gui.timetable.common.EntitySelector timeSlotSelector;
    // End of variables declaration//GEN-END:variables
}

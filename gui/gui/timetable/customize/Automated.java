/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * Automated.java
 *
 * Created on Jun 26, 2010, 4:07:33 PM
 */
package gui.timetable.customize;

import business.timetable.classrooms.ClassRoom;
import business.timetable.courses.Course;
import business.timetable.department.Department;
import business.timetable.semester.Semester;
import business.timetable.teachers.Teacher;
import business.timetable.timeslots.TimeSlot;
import gui.timetable.AppPanel;
import gui.timetable.AppView;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Automated extends javax.swing.JPanel implements AppPanel {

    /** Creates new form Automated */
    public Automated() {
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

        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList();
        saveButton = new javax.swing.JButton();

        jLabel1.setText("Available Timeslots");

        jScrollPane1.setViewportView(jList1);

        saveButton.setText("Save");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 169, Short.MAX_VALUE)
                .addComponent(saveButton)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(saveButton)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 258, Short.MAX_VALUE)))
                .addContainerGap())
        );
    }// </editor-fold>//GEN-END:initComponents

    public void init(AppView appView) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public AppView getAppView() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    void changeSelection(ClassRoom classRoom, TimeSlot timeSlot, Course course, Teacher teacher, Semester semester, Department department) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JList jList1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JButton saveButton;
    // End of variables declaration//GEN-END:variables
}

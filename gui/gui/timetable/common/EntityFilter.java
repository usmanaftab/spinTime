/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * EntityFilter.java
 *
 * Created on Mar 10, 2010, 1:55:32 PM
 */
package gui.timetable.common;

import business.timetable.department.Department;
import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import com.timetable.gui.JMessageDialog;
import com.timetable.gui.MessageInf;
import gui.timetable.AppPanel;
import gui.timetable.AppView;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JPanel;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class EntityFilter extends JPanel implements AppPanel {

    private AppView appView;
    private HashMap<JCheckBox, Department> departChkBoxMap;

    /** Creates new form EntityFilter */
    public EntityFilter() {
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

        jLabel2 = new javax.swing.JLabel();
        semesterComboBox = new javax.swing.JComboBox();
        jLabel1 = new javax.swing.JLabel();
        departmentPanel = new javax.swing.JPanel();

        jLabel2.setText("Department");

        semesterComboBox.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                semesterComboBoxItemStateChanged(evt);
            }
        });

        jLabel1.setText("Semester");

        departmentPanel.setLayout(new java.awt.GridLayout(2, 2, 1, 1));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(semesterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 208, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(departmentPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(semesterComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(departmentPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(20, 20, 20))
        );
    }// </editor-fold>//GEN-END:initComponents

    public void init(AppView appView) {
        this.appView = appView;
        try {
            Semesters semesters = Semesters.getData(appView.getSession());
            semesterComboBox.setModel(new DefaultComboBoxModel(semesters.values().toArray()));

            Departments departments = Departments.getData(appView.getSession());
            departChkBoxMap = new HashMap<JCheckBox, Department>();
            
            if (departments.size() <= 0) {
                return;
            }
            int rows = departments.size() / 2;
            int cols = rows;
            GridLayout layout = new GridLayout(rows, cols, 0, 0);
            departmentPanel.setLayout(layout);
            for (Department department : departments) {
                JCheckBox checkBox = new JCheckBox(department.getName());
                departChkBoxMap.put(checkBox, department);
                departmentPanel.add(checkBox);
                checkBox.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent evt) {
                        checkBoxActionPerformed(evt);
                    }
                });
            }
        } catch (BasicException ex) {
            JMessageDialog.showMessage(this, new MessageInf(MessageInf.SGN_DANGER, ex.getMessage(), ex));
        }
    }

    @Override
    public AppView getAppView() {
        return appView;
    }

    public JCheckBox[] getCheckBoxs() {
        JCheckBox[] result = new JCheckBox[departChkBoxMap.size()];
        for (int i = 0; i < departChkBoxMap.size(); i++) {
            result[i] = (JCheckBox) departmentPanel.getComponent(i);
        }
        return result;
    }

    public Departments getDepartments() {
        Departments departments = new Departments();
        for (int i = 0; i < departChkBoxMap.size(); i++) {
            JCheckBox checkBox = (JCheckBox) departmentPanel.getComponent(i);
            if (checkBox.isSelected()) {
                Department department = departChkBoxMap.get(checkBox);
                departments.put(department.getID(), department);
            }
        }
        return departments;
    }

    public Semester getSemester() {
        return (Semester) semesterComboBox.getSelectedItem();
    }

    public void addFilterChangedListerner(FilterChangedListener l) {
        listenerList.add(FilterChangedListener.class, l);
    }

    private void fireFilterSemesterChangedEvent() {
        FilterChangedEvent evt = new FilterChangedEvent(getDepartments(), getSemester());

        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == FilterChangedListener.class) {
                ((FilterChangedListener) listeners[i + 1]).filterSemesterChanged(evt);
            }
        }
    }

    private void fireFilterDepartmentChangedEvent() {
        FilterChangedEvent evt = new FilterChangedEvent(getDepartments(), getSemester());

        Object[] listeners = listenerList.getListenerList();

        for (int i = 0; i < listeners.length; i += 2) {
            if (listeners[i] == FilterChangedListener.class) {
                ((FilterChangedListener) listeners[i + 1]).filterDepartmentChanged(evt);
            }
        }
    }

    private void checkBoxActionPerformed(ActionEvent evt) {
        fireFilterDepartmentChangedEvent();
    }

    private void semesterComboBoxItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_semesterComboBoxItemStateChanged
        fireFilterSemesterChangedEvent();
    }//GEN-LAST:event_semesterComboBoxItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel departmentPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JComboBox semesterComboBox;
    // End of variables declaration//GEN-END:variables
}
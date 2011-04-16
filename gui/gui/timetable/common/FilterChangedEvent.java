/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.timetable.common;

import business.timetable.department.Departments;
import business.timetable.semester.Semester;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class FilterChangedEvent {

    private Departments departments;
    private Semester semester;

    public FilterChangedEvent(Departments departments, Semester semester) {
        this.departments = departments;
        this.semester = semester;
    }

    public Departments getDepartments() {
        return departments;
    }

    public Semester getSemester() {
        return semester;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable;

import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import com.timetable.BasicException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface ITimeTableGenerator {

    public int generateTimeTable() throws BasicException;

    public void generateResults();

    public void saveTimeTable() throws BasicException;

    public void setDepartments(Departments departments);

    public void setSemester(Semester semester);
}

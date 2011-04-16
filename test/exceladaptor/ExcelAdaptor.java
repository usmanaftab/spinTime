/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package exceladaptor;

import business.timetable.department.Departments;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import business.timetable.simulatedannealingttg.SimulatedAnnealingTTG;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ExcelAdaptor extends SimulatedAnnealingTTG {

    public static void main(String[] agrs) {
        try {
            Session session = new Session();
            Departments departments = Departments.getData(session);
            Departments newDepartments = new Departments();
            newDepartments.put(departments.getValueAt(0).getID(), departments.getValueAt(0));
            Semesters semesters = Semesters.getData(session);
            ExcelAdaptor ec = new ExcelAdaptor(session, newDepartments, semesters.getValueAt(0));
            ec.start();
        } catch (BasicException ex) {
            Logger.getLogger(ExcelAdaptor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public ExcelAdaptor(Session session, Departments departments, Semester semester) {
        super(session);
        setDepartments(departments);
        setSemester(semester);        
    }

    private void start() throws BasicException {
        initRepresentation();

        business.timetable.exceladaptor.ExcelAdaptor adaptor = new business.timetable.exceladaptor.ExcelAdaptor(this, getRepresentation());
        adaptor.GenerateTimeTable();
    }

}

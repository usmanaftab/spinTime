/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constraints;

import business.timetable.classrooms.ClassRoom;
import business.timetable.constraints.general.GeneralConstraints;
import business.timetable.department.Departments;
import business.timetable.exceladaptor.ExcelAdaptor;
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
public class GeneralConstraint extends SimulatedAnnealingTTG {

    public static void main(String[] args) {
        try {
            Session session = new Session();
            Departments departments = Departments.getData(session);
            Departments newDepartments = new Departments();
            newDepartments.put(departments.getValueAt(0).getID(), departments.getValueAt(0));
            Semesters semesters = Semesters.getData(session);
            GeneralConstraint gc = new GeneralConstraint(session, newDepartments, semesters.getValueAt(0));
            gc.start();
        } catch (BasicException ex) {
            Logger.getLogger(GeneralConstraint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public GeneralConstraint(Session session, Departments departments, Semester semester) {
        super(session);
        setDepartments(departments);
        setSemester(semester);
    }

    

    private void start() throws BasicException {
        initRepresentation();

        GeneralConstraints gcs = GeneralConstraints.getConstraints(getSession(), true, true);
        gcs.checkConstraints(this, getRepresentation());

        for(ClassRoom cr: gcs.getBadClassRooms()){
            System.out.println("ClassRoom ID: " + cr.getID() + " ClassRoom Name: " + cr.getName() + " ClassRoom Capacity: " + cr.getCapacity());
        }

        ExcelAdaptor adaptor = new ExcelAdaptor(this, getRepresentation());
        adaptor.GenerateTimeTable();
    }
}

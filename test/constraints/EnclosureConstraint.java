/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constraints;

import business.timetable.constraints.enclosure.EnclosureConstraints;
import business.timetable.department.Departments;
import business.timetable.exceladaptor.ExcelAdaptor;
import business.timetable.semester.Semester;
import business.timetable.semester.Semesters;
import business.timetable.simulatedannealingttg.SimulatedAnnealingTTG;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class EnclosureConstraint extends SimulatedAnnealingTTG {

    public static void main(String[] agrs) {
        try {
            Session session = new Session();
            Departments departments = Departments.getData(session);
            Departments newDepartments = new Departments();
            newDepartments.put(departments.getValueAt(0).getID(), departments.getValueAt(0));
            Semesters semesters = Semesters.getData(session);
            EnclosureConstraint ec = new EnclosureConstraint(session, newDepartments, semesters.getValueAt(0));
            ec.start();
        } catch (BasicException ex) {
            Logger.getLogger(EnclosureConstraint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public EnclosureConstraint(Session session, Departments departments, Semester semester) {
        super(session);
        setDepartments(departments);
        setSemester(semester);
    }

    private void start() throws BasicException {
        initRepresentation();

        EnclosureConstraints ecs = EnclosureConstraints.getConstraints(getSession(), true, true);
        ecs.checkConstraints(this, getRepresentation());

        for (TimeSlot timeSlot : ecs.getBadTimeSlots()) {
            System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
        }

        ExcelAdaptor adaptor = new ExcelAdaptor(this, getRepresentation());
        adaptor.GenerateTimeTable();
    }
}

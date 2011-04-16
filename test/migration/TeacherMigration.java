/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package migration;

import business.timetable.semester.Semesters;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TeacherMigration {

    public static void main(String[] args){
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            business.timetable.migration.excel.TeacherMigration t =
                    new business.timetable.migration.excel.TeacherMigration(session, semesters.getValueAt(0),
                    "input data\\Teacher Info.xls");
            t.migrate();
        } catch (BasicException ex) {
            Logger.getLogger(TeacherMigration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}

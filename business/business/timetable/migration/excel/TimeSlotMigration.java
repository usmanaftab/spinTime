/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.timeslot.TimeSlotDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TimeSlotMigration extends Migration {

    private TimeSlotDataLogic timeSlotDataLogic;
    private PropertyMigration propertyMigration;
    private boolean migrateProperties;

    public TimeSlotMigration(Session session) {
        super(session);
        migrateProperties = false;
    }

    public TimeSlotMigration(Session session, Semester semester, String fileName) {
        super(session, semester, fileName);
    }

    @Override
    protected void beginMigration() throws BasicException {
        propertyMigration = new PropertyMigration(getSession(), "TimeSlot", getHeader());
        int index = 3;
        if (getHeader().size() > index) {
            HashMap<String, ArrayList<String>> row = getRowAt(0);
            int count = Integer.parseInt(row.get(getHeader().get(index)).get(0));
            propertyMigration.setStartIndex(index + 1);
            propertyMigration.setEndIndex(count);
            migrateProperties = true;
        }

        timeSlotDataLogic = new TimeSlotDataLogic(getSession());
        timeSlotDataLogic.deleteSemesterData(getSemester().getID());
    }

    @Override
    protected void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException {
        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("day", row.get(getHeader().get(0)).get(0));
        data.put("begintime", row.get(getHeader().get(1)).get(0));
        data.put("endtime", row.get(getHeader().get(2)).get(0));

        int ID = timeSlotDataLogic.isAlreadyExist(data);
        if (ID == -1) {
            ID = timeSlotDataLogic.insert(data);
        }

        data.put("timeslotID", ID);
        data.put("semesterID", getSemester().getID());
        timeSlotDataLogic.insertTimeslotSemester(data);

        propertyMigration.deleteProperties(String.valueOf(ID));
        if (migrateProperties) {
            propertyMigration.migrate(row, String.valueOf(ID));
        }
    }

    @Override
    protected void endMigration() throws BasicException {
    }
}

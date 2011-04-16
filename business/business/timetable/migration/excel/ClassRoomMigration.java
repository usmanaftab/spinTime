/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.classroom.ClassRoomDataLogic;
import dal.timetable.db.department.DepartmentDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ClassRoomMigration extends Migration {

    private ClassRoomDataLogic classRoomDataLogic;
    private DepartmentDataLogic deptDataLogic;
    private PropertyMigration propertyMigration;
    private MultiValuePropertyMigration multiValuePropertyMigration;
    private boolean migrateProperties;
    private boolean migreateMulProperties;

    public ClassRoomMigration(Session session) {
        super(session);
        migrateProperties = false;
        migreateMulProperties = false;
    }

    public ClassRoomMigration(Session session, Semester semester, String fileName) {
        super(session, semester, fileName);
    }

    @Override
    protected void beginMigration() throws BasicException {
        propertyMigration = new PropertyMigration(getSession(), "ClassRoom", getHeader());
        multiValuePropertyMigration = new MultiValuePropertyMigration(getSession(), "ClassRoom", getHeader());
        int index = 3;
        if (getHeader().size() > index) {
            HashMap<String, ArrayList<String>> row = getRowAt(0);
            int count = Integer.parseInt(row.get(getHeader().get(index)).get(0));
            propertyMigration.setStartIndex(index + 1);
            propertyMigration.setEndIndex(count);
            migrateProperties = true;

            int startIndex = index + 1 + count * 2;
            if (getHeader().size() > startIndex) {
                multiValuePropertyMigration.setStartIndex(startIndex);
                multiValuePropertyMigration.setEndIndex(getHeader().size());
                migreateMulProperties = true;
            }
        }

        classRoomDataLogic = new ClassRoomDataLogic(getSession());
        deptDataLogic = new DepartmentDataLogic(getSession());
        classRoomDataLogic.deleteSemesterData(getSemester().getID());
    }

    @Override
    protected void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String name = row.get(getHeader().get(0)).get(0);
        data.put("name", name);
        data.put("semesterID", getSemester().getID());
        if (row.get(getHeader().get(2)) != null) {
            data.put("capacity", Integer.parseInt(row.get(getHeader().get(2)).get(0)));
        }

        int ID = classRoomDataLogic.isAlreadyPresent(data);
        if (ID == -1) {
            ID = classRoomDataLogic.insert(data);
        } else {
            classRoomDataLogic.update(data);
        }

        data.put("classroomID", ID);
        for (String deptName : row.get(getHeader().get(1))) {
            data.put("departmentID", deptDataLogic.getID(deptName));
            classRoomDataLogic.insertClassRoomDept(data);
        }

        propertyMigration.deleteProperties(String.valueOf(ID));
        if (migrateProperties) {
            propertyMigration.migrate(row, String.valueOf(ID));
        }

        multiValuePropertyMigration.deleteProperties(String.valueOf(ID));
        if(migreateMulProperties){
            multiValuePropertyMigration.migrate(row, String.valueOf(ID));
        }
    }

    @Override
    protected void endMigration() {
    }
}

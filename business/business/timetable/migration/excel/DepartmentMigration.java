/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.department.DepartmentDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class DepartmentMigration extends Migration implements IMigration {

    private DepartmentDataLogic departmentDataLogic;

    public DepartmentMigration(Session session) {
        super(session);
    }

    public DepartmentMigration(Session session, String fileName) {
        super(session, fileName);
    }

    @Override
    protected void beginMigration() throws BasicException {
        departmentDataLogic = new DepartmentDataLogic(getSession());
    }

    @Override
    protected void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        String name = row.get(getHeader().get(0)).get(0);
        data.put("name", name);
        departmentDataLogic.insert(data);
    }

    @Override
    protected void endMigration() throws BasicException {
    }
}

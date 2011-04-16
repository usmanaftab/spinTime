/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.Entity;
import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class MultiValuePropertyMigration {

    private Session session;
    private Entity leftEntity;
    private int startIndex;
    private int endIndex;
    private ArrayList<String> header;
    private EntityDataLogic entityDataLogic;

    public MultiValuePropertyMigration(Session session, String name, ArrayList<String> header) throws BasicException {
        this.session = session;
        leftEntity = Entity.getEntity(session, name);
        this.header = header;
        entityDataLogic = new EntityDataLogic(session);
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getEndIndex() {
        return endIndex;
    }

    public void setEndIndex(int endIndex) {
        this.endIndex = endIndex;
    }

    public void deleteProperties(String ID) throws BasicException {
        entityDataLogic.deleteMultiValueProperty(leftEntity.getID(), ID);
    }

    public void migrate(HashMap<String, ArrayList<String>> row, String ID) throws BasicException {
        for (int i = startIndex; i < endIndex; i += 2) {
            String propertyName = header.get(i);
            String rightEntityName = header.get(i + 1);
            if (row.get(header.get(i)) == null) {
                continue;
            }

            Entity rightEntity = Entity.getEntity(session, rightEntityName);
            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("leftconstraintentityID", leftEntity.getID());
            data.put("rightconstraintentityID", rightEntity.getID());
            data.put("entityID", ID);
            data.put("attributename", propertyName);
            for (String v : row.get(header.get(i))) {
                int value = Integer.parseInt(v);
                data.put("attributevalue", value);
                entityDataLogic.insertMultiValueProperty(data);
            }
        }
    }
}

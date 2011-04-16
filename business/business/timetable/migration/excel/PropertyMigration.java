/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.Entity;
import business.timetable.constraints.AttributeDType;
import com.timetable.BasicException;
import dal.timetable.db.EntityDataLogic;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class PropertyMigration {

    private Session session;
    private Entity entity;
    private int startIndex;
    private int endIndex;
    private ArrayList<String> header;
    private EntityDataLogic entityDataLogic;

    public PropertyMigration(Session session, String name, ArrayList<String> header) throws BasicException {
        this.session = session;
        entity = Entity.getEntity(session, name);
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

    public void setEndIndex(int count) {
        this.endIndex = startIndex + count * 2;
    }

    public void deleteProperties(String ID) throws BasicException{
        entityDataLogic.deleteProperty(entity.getID(), ID);
    }

    public void migrate(HashMap<String, ArrayList<String>> row, String ID) throws BasicException {
        for (int i = startIndex; i < endIndex; i += 2) {
            String propertyName = header.get(i);
            String propertyType = header.get(i + 1);
            if(row.get(header.get(i)) == null){
                continue;
            }
            String propertyValue = row.get(header.get(i)).get(0);
            AttributeDType attributeDType = AttributeDType.getAttributeDType(session, propertyType);

            HashMap<String, Object> data = new HashMap<String, Object>();
            data.put("constraintentityID", entity.getID());
            data.put("entityID", ID);
            data.put("attributename", propertyName);
            data.put("attributevalue", propertyValue);
            data.put("constraintattributetypeID", attributeDType.getID());
            entityDataLogic.insertProperty(data);
        }
    }
}

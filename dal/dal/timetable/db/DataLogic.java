/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db;

import com.timetable.BasicException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class DataLogic {

    private Session session;
    
    
    public DataLogic(Session session){
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public  Object getLastInsertValue(PreparedStatement ps, Class c) throws BasicException {
        try {
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                if (Integer.class == c) {
                    return rs.getInt(1);
                } else if (Double.class == c) {
                    return rs.getDouble(1);
                } else if (String.class == c) {
                    return rs.getString(1);
                }
                return rs.getObject(1);
            }
        } catch (SQLException ex){
            throw new BasicException(DataLogic.class.getName(), ex);
        }
        throw new BasicException("Couldn't create auto generated key");
    }

    protected String getIntegerList(Object[] list) {
        String result = "";

        if(list.length <= 0){
            return result;
        }

        result = String.valueOf(list[0]);
        for(int i = 1; i < list.length; i++){
            result += ", " + String.valueOf(list[i]);
        }

        return result;
    }

    protected String getStringList(Object[] list){
        String result;

        result = String.valueOf(list[0]);
        for(int i = 1; i < list.length; i++){
            result += "', '" + String.valueOf(list[i]);
        }

        return result;
    }
}

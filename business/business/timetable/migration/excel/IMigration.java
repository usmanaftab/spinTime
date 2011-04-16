/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import com.timetable.BasicException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface IMigration {

    public void migrate() throws BasicException;
}

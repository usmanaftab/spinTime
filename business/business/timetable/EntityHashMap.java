/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable;

import business.timetable.department.Departments;
import business.timetable.semester.Semester;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface EntityHashMap<K, V> extends IHashmap<K, V> {

    public EntityHashMap<K, V> findBy(Semester semester, Departments departments);

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.common;

import java.util.EventListener;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface FilterChangedListener extends EventListener {

    public void filterSemesterChanged(FilterChangedEvent evt);

    public void filterDepartmentChanged(FilterChangedEvent evt);
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable;

import dal.timetable.db.Session;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public interface AppView {

    public void init();

    public AppPanel getPanel(String string);

    public void setPanel(AppPanel panel, String name);

    public Session getSession();
}

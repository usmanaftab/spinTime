/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui.timetable.common;

import business.timetable.EntityObject;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SelectionChangedEvent {

    private EntityObject entityObject;

    public SelectionChangedEvent(EntityObject entityObject) {
        this.entityObject = entityObject;
    }

    public EntityObject getEntityObject() {
        return entityObject;
    }
}

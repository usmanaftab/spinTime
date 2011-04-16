/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package business.timetable.constraints;

import business.timetable.Entity;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ConstraintMultiValProperty {

    private String name;
    private Entity entity;

    public ConstraintMultiValProperty(String name, Entity entity) {
        this.name = name;
        this.entity = entity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ConstraintMultiValProperty createConstraintMultiValProperty(HashMap<String, Object> tokens) {
        Entity e = Entity.valueOf((Integer) tokens.get("rightconstraintentityID")
                , (String) tokens.get("rightconstraintentityname")
                , (String) tokens.get("rightconstraintentitydisplayname"), 2);

        return new ConstraintMultiValProperty((String) tokens.get("attributename"), e);
    }

}

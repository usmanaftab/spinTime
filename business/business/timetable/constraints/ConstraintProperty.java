/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints;

import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ConstraintProperty {

    private String name;
    private AttributeDType attributeDType;

    public ConstraintProperty(String name, AttributeDType attributeDType) {
        this.name = name;
        this.attributeDType = attributeDType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AttributeDType getAttributeDType() {
        return attributeDType;
    }

    public void setAttributeDType(AttributeDType attributeDType) {
        this.attributeDType = attributeDType;
    }

    @Override
    public String toString() {
        return name;
    }

    public static ConstraintProperty createConstraintProperty(HashMap<String, Object> tokens) {
        AttributeDType adt = AttributeDType.valueOf((Integer) tokens.get("constraintattributetypeID")
                , (String) tokens.get("constraintattributename"));

        return new ConstraintProperty((String) tokens.get("attributename"), adt);
    }
}

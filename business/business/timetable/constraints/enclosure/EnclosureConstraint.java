/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.enclosure;

import business.timetable.Entity;
import business.timetable.EntityObject;
import business.timetable.TimeTable;
import business.timetable.constraints.Constraint;
import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.ConstraintTypeOperator;
import business.timetable.representation.Keyable;
import business.timetable.representation.Representable;
import com.timetable.BasicException;
import com.timetable.ValidationException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.enclosure.EnclosureConstraintDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class EnclosureConstraint extends Constraint {

    private Entity leftEntity;
    private Entity rightEntity;
    private String attribName;
    private ConstraintTypeOperator constraintTypeOperator;

    public EnclosureConstraint() {
    }

    public EnclosureConstraint(int ID, String name, boolean hardConstraint, boolean apply, Entity leftEntity, Entity rightEntity, String attribName, ConstraintTypeOperator constraintTypeOperator) {
        super(ID, name, hardConstraint, apply);

        this.leftEntity = leftEntity;
        this.rightEntity = rightEntity;
        this.attribName = attribName;
        this.constraintTypeOperator = constraintTypeOperator;
    }

    public Entity getLeftEntity() {
        return leftEntity;
    }

    public void setLeftEntity(Entity leftEntity) {
        this.leftEntity = leftEntity;
    }

    public Entity getRightEntity() {
        return rightEntity;
    }

    public void setRightEntity(Entity rightEntity) {
        this.rightEntity = rightEntity;
    }

    public String getAttribName() {
        return attribName;
    }

    public void setAttribName(String attribName) {
        this.attribName = attribName;
    }

    public ConstraintTypeOperator getConstraintTypeOperator() {
        return constraintTypeOperator;
    }

    public void setConstraintTypeOperator(ConstraintTypeOperator constraintTypeOperator) {
        this.constraintTypeOperator = constraintTypeOperator;
    }

    @Override
    public void checkConstraint(TimeTable timeTable, Representable representation) {
        super.checkConstraint(timeTable, representation);

        for (EntityObject leftEntityObject : leftEntity.getEntities(timeTable)) {
            ArrayList<Keyable> rightEntityObjects = (ArrayList<Keyable>) leftEntity.getRightEntityObject(timeTable, representation, leftEntityObject, rightEntity);

            HashMap<Integer, Integer> multiValueProperty = leftEntityObject.getMultiValueProperty(attribName);
            if (multiValueProperty == null || multiValueProperty.size() == 0) {
                continue;
            }

            ArrayList badKeys = getBadKeys(multiValueProperty, rightEntityObjects);

            if (badKeys.size() > 0) {
                addBadKeys(badKeys);
            }
        }
    }

    private ArrayList getBadKeys(HashMap<Integer, Integer> list, ArrayList<Keyable> keys) {
        ArrayList badKeys = new ArrayList();
        switch (constraintTypeOperator) {
            case Inclusion:
                for (Keyable key : keys) {
                    if (list.get(key.getID()) == null) {
                        badKeys.add(key);
                    }
                }
                break;
            case Exclusion:
                for (Keyable key : keys) {
                    if (list.get(key.getID()) != null) {
                        badKeys.add(key);
                    }
                }
                break;
        }
        return badKeys;
    }

    private void addBadKeys(ArrayList badKeys) {
        switch (rightEntity) {
            case TimeSlot:
                addBadTimeSlots(badKeys);
                break;

            case ClassRoom:
                addBadClassRooms(badKeys);
                break;
        }
    }

    public static EnclosureConstraint createEnclosureConstraint(HashMap<String, Object> tokens) {
        Entity leftEntity = Entity.valueOf((Integer) tokens.get("leftentityID"),
                (String) tokens.get("leftentityname"), (String) tokens.get("leftentitydisplayname"), (Integer) tokens.get("leftentityposition"));
        Entity rightEntity = Entity.valueOf((Integer) tokens.get("rightentityID"),
                (String) tokens.get("rightentityname"), (String) tokens.get("rightentitydisplayname"), (Integer) tokens.get("rightentityposition"));
        ConstraintType constraintType = ConstraintType.valueOf((Integer) tokens.get("constrainttypeID"),
                (String) tokens.get("constrainttypename"));
        ConstraintTypeOperator constraintTypeOperator = ConstraintTypeOperator.valueOf((Integer) tokens.get("optID"),
                constraintType, (String) tokens.get("optname"), (String) tokens.get("optsign"));

        return new EnclosureConstraint((Integer) tokens.get("ID"), (String) tokens.get("name"), (Boolean) tokens.get("hardconstraint"), (Boolean) tokens.get("apply"),
                leftEntity, rightEntity, (String) tokens.get("attribname"), constraintTypeOperator);
    }

    public static EnclosureConstraint createDefaultConstraint() {
        EnclosureConstraint enclosureConstraint = new EnclosureConstraint();
        enclosureConstraint.setName("New Enclosure Constraint");
        enclosureConstraint.setHardConstraint(true);
        enclosureConstraint.setApply(true);
        enclosureConstraint.setLeftEntity(Entity.Teacher);
        enclosureConstraint.setRightEntity(Entity.TimeSlot);
        enclosureConstraint.setAttribName("Attribute Name");
        enclosureConstraint.setConstraintTypeOperator(ConstraintTypeOperator.Exclusion);
        return enclosureConstraint;
    }

    public static int save(Session session, EnclosureConstraint enclosureConstraint) throws ValidationException, BasicException {
        if (!validate(enclosureConstraint)) {
            return -1;
        }

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("ID", enclosureConstraint.getID());
        data.put("name", enclosureConstraint.getName());
        data.put("apply", enclosureConstraint.isApply());
        data.put("hardconstraint", enclosureConstraint.isHardConstraint());
        data.put("constrainttypeoperatorID", enclosureConstraint.constraintTypeOperator.getID());
        data.put("leftconstrainttypeentityID", enclosureConstraint.leftEntity.getID());
        data.put("rightconstrainttypeentityID", enclosureConstraint.rightEntity.getID());
        data.put("attributename", enclosureConstraint.attribName);

        EnclosureConstraintDataLogic enclosureConstraintDataLogic = new EnclosureConstraintDataLogic(session);
        if (enclosureConstraint.getID() == -1) {
            return enclosureConstraintDataLogic.insert(data);
        } else {
            return enclosureConstraintDataLogic.update(data);
        }
    }

    public static boolean validate(EnclosureConstraint enclosureConstraint) throws ValidationException {
        if (!Constraint.validate(enclosureConstraint)) {
            return false;
        }

        if (enclosureConstraint.constraintTypeOperator == null) {
            throw new ValidationException("Constraint operator must be specified.");
        }

        if (enclosureConstraint.leftEntity == null) {
            throw new ValidationException("Left enity must be specified.");
        }

        if (enclosureConstraint.rightEntity == null) {
            throw new ValidationException("Right enity must be specified.");
        }

        if (enclosureConstraint.attribName == null || enclosureConstraint.attribName.isEmpty()) {
            throw new ValidationException("Enity attribute must be specified.");
        }

        return true;

    }
}

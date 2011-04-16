/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.general;

import business.timetable.constraints.AttributeDType;
import business.timetable.TimeTable;
import business.timetable.constraints.Constraint;
import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.ConstraintTypeOperator;
import business.timetable.Entity;
import business.timetable.EntityObject;
import business.timetable.classrooms.ClassRoom;
import business.timetable.representation.Keyable;
import business.timetable.representation.Representable;
import business.timetable.timeslots.Day;
import business.timetable.timeslots.DayComparator;
import business.timetable.timeslots.Time;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import com.timetable.ValidationException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.general.GeneralConstraintDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GeneralConstraint extends Constraint {

    private Entity leftEntity;
    private Entity rightEntity;
    private String leftAttribName;
    private String rightAttribName;
    private AttributeDType attribDType;
    private ConstraintTypeOperator constraintTypeOperator;

    public GeneralConstraint() {
    }

    public GeneralConstraint(int ID, String name, boolean hardConstraint, boolean apply, Entity leftEntity, Entity rightEntity, String leftAttribName, String rightAttribName, AttributeDType attribType, ConstraintTypeOperator constraintTypeOperator) {
        super(ID, name, hardConstraint, apply);

        this.leftEntity = leftEntity;
        this.rightEntity = rightEntity;
        this.leftAttribName = leftAttribName;
        this.rightAttribName = rightAttribName;
        this.attribDType = attribType;
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

    public String getLeftAttribName() {
        return leftAttribName;
    }

    public void setLeftAttribName(String leftAttribName) {
        this.leftAttribName = leftAttribName;
    }

    public String getRightAttribName() {
        return rightAttribName;
    }

    public void setRightAttribName(String rightAttribName) {
        this.rightAttribName = rightAttribName;
    }

    public AttributeDType getAttribDType() {
        return attribDType;
    }

    public void setAttribDType(AttributeDType attribDType) {
        this.attribDType = attribDType;
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

            String leftAttrib = leftEntityObject.getProperty(leftAttribName);
            if (leftAttrib == null) {
                continue;
            }

            for (Keyable key : rightEntityObjects) {
                String rightAttrib = ((EntityObject) key).getProperty(rightAttribName);
                if (rightAttrib == null) {
                    continue;
                }

                if (!isValid(leftAttrib, rightAttrib)) {
                    addBadKey(key);
                }
            }
        }
    }

    private boolean isValid(String leftAttrib, String rightAttrib) {
        Integer result = getResult(leftAttrib, rightAttrib);
        if (result == null) {
            return false;
        }

        switch (constraintTypeOperator) {
            case Equal:
                if (result == 0) {
                    return true;
                }
                break;

            case NotEqual:
                if (result != 0) {
                    return true;
                }
                break;

            case LessThan:
                if (result < 0) {
                    return true;
                }
                break;

            case LessThanEqual:
                if (result <= 0) {
                    return true;
                }
                break;

            case GreaterThan:
                if (result > 0) {
                    return true;
                }
                break;

            case GreaterThanEqual:
                if (result >= 0) {
                    return true;
                }
                break;
        }

        return false;
    }

    private Integer getResult(String leftAttrib, String rightAttrib) {
        switch (attribDType) {
            case Integer:
                Integer leftInt = Integer.valueOf(leftAttrib);
                Integer rightInt = Integer.valueOf(rightAttrib);

                return leftInt.compareTo(rightInt);

            case Double:
                Double leftDouble = Double.valueOf(leftAttrib);
                Double riDouble = Double.valueOf(rightAttrib);

                return leftDouble.compareTo(riDouble);

            case String:
                String leftString = leftAttrib;
                String rightString = rightAttrib;

                return leftString.compareTo(rightString);

            case Time:
                Time leftTime = new Time(leftAttrib);
                Time rightTime = new Time(rightAttrib);

                return leftTime.compareTo(rightTime);

            case Day:
                Day leftDay = Day.valueOf(leftAttrib);
                Day rightDay = Day.valueOf(rightAttrib);

                DayComparator dc = new DayComparator();
                return dc.compare(leftDay, rightDay);

            default:
                return null;
        }
    }

    private void addBadKey(Keyable key) {
        switch (rightEntity) {
            case TimeSlot:
                ArrayList<TimeSlot> timeSlots = new ArrayList<TimeSlot>();
                timeSlots.add((TimeSlot) key);
                addBadTimeSlots(timeSlots);
                break;
            case ClassRoom:
                ArrayList<ClassRoom> classRooms = new ArrayList<ClassRoom>();
                classRooms.add((ClassRoom) key);
                addBadClassRooms(classRooms);
                break;
        }
    }

    public static GeneralConstraint createGeneralConstraint(HashMap<String, Object> tokens) {
        Entity leftEntity = Entity.valueOf((Integer) tokens.get("leftentityID"),
                (String) tokens.get("leftentityname"), (String) tokens.get("leftentitydisplayname"), (Integer) tokens.get("leftentityposition"));
        Entity rightEntity = Entity.valueOf((Integer) tokens.get("rightentityID"),
                (String) tokens.get("rightentityname"), (String) tokens.get("rightentitydisplayname"), (Integer) tokens.get("rightentityposition"));
        ConstraintType constraintType = ConstraintType.valueOf((Integer) tokens.get("constrainttypeID"),
                (String) tokens.get("constrainttypename"));
        ConstraintTypeOperator constraintTypeOperator = ConstraintTypeOperator.valueOf((Integer) tokens.get("optID"),
                constraintType, (String) tokens.get("optname"), (String) tokens.get("optsign"));
        AttributeDType attributeDType = AttributeDType.valueOf((Integer) tokens.get("attribdtypeID"),
                (String) tokens.get("attribdtypename"));

        return new GeneralConstraint((Integer) tokens.get("ID"), (String) tokens.get("name"),
                (Boolean) tokens.get("hardconstraint"), (Boolean) tokens.get("apply"),
                leftEntity, rightEntity, (String) tokens.get("leftattribname"),
                (String) tokens.get("rightattribname"), attributeDType, constraintTypeOperator);
    }

    public static GeneralConstraint createDefaultConstraint() {
        GeneralConstraint generalConstraint = new GeneralConstraint();
        generalConstraint.setName("New General Constraint");
        generalConstraint.setHardConstraint(true);
        generalConstraint.setApply(true);
        generalConstraint.setLeftEntity(Entity.Teacher);
        generalConstraint.setRightEntity(Entity.TimeSlot);
        generalConstraint.setAttribDType(AttributeDType.Integer);
        generalConstraint.setConstraintTypeOperator(ConstraintTypeOperator.Equal);
        return generalConstraint;
    }

    public static int save(Session session, GeneralConstraint generalConstraint) throws ValidationException, BasicException {
        if (!validate(generalConstraint)) {
            return -1;
        }

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("ID", generalConstraint.getID());
        data.put("name", generalConstraint.getName());
        data.put("apply", generalConstraint.isApply());
        data.put("hardconstraint", generalConstraint.isHardConstraint());
        data.put("constrainttypeoperatorID", generalConstraint.constraintTypeOperator.getID());
        data.put("leftconstrainttypeentityID", generalConstraint.leftEntity.getID());
        data.put("rightconstrainttypeentityID", generalConstraint.rightEntity.getID());
        data.put("leftattributename", generalConstraint.leftAttribName);
        data.put("rightattributename", generalConstraint.rightAttribName);
        data.put("constraintattributetypeID", generalConstraint.attribDType.getID());

        GeneralConstraintDataLogic generalConstraintDataLogic = new GeneralConstraintDataLogic(session);
        if (generalConstraint.getID() == -1) {
            return generalConstraintDataLogic.insert(data);
        } else {
            return generalConstraintDataLogic.update(data);
        }
    }

    public static boolean validate(GeneralConstraint generalConstraint) throws ValidationException {
        if (!Constraint.validate(generalConstraint)) {
            return false;
        }

        if (generalConstraint.constraintTypeOperator == null) {
            throw new ValidationException("Constraint operator must be specified.");
        }

        if (generalConstraint.leftEntity == null) {
            throw new ValidationException("Left enity must be specified.");
        }

        if (generalConstraint.leftAttribName == null || generalConstraint.leftAttribName.isEmpty()) {
            throw new ValidationException("Left enity attribute must be specified.");
        }

        if (generalConstraint.rightEntity == null) {
            throw new ValidationException("Right enity must be specified.");
        }

        if (generalConstraint.rightAttribName == null || generalConstraint.rightAttribName.isEmpty()) {
            throw new ValidationException("Right enity attribute must be specified.");
        }

        if (generalConstraint.attribDType == null) {
            throw new ValidationException("Attribute type must be specified.");
        }

        return true;
    }
}

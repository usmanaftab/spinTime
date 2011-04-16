/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.limit;

import business.timetable.TimeTable;
import com.timetable.ValidationException;
import business.timetable.constraints.Constraint;
import business.timetable.Entity;
import business.timetable.EntityObject;
import business.timetable.constraints.TimeSlotType;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.limit.LimitConstraintDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class LimitConstraint extends Constraint {

    private Entity entity;
    private int minLimit;
    private int maxLimit;
    private TimeSlotType timeSlotType;

    private LimitConstraint() {
    }

    public LimitConstraint(int ID, String name, boolean hardConstraint, boolean apply, Entity entity,
            int minLimit, int maxLimit, TimeSlotType timeSlotType) {
        super(ID, name, hardConstraint, apply);

        this.entity = entity;
        this.minLimit = minLimit;
        this.maxLimit = maxLimit;
        this.timeSlotType = timeSlotType;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getMinLimit() {
        return minLimit;
    }

    public void setMinLimit(int minLimit) {
        this.minLimit = minLimit;
    }

    public int getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(int maxLimit) {
        this.maxLimit = maxLimit;
    }

    public TimeSlotType getTimeSlotType() {
        return timeSlotType;
    }

    public void setTimeSlotType(TimeSlotType timeSlotType) {
        this.timeSlotType = timeSlotType;
    }

    @Override
    public void checkConstraint(TimeTable timeTable, Representable representation) {
        super.checkConstraint(timeTable, representation);

        for (EntityObject entityObject : entity.getEntities(timeTable)) {
            ArrayList<TimeSlot> timeSlots = (ArrayList<TimeSlot>) entity.getTimeSlots(timeTable, representation, entityObject);
            TimeSlotOccurences timeSlotOccurences = new TimeSlotOccurences(timeSlotType);
            timeSlotOccurences.setTimeSlotOccurences(timeSlots);

            for (int i = 0; i < timeSlotOccurences.getSize(); i++) {
                int occurences = timeSlotOccurences.getOccurencesAt(i);

                if (minLimit != -1 && occurences < minLimit) {
                    addBadTimeSlots(timeSlotOccurences.getTimeSlotsAt(i));
                }

                if (maxLimit != -1 && maxLimit < occurences) {
                    addBadTimeSlots(timeSlotOccurences.getTimeSlotsAt(i));
                }
            }
        }
    }

    public static LimitConstraint createLimitConstraint(HashMap<String, Object> tokens) {
        Entity entity = Entity.valueOf((Integer) tokens.get("entityID"),
                (String) tokens.get("entityname"), (String) tokens.get("entitydisplayname"), (Integer) tokens.get("entityposition"));
        TimeSlotType timeSlotType = TimeSlotType.valueOf((Integer) tokens.get("timeslottypeID"), (String) tokens.get("timeslottypename"));

        return new LimitConstraint((Integer) tokens.get("ID"), (String) tokens.get("name"),
                (Boolean) tokens.get("hardconstraint"), (Boolean) tokens.get("apply"),
                entity, (Integer) tokens.get("minlimit"), (Integer) tokens.get("maxlimit"),
                timeSlotType);
    }

    public static LimitConstraint createDefaultConstraint() {
        LimitConstraint limitConstraint = new LimitConstraint();
        limitConstraint.setName("New Limit Constraint");
        limitConstraint.setEntity(Entity.Teacher);
        limitConstraint.setMinLimit(-1);
        limitConstraint.setMaxLimit(-1);
        limitConstraint.setTimeSlotType(TimeSlotType.TimeSlot);
        return limitConstraint;
    }

    public static int save(Session session, LimitConstraint limitConstraint) throws ValidationException, BasicException {
        if (!validate(limitConstraint)) {
            return -1;
        }

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("ID", limitConstraint.getID());
        data.put("name", limitConstraint.getName());
        data.put("apply", limitConstraint.isApply());
        data.put("hardconstraint", limitConstraint.isHardConstraint());
        data.put("constrainttypeentityID", limitConstraint.getEntity().getID());
        data.put("minlimit", limitConstraint.getMinLimit());
        data.put("maxlimit", limitConstraint.getMaxLimit());
        data.put("constrainttimeslottypeID", limitConstraint.getTimeSlotType().getID());

        LimitConstraintDataLogic limitConstraintDataLogic = new LimitConstraintDataLogic(session);
        if (limitConstraint.getID() == -1) {
            return limitConstraintDataLogic.insert(data);
        } else {
            return limitConstraintDataLogic.update(data);
        }
    }

    public static boolean validate(LimitConstraint limitConstraint) throws ValidationException {
        if (!Constraint.validate(limitConstraint)) {
            return false;
        }

        if (limitConstraint.entity == null) {
            throw new ValidationException("Entity must be specified.");
        }

        if (limitConstraint.minLimit < -1) {
            throw new ValidationException("Minimum limit must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (limitConstraint.maxLimit < -1) {
            throw new ValidationException("Maximum limit must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (limitConstraint.minLimit == -1 && limitConstraint.maxLimit == -1) {
            throw new ValidationException("Either max or min limit must be specified.");
        }

        if (limitConstraint.maxLimit != -1 && limitConstraint.minLimit != -1) {
            if (limitConstraint.minLimit > limitConstraint.maxLimit) {
                throw new ValidationException("Minimum limit must be less than max limit.");
            }
        }

        if (limitConstraint.timeSlotType == null) {
            throw new ValidationException("Timeslot Type must be specified.");
        }

        return true;
    }
}

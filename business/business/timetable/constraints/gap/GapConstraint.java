/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.TimeTable;
import business.timetable.constraints.Constraint;
import business.timetable.constraints.ConstraintType;
import business.timetable.constraints.ConstraintTypeOperator;
import business.timetable.Entity;
import business.timetable.EntityObject;
import business.timetable.constraints.TimeSlotType;
import business.timetable.representation.Representable;
import business.timetable.timeslots.TimeSlot;
import com.timetable.BasicException;
import com.timetable.ValidationException;
import dal.timetable.db.Session;
import dal.timetable.db.constraints.gap.GapConstraintDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapConstraint extends Constraint {

    private Entity entity;
    private int minGap;
    private int maxGap;
    private int minSuccession;
    private int maxSuccession;
    private TimeSlotType timeSlotType;

    public GapConstraint() {
    }

    public GapConstraint(int ID, String name, boolean hardConstraint, boolean apply, Entity entity,
            int minGap, int maxGap, int minSuccession, int maxSuccession, TimeSlotType timeSlotType) {
        super(ID, name, hardConstraint, apply);

        this.entity = entity;
        this.minGap = minGap;
        this.maxGap = maxGap;
        this.minSuccession = minSuccession;
        this.maxSuccession = maxSuccession;
        this.timeSlotType = timeSlotType;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public int getMinGap() {
        return minGap;
    }

    public void setMinGap(int minGap) {
        this.minGap = minGap;
    }

    public int getMaxGap() {
        return maxGap;
    }

    public void setMaxGap(int maxGap) {
        this.maxGap = maxGap;
    }

    public int getMinSuccession() {
        return minSuccession;
    }

    public void setMinSuccession(int minSuccession) {
        this.minSuccession = minSuccession;
    }

    public int getMaxSuccession() {
        return maxSuccession;
    }

    public void setMaxSuccession(int maxSuccession) {
        this.maxSuccession = maxSuccession;
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

        GapManager gapManager = new GapManager(timeSlotType);
        for (EntityObject entityObject : entity.getEntities(timeTable)) {
            ArrayList<TimeSlot> timeSlots = (ArrayList<TimeSlot>) entity.getTimeSlots(timeTable, representation, entityObject);
            gapManager.setTimeSlotEnteries(timeSlots);

            for (int i = 0; i < gapManager.rowSize(); i++) {
                int colSize = gapManager.colSize(i) - 1;
                if (colSize < 1) {
                    continue;
                }

                int count = 0;
                for (int j = 0; j < colSize; j++) {
                    int gap = gapManager.getGap(i, j, j + 1);
                    if (gap <= 0) {
                        count++;

                        if (maxSuccession != -1 && maxSuccession < count) {
                            addBadTimeSlots(gapManager.getTimeSlotsAt(i, j));
                        }

                        continue;
                    }

                    if (minGap != -1 && gap < minGap) {
                        addBadTimeSlots(gapManager.getTimeSlotsAt(i, j + 1));
                    }

                    if (maxGap != -1 && maxGap < gap) {
                        addBadTimeSlots(gapManager.getTimeSlotsAt(i, j + 1));
                    }

                    if (minSuccession != -1 && count < minSuccession) {
                        addBadTimeSlots(gapManager.getTimeSlotsAt(i, j + 1));
                    }
                    count = 0;
                }
            }
        }
    }

    public static GapConstraint createGapConstraint(HashMap<String, Object> tokens) {
        Entity entity = Entity.valueOf((Integer) tokens.get("entityID"), (String) tokens.get("entityname"), (String) tokens.get("entitydisplayname"), (Integer) tokens.get("entityposition"));
        TimeSlotType timeSlotType = TimeSlotType.valueOf((Integer) tokens.get("timeslottypeID"), (String) tokens.get("timeslottypename"));

        return new GapConstraint((Integer) tokens.get("ID"), (String) tokens.get("name"),
                (Boolean) tokens.get("hardconstraint"), (Boolean) tokens.get("apply"), entity,
                (Integer) tokens.get("mingap"), (Integer) tokens.get("maxgap"),
                (Integer) tokens.get("minsuc"), (Integer) tokens.get("maxsuc"),
                timeSlotType);
    }

    public static GapConstraint createDefaultConstraint() {
        GapConstraint gapConstraint = new GapConstraint();
        gapConstraint.setName("New Gap Constraint");
        gapConstraint.setHardConstraint(true);
        gapConstraint.setApply(true);
        gapConstraint.setEntity(Entity.Student);
        gapConstraint.setMinGap(-1);
        gapConstraint.setMaxGap(-1);
        gapConstraint.setMinSuccession(-1);
        gapConstraint.setMaxSuccession(-1);
        gapConstraint.setTimeSlotType(TimeSlotType.TimeSlot);
        return gapConstraint;
    }

    public static int save(Session session, GapConstraint gapConstraint) throws ValidationException, BasicException {
        if (!validate(gapConstraint)) {
            return -1;
        }

        HashMap<String, Object> data = new HashMap<String, Object>();

        data.put("ID", gapConstraint.getID());
        data.put("name", gapConstraint.getName());
        data.put("apply", gapConstraint.isApply());
        data.put("hardconstraint", gapConstraint.isHardConstraint());
        data.put("constrainttypeentityID", gapConstraint.getEntity().getID());
        data.put("mingap", gapConstraint.getMinGap());
        data.put("maxgap", gapConstraint.getMaxGap());
        data.put("minsuc", gapConstraint.getMinSuccession());
        data.put("maxsuc", gapConstraint.getMaxSuccession());
        data.put("constrainttimeslottypeID", gapConstraint.getTimeSlotType().getID());

        GapConstraintDataLogic gapConstraintDataLogic = new GapConstraintDataLogic(session);
        if (gapConstraint.getID() == -1) {
            return gapConstraintDataLogic.insert(data);
        } else {
            return gapConstraintDataLogic.update(data);
        }
    }

    public static boolean validate(GapConstraint gapConstraint) throws ValidationException {
        if (!Constraint.validate(gapConstraint)) {
            return false;
        }

        if (gapConstraint.entity == null) {
            throw new ValidationException("Entity must be specified.");
        }

        if (gapConstraint.minGap < -1) {
            throw new ValidationException("Minimum gap must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (gapConstraint.maxGap < -1) {
            throw new ValidationException("Maximum gap must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (gapConstraint.minSuccession < -1) {
            throw new ValidationException("Minimum succession must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (gapConstraint.maxSuccession < -1) {
            throw new ValidationException("Maximum succession must be greater or equal than 0.\nSpecify -1 if there is no mimimum limit.");
        }

        if (gapConstraint.minGap == -1 && gapConstraint.maxGap == -1) {
            if (gapConstraint.minSuccession == -1 && gapConstraint.maxSuccession == -1) {
                throw new ValidationException("All attributes cannot be left unspecified.");
            }
        }

        if(gapConstraint.minGap != -1 && gapConstraint.maxGap != -1){
            if(gapConstraint.minGap > gapConstraint.maxGap){
                throw new ValidationException("Minimum gap must be less than max gap.");
            }
        }

        if(gapConstraint.minSuccession != -1 && gapConstraint.maxSuccession != -1){
            if(gapConstraint.minSuccession > gapConstraint.maxSuccession){
                throw new ValidationException("Minimum gap must be less than max gap.");
            }
        }

        if (gapConstraint.timeSlotType == null) {
            throw new ValidationException("Timeslot Type must be specified.");
        }


        return true;
    }
}

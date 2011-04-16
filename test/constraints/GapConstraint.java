/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package constraints;

import business.timetable.constraints.TimeSlotType;
import business.timetable.constraints.gap.GapManager;
import business.timetable.semester.Semesters;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class GapConstraint {

    public static void main(String[] args) {
        try {
            Session session = new Session();
            Semesters semesters = Semesters.getData(session);
            TimeSlots timeSlots = TimeSlots.getData(session, semesters.getValueAt(0));
            ArrayList<TimeSlot> b = new ArrayList<TimeSlot>();
            GapManager gapManager = new GapManager(TimeSlotType.TimeSlot);
            ArrayList<TimeSlot> t = new ArrayList<TimeSlot>();
            t.add(timeSlots.get(1));
            t.add(timeSlots.get(2));
//        t.add(timeSlots.getValueAt(2));
            t.add(timeSlots.get(4));
            t.add(timeSlots.get(14));
            t.add(timeSlots.get(13));
            t.add(timeSlots.get(8));
            t.add(timeSlots.get(15));
            t.add(timeSlots.get(18));
            for (TimeSlot timeSlot : t) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
            System.out.println();
            int minGap = 1;
            int maxGap = 3;
            int minSuccession = 1;
            int maxSuccession = 2;
            gapManager.setTimeSlotEnteries(t);
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
                            b.addAll(gapManager.getTimeSlotsAt(i, j));
                        }
                        continue;
                    }
                    if (minGap != -1 && gap < minGap) {
                        b.addAll(gapManager.getTimeSlotsAt(i, j + 1));
                    }
                    if (maxGap != -1 && maxGap < gap) {
                        b.addAll(gapManager.getTimeSlotsAt(i, j + 1));
                    }
                    if (minSuccession != -1 && count < minSuccession) {
                        b.addAll(gapManager.getTimeSlotsAt(i, j + 1));
                    }
                    count = 0;
                }
            }
            for (TimeSlot timeSlot : b) {
                System.out.println(timeSlot.getID() + " " + timeSlot.getDay().toString() + " " + timeSlot.getBeginTime().toString() + " " + timeSlot.getEndTime().toString());
            }
        } catch (BasicException ex) {
            Logger.getLogger(GapConstraint.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

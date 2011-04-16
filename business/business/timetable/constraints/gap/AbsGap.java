/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.constraints.gap;

import business.timetable.timeslots.TimeSlot;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class AbsGap {

    protected HashMap<Integer, HashMap<Object, ArrayList<TimeSlot>>> timeSlotEnteries;
    protected HashMap<Integer, ArrayList<Object>> timeSlotColumnKeys;

    abstract void setTimeSlotEnteries(ArrayList<TimeSlot> timeSlots);

    abstract int getGap(int row, int col1, int col2);

    abstract Comparator getComparator();

    void reset() {
        timeSlotEnteries = new HashMap<Integer, HashMap<Object, ArrayList<TimeSlot>>>();
        timeSlotColumnKeys = new HashMap<Integer, ArrayList<Object>>();
    }

    int timeSlotEnteriesSize() {
        return timeSlotEnteries.size();
    }

    int getColSize(int row) {
        return timeSlotColumnKeys.get(getFirstIndexMaping(row)).size();
    }
    
    void sortKeys() {
        for (int i = 0; i < timeSlotColumnKeys.size(); i++) {
            ArrayList<Object> column = timeSlotColumnKeys.get(getFirstIndexMaping(i));
            Collections.sort(column, getComparator());
        }
    }

    ArrayList<TimeSlot> timeSlotsAt(int row, int col1) {
        int newRow = getFirstIndexMaping(row);
        Object newCol = getSecondIndexMapping(row, col1);

        return timeSlotEnteries.get(newRow).get(newCol);
    }

    protected void addTimeSlot(int row, Object colValue, TimeSlot timeSlot) {
        HashMap<Object, ArrayList<TimeSlot>> column = timeSlotEnteries.get(row);
        if (column == null) {
            column = new HashMap<Object, ArrayList<TimeSlot>>();
            timeSlotEnteries.put(row, column);
        }

        ArrayList<TimeSlot> arrayValues = column.get(colValue);
        if (arrayValues == null) {
            arrayValues = new ArrayList<TimeSlot>();
            column.put(colValue, arrayValues);
            ArrayList<Object> columnKeys = timeSlotColumnKeys.get(row);
            if (columnKeys == null) {
                columnKeys = new ArrayList<Object>();
                timeSlotColumnKeys.put(row, columnKeys);
            }
            columnKeys.add(colValue);
        }
        arrayValues.add(timeSlot);
    }

    protected int getFirstIndexMaping(int i){
        return (Integer) timeSlotColumnKeys.keySet().toArray()[i];
    }

    protected Object getSecondIndexMapping(int row, int col){
        return timeSlotColumnKeys.get(getFirstIndexMaping(row)).get(col);
    }
}

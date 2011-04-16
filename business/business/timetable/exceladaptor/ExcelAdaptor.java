/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.exceladaptor;

import business.timetable.TimeTable;
import business.timetable.classrooms.ClassRoom;
import business.timetable.courses.CourseLesson;
import business.timetable.representation.Representable;
import business.timetable.teachers.Teacher;
import business.timetable.timeslots.TimeSlot;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import jxl.*;
import jxl.write.*;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ExcelAdaptor {

    private Representable representation;
    private TimeSlots cols;
    private ClassRooms rows;

    public ExcelAdaptor(TimeTable timeTable, Representable representation) {
        cols = new TimeSlots(timeTable.getTimeSlots().getAllValues());
        rows = new ClassRooms(timeTable.getClassRooms().getAllValues());
        this.representation = representation;
    }

    private int getBeginDayRow(int dayNo) {
        return dayNo * rows.getList().size() + 1;
    }

    private int getEndDayRow(int dayNo) {
        return getBeginDayRow(dayNo) + rows.getList().size() - 1;
    }

    private int getRow(int dayNo, int rowNo) {
        return getBeginDayRow(dayNo) + rowNo;
    }

    private int getCol(int colNo){
        return colNo + 2;
    }

    private void init(WritableSheet sheet) throws WriteException {
        ArrayList<String> colList = cols.getList();
        ArrayList<String> rowList = rows.getList();

        for (int colIndex = 0; colIndex < colList.size(); colIndex++) {
            Label label = new Label(colIndex + 2, 0, colList.get(colIndex));
            sheet.addCell(label);
        }

        ArrayList<String> days = cols.getDays();
        for (int dayIndex = 0; dayIndex < days.size(); dayIndex++) {
            int beginDayRow = getBeginDayRow(dayIndex);
            int endDayRow = getEndDayRow(dayIndex);
            Label label = new Label(0, beginDayRow, days.get(dayIndex));
            sheet.addCell(label);
            sheet.mergeCells(0, beginDayRow, 0, endDayRow);

            for (int rowIndex = 0; rowIndex < rowList.size(); rowIndex++) {
                int row = getRow(dayIndex, rowIndex);
                label = new Label(1, row, rowList.get(rowIndex));
                sheet.addCell(label);
            }
        }
    }

    public void GenerateTimeTable() {
        try {
            WritableWorkbook workbook = Workbook.createWorkbook(new File("timetable.xls"));
            WritableSheet sheet = workbook.createSheet("Time Table", 0);

            init(sheet);

            for (int rowIndex = 0; rowIndex < rows.getClassRooms().size(); rowIndex++) {
                ClassRoom classRoom = rows.getClassRooms().get(rowIndex);
                for (int colIndex = 0; colIndex < cols.getTimeSlots().size(); colIndex++) {
                    TimeSlot timeSlot = cols.getTimeSlots().get(colIndex);
                    int col = getCol(cols.getCol(timeSlot));
                    int row = getRow( cols.getDay(timeSlot.getDay()),rows.getRow(classRoom));

                    CourseLesson courseLesson = representation.get(timeSlot, classRoom);
                    if (courseLesson != null) {
                        String text = courseLesson.toString() + " ";
                        for(Teacher teacher: courseLesson.getCourse().getTeachers()){
                            text += teacher.toString() + ", ";
                        }
                        
                        Label label = new Label(col, row, text);
                        sheet.addCell(label);
                    }
                }
            }

            workbook.write();
            workbook.close();
        } catch (IOException ex) {
            System.out.println("IOException while creating excel file.");
        } catch (WriteException ex) {
            System.out.println("WriteException while creating excel file.");
        }
    }
}

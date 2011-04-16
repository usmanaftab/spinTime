/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package migration.timetable;

import com.timtable.migration.CourseInfoExtractor;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class CourseStudentInfoMigration {

    public static void main(String[] args) {
        try {
            Workbook courseWorkbook = Workbook.getWorkbook(new File("input data\\Course Info - ext.xls"));
            Sheet courseSheet = courseWorkbook.getSheet("Sheet1");

            HashMap<String, HashMap<String, Object>> courses = new HashMap<String, HashMap<String, Object>>();
            for (int i = 1; i < courseSheet.getRows(); i++) {
                Cell[] row = courseSheet.getRow(i);

                if (row[0].getContents().isEmpty()) {
                    continue;
                }

                HashMap<String, Object> course = courses.get(row[0].getContents() + row[5].getContents());
                if (course == null) {
                    course = new HashMap<String, Object>();
                    courses.put(row[0].getContents() + row[5].getContents(), course);

                    course.put("ID", row[0].getContents());
                    course.put("name", row[1].getContents());
                    course.put("lessoncount", row[3].getContents());
                    course.put("timeduration", row[4].getContents());
                    course.put("department", row[5].getContents());
                }

                Integer sectionCount = (Integer) course.get("sectioncount");
                if (sectionCount == null) {
                    sectionCount = 0;
                }

                course.put("sectioncount", ++sectionCount);
            }

            Workbook studentWorkBook = Workbook.getWorkbook(new File("input data\\Student Info.xls"));
            Sheet studentSheet = studentWorkBook.getSheet("Sheet1");

            CourseInfoExtractor cnx = new CourseInfoExtractor();
            for (int i = 1; i < studentSheet.getRows(); i++) {
                Cell[] row = studentSheet.getRow(i);
                String ID = row[0].getContents();

                for (int j = 1; j < row.length; j++) {
                    if (!row[j].getContents().isEmpty()) {
                        cnx.updateContent(row[j].getContents());

                        HashMap<String, Object> course = courses.get(cnx.getID() + cnx.getDepartment());
                        ArrayList<String> students = (ArrayList<String>) course.get("students");
                        if (students == null) {
                            students = new ArrayList<String>();
                            course.put("students", students);
                        }

                        students.add(ID);
                    }
                }
            }

            WritableWorkbook destWorkBook = Workbook.createWorkbook(new File("new input data\\Course Info.xls"));
            WritableSheet destSheet = destWorkBook.createSheet("Sheet1", 0);

            Label label = new Label(0, 0, "ID");
            destSheet.addCell(label);
            label = new Label(1, 0, "Name");
            destSheet.addCell(label);
            label = new Label(2, 0, "Section Count");
            destSheet.addCell(label);
            label = new Label(3, 0, "Lesson Count");
            destSheet.addCell(label);
            label = new Label(4, 0, "Time Duration");
            destSheet.addCell(label);
            label = new Label(5, 0, "Capacity");
            destSheet.addCell(label);
            label = new Label(6, 0, "Department");
            destSheet.addCell(label);
            label = new Label(7, 0, "Students");
            destSheet.addCell(label);

            int rowIndex = 1;
            for (String key : courses.keySet()) {
                HashMap<String, Object> course = courses.get(key);

                label = new Label(0, rowIndex, (String) course.get("ID"));
                destSheet.addCell(label);
                label = new Label(1, rowIndex, (String) course.get("name"));
                destSheet.addCell(label);
                label = new Label(2, rowIndex, String.valueOf(course.get("sectioncount")));
                destSheet.addCell(label);
                label = new Label(3, rowIndex, (String) course.get("lessoncount"));
                destSheet.addCell(label);
                label = new Label(4, rowIndex, (String) course.get("timeduration"));
                destSheet.addCell(label);
                label = new Label(5, rowIndex, String.valueOf(50));
                destSheet.addCell(label);
                label = new Label(6, rowIndex, (String) course.get("department"));
                destSheet.addCell(label);

                ArrayList<String> students = (ArrayList<String>) course.get("students");
                if (students != null) {
                    for (String ID : students) {
                        label = new Label(7, rowIndex, ID);
                        destSheet.addCell(label);

                        rowIndex++;
                    }
                }
                rowIndex++;
            }

            destWorkBook.write();
            destWorkBook.close();
            courseWorkbook.close();
            studentWorkBook.close();

        } catch (IOException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

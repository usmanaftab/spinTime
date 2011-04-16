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
public class TeacherInfoMigration {

    public static void main(String[] agrs) {
        try {
            Workbook sourceWorkbook = Workbook.getWorkbook(new File("input data\\Teacher Info.xls"));
            Sheet sourceSheet = sourceWorkbook.getSheet("Sheet1");

            HashMap<String, ArrayList<String>> teachers = new HashMap<String, ArrayList<String>>();
            HashMap<String, Integer> courseSectionCount = new HashMap<String, Integer>();
            CourseInfoExtractor cnx = new CourseInfoExtractor();
            
            for (int i = 1; i < sourceSheet.getRows(); i++) {
                Cell[] row = sourceSheet.getRow(i);
                String name = row[0].getContents();

                ArrayList<String> teacher = teachers.get(name);
                if (teacher == null) {
                    teacher = new ArrayList<String>();
                    teachers.put(name, teacher);
                }

                for (int j = 2; j < row.length; j++) {
                    if (!row[j].getContents().isEmpty()) {
                        teacher.add(row[j].getContents());

                        cnx.updateContent(row[j].getContents());
                        Integer count = courseSectionCount.get(cnx.getID()+cnx.getDepartment());
                        if(count == null){
                            count = 0;
                        }
                        courseSectionCount.put(cnx.getID()+cnx.getDepartment(), ++count);
                    }
                }
            }

            WritableWorkbook destWorkBook = Workbook.createWorkbook(new File("new input data\\Teacher Info.xls"));
            WritableSheet destSheet = destWorkBook.createSheet("Sheet1", 0);

            Label label = new Label(0, 0, "Name");
            destSheet.addCell(label);
            label = new Label(1, 0, "Course");
            destSheet.addCell(label);
            label = new Label(2, 0, "Section");
            destSheet.addCell(label);
            label = new Label(3, 0, "Department");
            destSheet.addCell(label);

            int rowIndex = 1;
            for (String name : teachers.keySet()) {
                ArrayList<String> teacher = teachers.get(name);

                label = new Label(0, rowIndex, name);
                destSheet.addCell(label);
                for(String course : teacher){
                    cnx.updateContent(course);

                    label = new Label(1, rowIndex, cnx.getID());
                    destSheet.addCell(label);

                    Integer count = courseSectionCount.get(cnx.getID()+cnx.getDepartment());
                    String section = String.valueOf((char) (count + 'A' - 1));
                    courseSectionCount.put(cnx.getID()+cnx.getDepartment(), --count);
                    
                    label = new Label(2, rowIndex, section);
                    destSheet.addCell(label);
                    label = new Label(3, rowIndex, cnx.getDepartment());
                    destSheet.addCell(label);

                    rowIndex++;
                }
            }

            destWorkBook.write();
            destWorkBook.close();
            sourceWorkbook.close();

        } catch (IOException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(TeacherInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}

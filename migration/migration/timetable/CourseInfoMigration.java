/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package migration.timetable;

import com.timtable.migration.CourseInfoExtractor;
import java.io.File;
import java.io.IOException;
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
public class CourseInfoMigration {

    public static void main(String[] agrs) {
        try {
            Workbook sourceWorkBook = Workbook.getWorkbook(new File("input data\\Teacher Info.xls"));
            Sheet sourceSheet = sourceWorkBook.getSheet("Sheet1");

            WritableWorkbook destWorkBook = Workbook.createWorkbook(new File("input data\\Course Info.xls"));
            WritableSheet destSheet = destWorkBook.createSheet("Sheet1", 0);

            Label label = new Label(0, 0, "ID");
            destSheet.addCell(label);
            label = new Label(1, 0, "Name");
            destSheet.addCell(label);
            label = new Label(2, 0, "Section");
            destSheet.addCell(label);
            label = new Label(3, 0, "Department");
            destSheet.addCell(label);

            int rowIndex = 1;

            for (int i = 1; i < sourceSheet.getRows(); i++) {
                Cell[] row = sourceSheet.getRow(i);
                CourseInfoExtractor cnx = new CourseInfoExtractor();
                for (int j = 2; j < row.length; j++) {
                    String content = row[j].getContents();

                    if (content.isEmpty()) {
                        continue;
                    }
                    
                    cnx.updateContent(content);

                    label = new Label(0, rowIndex, cnx.getID());
                    destSheet.addCell(label);
                    label = new Label(1, rowIndex, cnx.getName());
                    destSheet.addCell(label);
                    label = new Label(2, rowIndex, cnx.getSection());
                    destSheet.addCell(label);
                    label = new Label(3, rowIndex, cnx.getDepartment());
                    destSheet.addCell(label);

                    rowIndex++;
                }
            }

            destWorkBook.write();
            destWorkBook.close();
            sourceWorkBook.close();


        } catch (IOException ex) {
            Logger.getLogger(CourseInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (BiffException ex) {
            Logger.getLogger(CourseInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        } catch (WriteException ex) {
            Logger.getLogger(CourseInfoMigration.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public abstract class Migration implements IMigration {

    private Session session;
    private String fileName;
    private Semester semester;
    private final String sheetName;
    private ArrayList<String> header;
    private ArrayList<HashMap<String, ArrayList<String>>> data;

    protected abstract void beginMigration() throws BasicException;

    protected abstract void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException;

    protected abstract void endMigration() throws BasicException;

    public Migration(Session session) {
        this(session, null, null);
    }

    public Migration(Session session, String fileName){
        this(session, null, fileName);
    }

    public Migration(Session session, Semester semester, String fileName) {
        this.session = session;
        this.semester = semester;
        this.fileName = fileName;
        sheetName = "Sheet1";
    }

    public Session getSession() {
        return session;
    }

    public Semester getSemester() {
        return semester;
    }

    public void setSemester(Semester semester) {
        this.semester = semester;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public ArrayList<String> getHeader() {
        return header;
    }

    public ArrayList<HashMap<String, ArrayList<String>>> getData() throws BasicException {
        try {
            Workbook sourceWorkBook = Workbook.getWorkbook(new File(fileName));
            Sheet sourceSheet = sourceWorkBook.getSheet(sheetName);

            header = new ArrayList<String>();
            if (sourceSheet.getRows() > 1) {
                addRow(header, sourceSheet, 0);
            }

            ArrayList<HashMap<String, ArrayList<String>>> data = new ArrayList<HashMap<String, ArrayList<String>>>();
            HashMap<String, ArrayList<String>> rowData = new HashMap<String, ArrayList<String>>();
            for (int i = 1; i < sourceSheet.getRows(); i++) {
                Cell[] row = sourceSheet.getRow(i);
                if(row.length == 0){
                    continue;
                }
                
                if (!row[0].getContents().isEmpty()) {
                    rowData = new HashMap<String, ArrayList<String>>();
                    data.add(rowData);
                }

                for (int j = 0; j < row.length; j++) {
                    if (row[j].getContents().isEmpty()) {
                        continue;
                    }

                    ArrayList<String> attributeData = rowData.get(header.get(j));
                    if(attributeData == null){
                        attributeData = new ArrayList<String>();
                        rowData.put(header.get(j), attributeData);
                    }

                    attributeData.add(row[j].getContents());
                }
            }
            return data;

        } catch (IOException ex) {
            throw new BasicException(Migration.class.getName(), ex);
        } catch (BiffException ex) {
            throw new BasicException(Migration.class.getName(), ex);
        }
    }

    private void addRow(ArrayList<String> list, Sheet sheet, int rowNo) {
        Cell[] row = sheet.getRow(rowNo);

        for (int j = 0; j < row.length; j++) {
            String content = row[j].getContents();

            if (content.isEmpty()) {
                continue;
            }

            list.add(content);
        }
    }

    protected HashMap<String, ArrayList<String>> getRowAt(int i){
        return data.get(i);
    }

    public void migrate() throws BasicException {
        try {
            data = getData();
            beginMigration();
            for (HashMap<String, ArrayList<String>> row : data) {
                migrateObject(row);
            }
            endMigration();
        } catch (Exception ex){
            throw new BasicException(Migration.class.getName(), ex);
        }
    }
}

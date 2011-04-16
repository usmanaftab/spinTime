/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.migration.excel;

import business.timetable.semester.Semester;
import com.timetable.BasicException;
import dal.timetable.db.Session;
import dal.timetable.db.course.CourseDataLogic;
import dal.timetable.db.department.DepartmentDataLogic;
import dal.timetable.db.teacher.TeacherDataLogic;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class TeacherMigration extends Migration {

    private TeacherDataLogic teacherDataLogic;
    private CourseDataLogic courseDataLogic;
    private DepartmentDataLogic departmentDataLogic;
    private PropertyMigration propertyMigration;
    private MultiValuePropertyMigration multiValuePropertyMigration;
    private boolean migrateProperties;
    private boolean migreateMulProperties;


    public TeacherMigration(Session session){
        super(session);
        migrateProperties = false;
        migreateMulProperties = false;
    }

    public TeacherMigration(Session session, Semester semester, String fileName) {
        super(session, semester, fileName);
    }

    @Override
    protected void beginMigration() throws BasicException {
        propertyMigration = new PropertyMigration(getSession(), "Course", getHeader());
        multiValuePropertyMigration = new MultiValuePropertyMigration(getSession(), "Course", getHeader());
        int index  = 4;
        if (getHeader().size() > index) {
            HashMap<String, ArrayList<String>> row = getRowAt(0);
            int count = Integer.parseInt(row.get(getHeader().get(index)).get(0));
            propertyMigration.setStartIndex(index + 1);
            propertyMigration.setEndIndex(count);
            migrateProperties = true;

            int startIndex = index + 1 + count * 2;
            if (getHeader().size() > startIndex) {
                multiValuePropertyMigration.setStartIndex(startIndex);
                multiValuePropertyMigration.setEndIndex(getHeader().size());
                migreateMulProperties = true;
            }
        }
        
        teacherDataLogic = new TeacherDataLogic(getSession());
        courseDataLogic = new CourseDataLogic(getSession());
        departmentDataLogic = new DepartmentDataLogic(getSession());
        teacherDataLogic.deleteSemesterData(getSemester().getID());
    }

    @Override
    protected void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("name", row.get(getHeader().get(0)).get(0));

        int ID = teacherDataLogic.isAlreadyPresent(data);
        if(ID == -1){
            ID = teacherDataLogic.insert(data);
        }

        data.put("ID", ID);

        ArrayList<String> courses = row.get(getHeader().get(1));
        ArrayList<String> sections = row.get(getHeader().get(2));
        ArrayList<String> depts = row.get(getHeader().get(3));
        data.put("semesterID", getSemester().getID());

        HashMap<String, Object> courseData = new HashMap<String, Object>();
        courseData.put("semesterID", getSemester().getID());
        for(int i = 0; i < courses.size(); i++){
            String courseID = courses.get(i);
            String section = sections.get(i);
            int deptID = departmentDataLogic.getID(depts.get(i));

            courseData.put("ID", courseID);
            courseData.put("section", section);
            courseData.put("departmentID", deptID);
            int courseSecID = (Integer) courseDataLogic.getCourseSectionRow(courseData).get("ID");
            
            data.put("courseID", courseSecID);

            teacherDataLogic.insertTeacherCourse(data);
        }

        propertyMigration.deleteProperties(String.valueOf(ID));
        if (migrateProperties) {
            propertyMigration.migrate(row, String.valueOf(ID));
        }

        multiValuePropertyMigration.deleteProperties(String.valueOf(ID));
        if(migreateMulProperties){
            multiValuePropertyMigration.migrate(row, String.valueOf(ID));
        }       
    }

    @Override
    protected void endMigration() throws BasicException {
    }
}

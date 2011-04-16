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
import dal.timetable.db.student.StudentDataLogic;
import dal.timetable.db.teacher.TeacherDataLogic;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class CourseMigration extends Migration {

    private CourseDataLogic courseDataLogic;
    private DepartmentDataLogic deptDataLogic;
    private StudentDataLogic studentDataLogic;
    private TeacherDataLogic teacherDataLogic;
    private PropertyMigration propertyMigration;
    private MultiValuePropertyMigration multiValuePropertyMigration;
    private boolean migrateProperties;
    private boolean migreateMulProperties;

    public CourseMigration(Session session) {
        super(session);
    }

    public CourseMigration(Session session, Semester semester, String fileName) {
        super(session, semester, fileName);
        migrateProperties = false;
        migreateMulProperties = false;
    }

    @Override
    protected void beginMigration() throws BasicException {
        propertyMigration = new PropertyMigration(getSession(), "Course", getHeader());
        multiValuePropertyMigration = new MultiValuePropertyMigration(getSession(), "Course", getHeader());
        int index = 8;
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

        courseDataLogic = new CourseDataLogic(getSession());
        deptDataLogic = new DepartmentDataLogic(getSession());
        studentDataLogic = new StudentDataLogic(getSession());
        teacherDataLogic = new TeacherDataLogic(getSession());
        studentDataLogic.deleteStudentSections(getSemester().getID());
        teacherDataLogic.deleteSemesterData(getSemester().getID());
        courseDataLogic.deleteCourseSection(getSemester().getID());
    }

    @Override
    protected void migrateObject(HashMap<String, ArrayList<String>> row) throws BasicException {
        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("ID", row.get(getHeader().get(0)).get(0));
        data.put("name", row.get(getHeader().get(1)).get(0));
        data.put("lessoncount", Integer.parseInt(row.get(getHeader().get(3)).get(0)));
        data.put("duration", row.get(getHeader().get(4)).get(0));
        data.put("capacity", Integer.parseInt(row.get(getHeader().get(5)).get(0)));
        data.put("departmentID", deptDataLogic.getID(row.get(getHeader().get(6)).get(0)));
        data.put("semesterID", getSemester().getID());

        String courseID = courseDataLogic.isAlreadyExist(data);
        if(courseID == null){
            courseID = courseDataLogic.insert(data);
        } else {
            courseDataLogic.update(data);
        }
        
        data.put("ID", courseID);

        int sectionCount = Integer.parseInt(row.get(getHeader().get(2)).get(0));
        ArrayList<String> students = row.get(getHeader().get(7));
        if(students == null){
            students = new ArrayList<String>();
        }
        Collections.sort(students);

        int studentPerSection = students.size() / sectionCount;
        ArrayList<List<String>> sectionStudents = new ArrayList<List<String>>();
        for (int i = 0; i < sectionCount; i++) {
            List<String> s = students.subList(i * studentPerSection, (i + 1) * studentPerSection);
            sectionStudents.add(s);
        }
        
        HashMap<String, Object> studentData = new HashMap<String, Object>();
        studentData.put("semesterID", getSemester().getID());
        for(int i = 0; i < sectionCount; i++){
            String section = String.valueOf((char) (i + 'A'));
            data.put("section", section);
            int courseSectionID = courseDataLogic.insertCourseSection(data);
            studentData.put("coursesectionID", courseSectionID);

            for(String studentID : sectionStudents.get(i)){

                studentData.put("ID", studentID);
                studentData.put("studentID", studentID);
                if(!studentDataLogic.isAlreadyExist(studentData)){
                    studentDataLogic.insert(studentData);
                }
                studentDataLogic.insertStudentCourse(studentData);
            }
        }

        propertyMigration.deleteProperties(courseID);
        if (migrateProperties) {
            propertyMigration.migrate(row, courseID);
        }

        multiValuePropertyMigration.deleteProperties(courseID);
        if(migreateMulProperties){
            multiValuePropertyMigration.migrate(row, courseID);
        }
    }

    @Override
    protected void endMigration() throws BasicException {
    }
}

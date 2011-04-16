/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.simulatedannealingttg;

import business.timetable.representation.Representation;
import business.timetable.simulatedannealingttg.clashes.Clashes;
import business.timetable.*;
import business.timetable.classrooms.ClassRoom;
import business.timetable.classrooms.ClassRooms;
import business.timetable.constraints.specific.SpecificConstraints;
import business.timetable.courses.Courses;
import business.timetable.courses.Course;
import business.timetable.courses.CourseLesson;
import business.timetable.department.Departments;
import business.timetable.exceladaptor.ExcelAdaptor;
import business.timetable.semester.Semester;
import business.timetable.simulatedannealingttg.clashes.ClashType;
import business.timetable.students.Students;
import business.timetable.teachers.Teacher;
import business.timetable.teachers.Teachers;
import business.timetable.timeslots.TimeSlot;
import business.timetable.timeslots.TimeSlots;
import com.timetable.BasicException;
import com.timetable.utilz.AllValuesUsedException;
import com.timetable.utilz.Integer2D;
import com.timetable.utilz.NoRepeatRandom;
import com.timetable.utilz.NoRepeatRandom2D;
import dal.timetable.db.Session;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SimulatedAnnealingTTG implements ITimeTableGenerator, TimeTable {

    private Courses courses;
    private Teachers teachers;
    private Students students;
    private TimeSlots timeSlots;
    private ClassRooms classRooms;
    private Departments departments;
    private Semester semester;
    private Representation representation;
    private Session session;

    public SimulatedAnnealingTTG(Session session) {
        this.session = session;
    }

    private void init() throws BasicException {
        System.out.println("populating data.");
        timeSlots = TimeSlots.getData(session, semester);
//        timeSlots.printAll();
        
        classRooms = ClassRooms.getData(session, departments, semester);
//        classRooms.printAll();

        courses = Courses.getData(session, departments, semester);
//        courses.printAll();

        teachers = Teachers.getData(session, departments, semester, courses, timeSlots);
//        teachers.printAll();

        students = Students.getData(session, departments, semester, courses);
//        students.printAll();
    }

    public Representation getRepresentation() {
        return representation;
    }

    public void initRepresentation() throws BasicException {
        ArrayList<TimeSlot> timeKeys = timeSlots.getAllValues();
        ArrayList<ClassRoom> classKeys = classRooms.getAllValues();
        representation = new Representation(this, timeSlots, classRooms);
        NoRepeatRandom2D random = new NoRepeatRandom2D(timeKeys.size(), classKeys.size());
        Collections.sort(timeKeys);

        for (Teacher teacher : teachers) {
            for (Course course : teacher.getCourses()) {
                for (CourseLesson courseLesson : course.getCourseLessons()) {
                    try {
                        Integer2D num2D = random.nextInt2D();
                        TimeSlot timeSlot = timeSlots.get(timeKeys.get(num2D.x).getID());
                        ClassRoom classRoom = classRooms.get(classKeys.get(num2D.y).getID());
                        representation.put(timeSlot, classRoom, courseLesson);
                    } catch (AllValuesUsedException ex) {
                        System.out.println("All values used exception.");
                        throw new InsufficientClassesException();
                    }
                }
            }
        }

        SpecificConstraints specificConstraints = SpecificConstraints.getConstraints(getSession(), true);
        specificConstraints.applyConstraints(this, representation);
    }

    public int generateTimeTable() throws BasicException {
        init();
        initRepresentation();
        SAParameters hardParams = new SAParameters(SAParam.HARD);
        System.out.println("createFeasibleTimeTable");
        createFeasibleTimeTable(hardParams);
        SAParameters softParams = new SAParameters(SAParam.SOFT);
        System.out.println("optimizeFeasibleTimeTable");
        optimizeFeasibleTimeTable(softParams);
        return 0;
    }

    private void createFeasibleTimeTable(SAParameters params) throws BasicException {
        Clashes hardClashes = Clashes.getClashes(getSession(), this, representation, ClashType.HardClashes);
        Random random = new Random();

        while (hardClashes.count() > 0 || params.temperature > params.terminalTemperateure) {
            for (int i = 0; i < params.innerloopCount; i++) {
                TimeSlot timeSlot1 = getNewRandomBadTimeSlot1(random, hardClashes);
                TimeSlot timeSlot2 = getNewRandomTimeSlot2(random, timeSlot1);
                ArrayList<Integer> swapSeqs = generateSwapSequences(representation.getKeys2().size());

                for (int swapSeq : swapSeqs) {
                    Representation newRepresentation = representation.clone();
                    if (!swap(newRepresentation, timeSlot1, timeSlot2, swapSeq)) {
                        continue;
                    }
                    Clashes newHardClashes = Clashes.getClashes(getSession(), this, newRepresentation, ClashType.HardClashes);

                    int delta = newHardClashes.count() - hardClashes.count();
                    double prob = Math.exp((-1) * (delta / params.temperature));
                    if (delta <= 0 || prob > Math.random()) {
                        hardClashes = newHardClashes;
                        representation = newRepresentation;
                    }
                }
                if (hardClashes.count() == 0) {
                    break;
                }
            }
            params.temperature *= params.alpha;
        }
    }

    private TimeSlot getNewRandomBadTimeSlot1(Random random, Clashes hardClashes) {
        TimeSlot timeSlot = null;
        int randNum = 0;

        if (hardClashes.count() <= 0) {
            randNum = random.nextInt(representation.getKeys1().size());
            timeSlot = representation.getTimeSlotAt(randNum);
        } else {
            randNum = random.nextInt(hardClashes.getBadTimeSlotsSize());
            timeSlot = hardClashes.getBadTimeSlotAt(randNum);
        }

        return timeSlot;
    }

    private TimeSlot getNewRandomTimeSlot2(Random random, TimeSlot timeSlot1) {
        int randNum = 0;
        TimeSlot timeSlot2;
        do {
            randNum = random.nextInt(representation.getKeys1().size());
            timeSlot2 = representation.getTimeSlotAt(randNum);
        } while (timeSlot1 == timeSlot2);

        return timeSlot2;
    }

    private void optimizeFeasibleTimeTable(SAParameters params) throws BasicException {
        Clashes softClashes = Clashes.getClashes(getSession(), this, representation, ClashType.SoftClashes);
        NoRepeatRandom random = new NoRepeatRandom(representation.getKeys1().size());

        while (softClashes.count() > 0 && params.temperature > params.terminalTemperateure) {
            for (int count = 0; count < params.innerloopCount; count++) {
                ArrayList<TimeSlot> candidateList = getRandomlySortedTimeSlots(random);

                for (int i = 0; i < candidateList.size(); i++) {
                    for (int j = i + 1; j < candidateList.size(); j++) {
                        TimeSlot timeSlot1 = candidateList.get(i);
                        TimeSlot timeSlot2 = candidateList.get(j);
                        ArrayList<Integer> swapSeqs = generateSwapSequences(representation.getKeys2().size());

                        for (int swapSeq : swapSeqs) {
                            Representation newRepresentation = representation.clone();
                            if (!swap(newRepresentation, timeSlot1, timeSlot2, swapSeq)) {
                                continue;
                            }
                            Clashes hardClashes = Clashes.getClashes(getSession(), this, newRepresentation, ClashType.HardClashes);
                            if (hardClashes.count() > 0) {
                                continue;
                            }

                            Clashes newSoftClashes = Clashes.getClashes(getSession(), this, newRepresentation, ClashType.SoftClashes);
                            int delta = newSoftClashes.count() - softClashes.count();
                            double prob = Math.exp((-1) * (delta / params.temperature));
                            if (delta <= 0 || prob > Math.random()) {
                                softClashes = newSoftClashes;
                                representation = newRepresentation;
                            }
                        }
                    }
                }

                if (softClashes.count() == 0) {
                    break;
                }
            }
            params.temperature *= params.alpha;
        }
    }

    private ArrayList<TimeSlot> getRandomlySortedTimeSlots(NoRepeatRandom random) {
        random.restart();
        ArrayList<TimeSlot> results = new ArrayList<TimeSlot>();

        for (int index = 0; index < representation.getKeys1().size(); index++) {
            results.add(representation.getTimeSlotAt(random.nextInt()));
        }

        return results;
    }

    private ArrayList<Integer> generateSwapSequences(int maxSize) {
        ArrayList<Integer> swapSeqs = new ArrayList<Integer>();
        NoRepeatRandom random = new NoRepeatRandom(maxSize);
        while (!random.isDone()) {
            swapSeqs.add(random.nextInt());
        }
        return swapSeqs;
    }

    private boolean swap(Representation newRepresentation, TimeSlot badTimeSlot, TimeSlot timeSlot, int swapSeq) {
        ClassRoom classRoom = newRepresentation.getClassRoomAt(swapSeq);
        CourseLesson course1 = newRepresentation.get(badTimeSlot, classRoom);
        CourseLesson course2 = newRepresentation.get(timeSlot, classRoom);
        if (course1 != null && course1.isFixedTimeSlot()) {
            return false;
        }
        if (course2 != null && course2.isFixedTimeSlot()) {
            return false;
        }
        newRepresentation.put(badTimeSlot, classRoom, course2);
        newRepresentation.put(timeSlot, classRoom, course1);
        return true;
    }

    public void generateResults() {
        System.out.println("generating results");
        ExcelAdaptor adaptor = new ExcelAdaptor(this, representation);
        adaptor.GenerateTimeTable();
    }

    public void saveTimeTable() throws BasicException {
        System.out.println("saving timetable");
        representation.save(session);
    }

    public ClassRooms getClassRooms() {
        return classRooms;
    }

    public Courses getCourses() {
        return courses;
    }

    public Teachers getTeachers() {
        return teachers;
    }

    public TimeSlots getTimeSlots() {
        return timeSlots;
    }

    public Students getStudents() {
        return students;
    }

    public Departments getDepartments() {
        return departments;
    }

    public void setDepartments(Departments departments) {
        this.departments = departments;
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
}

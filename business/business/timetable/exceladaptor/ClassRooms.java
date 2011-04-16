/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.exceladaptor;

import business.timetable.classrooms.ClassRoom;
import java.util.ArrayList;
import java.util.Collections;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ClassRooms {

    private ArrayList<String> map;
    private ArrayList<ClassRoom> classRooms;

    public ClassRooms(ArrayList<ClassRoom> classRooms) {
        map = new ArrayList<String>();
        this.classRooms = classRooms;

        for (ClassRoom classRoom : classRooms) {
            String classRoomString = classRoom.getName();

            if (!map.contains(classRoomString)) {
                map.add(classRoomString);
            }
        }

        Collections.sort(map);
    }

    ArrayList<String> getList() {
        return map;
    }

    public ArrayList<ClassRoom> getClassRooms() {
        return classRooms;
    }

    int getRow(ClassRoom classRoom) {
        return map.indexOf(classRoom.getName());
    }
}

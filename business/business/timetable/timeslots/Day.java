/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.timeslots;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public enum Day implements Comparable<Day> {

    Monday,
    Tuesday,
    Wednesday,
    Thursday,
    Friday,
    Saturday,
    Sunday;

    public int getDayNo(){
        switch(this){
            case Monday:
                return 1;
            case Tuesday:
                return 2;
            case Wednesday:
                return 3;
            case Thursday:
                return 4;
            case Friday:
                return 5;
            case Saturday:
                return 6;
            case Sunday:
                return 7;
            default:
                return 0;
        }
    }
}

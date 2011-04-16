/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.timeslots;

import com.timetable.utilz.Utilz;
import java.util.ArrayList;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Time implements Comparable<Time> {

    private int hours;
    private int mins;
    private int secs;
    private long time;

    private Time() {
    }

    public Time(String time) {
        setTime(time);
    }

    public String getTime() {
        String sHours = (String.valueOf(hours).length() < 2 ? "0" + String.valueOf(hours) : String.valueOf(hours));
        String sMins = (String.valueOf(mins).length() < 2 ? "0" + String.valueOf(mins) : String.valueOf(mins));
        String sSecs = (String.valueOf(secs).length() < 2 ? "0" + String.valueOf(secs) : String.valueOf(secs));

        return sHours + ":" + sMins + ":" + sSecs;
    }

    public void setTime(String time) {
        ArrayList<String> tokens = Utilz.getTokens(time, ":");
        hours = Integer.parseInt(tokens.get(0));
        mins = Integer.parseInt(tokens.get(1));
        secs = Integer.parseInt(tokens.get(2));
        this.time = (60 * 60 * hours) + (60 * mins) + secs;
    }

    public static Time getDifference(String beginTime, String endTime) {
        Time t1 = new Time(beginTime);
        Time t2 = new Time(endTime);

        Time result = new Time();
        result.hours = Math.abs(t2.hours - t1.hours);
        result.mins = Math.abs(t2.mins - t1.mins);
        result.secs = Math.abs(t2.secs - t1.secs);

        return result;
    }

    public int getHours() {
        return hours;
    }

    public int getMins() {
        return mins;
    }

    public int getSecs() {
        return secs;
    }

    public long getLongTime() {
        return time;
    }

    @Override
    public String toString() {
        return getTime();
    }

    public int compareTo(Time o) {

        if (getLongTime() < o.getLongTime()) {
            return -1;
        }

        if (getLongTime() > o.getLongTime()) {
            return 1;
        }

        return 0;
    }
}


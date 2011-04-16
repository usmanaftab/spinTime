/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class InsufficientClassesException extends RuntimeException {

    public InsufficientClassesException() {
    }

    public InsufficientClassesException(String msg) {
        super(msg);
    }
}

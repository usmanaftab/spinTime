/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.representation;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class IncompatibleKeysException extends RuntimeException {

    public IncompatibleKeysException() {
    }

    public IncompatibleKeysException(String msg) {
        super(msg);
    }
}

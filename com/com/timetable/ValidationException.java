/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.timetable;

import com.timetable.BasicException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class ValidationException extends BasicException {

    /**
     * Creates a new instance of <code>ValidationException</code> without detail message.
     */
    public ValidationException() {
    }


    /**
     * Constructs an instance of <code>ValidationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ValidationException(String msg) {
        super(msg);
    }
}

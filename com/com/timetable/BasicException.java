/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.timetable;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class BasicException extends Exception {

    /**
     * Creates a new instance of <code>BasicException</code> without detail message.
     */
    public BasicException() {
    }

    /**
     * Constructs an instance of <code>BasicException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public BasicException(String msg) {
        super(msg);
    }

    /**
     * Constructs an instance of <code>BasicException</code> with the specified detail message.
     * @param msg the detail message.
     * @param cause cause of the exception.
     */
    public BasicException(String msg, Throwable cause) {
        super(msg, cause);
    }

    /**
     * Constructs an instance of <code>BasicException</code> with the specified detail message.
     * @param cause cause of the exception.
     */
    public BasicException(Throwable cause) {
        super(cause);
    }
}

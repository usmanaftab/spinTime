/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dal.timetable.db;

import com.timetable.BasicException;
import com.timetable.config.Config;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class Session {

    private static Connection conn;

    public Session() throws BasicException {
        startConnection();
    }

    private String getDriver() {
        return Config.getProperty("db.driver");
    }

    private String getUrl() {
        return Config.getProperty("db.url");
    }

    private String getUser() {
        return Config.getProperty("db.user");
    }

    private String getPassword() {
        return Config.getProperty("db.password");
    }

    public void startConnection() throws BasicException {
        try {
            if (conn == null) {
                Class.forName(getDriver()).newInstance();
                conn = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            }

            if (conn.isClosed()) {
                conn = DriverManager.getConnection(getUrl(), getUser(), getPassword());
            }

        } catch (ClassNotFoundException ex) {
            throw new BasicException(Session.class.getName(), ex);
        } catch (InstantiationException ex) {
            throw new BasicException(Session.class.getName(), ex);
        } catch (IllegalAccessException ex) {
            throw new BasicException(Session.class.getName(), ex);
        } catch (SQLException ex) {
            throw new BasicException(Session.class.getName(), ex);
        }
    }

    public Connection getConnection() throws BasicException {
        if (conn == null) {
            startConnection();
        }

        return conn;
    }

    public void closeConnection() throws BasicException {
        try {
            conn.close();
        } catch (SQLException ex) {
            throw new BasicException(Session.class.getName(), ex);
        }
    }

    public void beginTransaction() throws BasicException {
        try {
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new BasicException(Session.class.getName(), ex);
        }
    }

    public void endTransaction() throws BasicException {
        try {
            conn.commit();
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new BasicException(Session.class.getName(), ex);
        }
    }

    public void rollBack() throws BasicException {
        try {
            conn.rollback();
            conn.setAutoCommit(false);
        } catch (SQLException ex) {
            throw new BasicException(Session.class.getName(), ex);
        }
    }
}

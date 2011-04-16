/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package gui.timetable;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class StartST {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(StartST.class.getName()).log(Level.SEVERE, null, ex);
                }
                new RootFrame().init();
            }
        });
    }

}

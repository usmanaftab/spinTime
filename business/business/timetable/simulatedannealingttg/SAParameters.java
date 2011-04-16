/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package business.timetable.simulatedannealingttg;

import com.timetable.config.Config;

/**
 *
 * @author Usman Aftab (08-0964)
 */
public class SAParameters {

    public double temperature;
    public double terminalTemperateure;
    public double alpha;
    public int innerloopCount;

    public SAParameters(SAParam saParam) {
        String saParamType = (saParam == SAParam.HARD)? "HardParameter." : "SoftParameter.";

        temperature = Config.getPropertyDouble("business.timetable.simulatedannealingttg." + saParamType + "temperature");
        terminalTemperateure = Config.getPropertyDouble("business.timetable.simulatedannealingttg." + saParamType + "terminalTemperature");
        alpha = Config.getPropertyDouble("business.timetable.simulatedannealingttg." + saParamType + "alpha");
        innerloopCount = Config.getPropertyInt("business.timetable.simulatedannealingttg." + saParamType + "innerLoopCount");
    }
}

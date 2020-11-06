package org.haland.javaasv.config.unit;

import org.haland.javaasv.config.BaseConfig;

import java.util.Properties;

/**
 * Contains configuration information for a PID controller
 */
public class PIDConfigUnit extends BaseConfig {
    private static final String PROPERTY_FILE_NAME = "controllers.properties";

    private double Kp;
    private double Ki;
    private double Kd;
    private double period;

    private final String name;

    public PIDConfigUnit(String name) {
        this(name, true);
    }

    private PIDConfigUnit(String name, boolean foo) {
        this.name = name;
    }

    /**
     * @return The property file name to load for this config.
     */
    @Override
    protected String getPropertyFileName() {
        return PROPERTY_FILE_NAME;
    }

    /**
     * @param properties The properties to use for configuration, loaded from the
     *                   specified property file.
     */
    @Override
    protected void configure(Properties properties) {
        if (name != null) {
            Kp = getDoublePropertyValue("controllers." + name + ".kp", properties);
            Ki = getDoublePropertyValue("controllers." + name + ".ki", properties);
            Kd = getDoublePropertyValue("controllers." + name + ".kd", properties);
            period = getDoublePropertyValue("controllers." + name + ".period", properties);
        }
    }

    public double getKp() {
        return Kp;
    }

    public double getKi() {
        return Ki;
    }

    public double getKd() {
        return Kd;
    }

    public double getPeriod() {
        return period;
    }

    public String getName() {
        return name;
    }
}

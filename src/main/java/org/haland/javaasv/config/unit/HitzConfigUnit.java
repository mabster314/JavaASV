package org.haland.javaasv.config.unit;

import org.haland.javaasv.config.BaseConfig;
import org.haland.javaasv.config.PropertiesLoader;

import java.util.PrimitiveIterator;
import java.util.Properties;

public class HitzConfigUnit extends BaseConfig {
    private static final String PROPERTY_FILE_NAME = "controllers.properties";

    private double Kph;
    private double Kpx;
    private double Kix;

    private final String name;

    public HitzConfigUnit(String name, Properties properties) {
        this.name = name;
        configure(properties);
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
            Kph = getDoublePropertyValue("controllers." + name + ".kph", properties);
            Kpx = getDoublePropertyValue("controllers." + name + ".kpx", properties);
            Kix = getDoublePropertyValue("controllers." + name + ".kix", properties);
        }
    }

    public double getKph() {
        return Kph;
    }

    public double getKpx() {
        return Kpx;
    }

    public double getKix() {
        return Kix;
    }

    public String getName() {
        return name;
    }
}

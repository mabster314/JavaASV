/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2020  Max Haland
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.haland.javaasv.config;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Base class for configuration from loading values from properties file.
 */
public abstract class BaseConfig {
    private final Logger log = Logger.getLogger(getClass().getName());

    private static final String PROPERTY_FILE_DIR_DEFAULT = "/home/asv-config/";

    private static String propertyFileDir = PROPERTY_FILE_DIR_DEFAULT;

    private final Properties properties;

    public BaseConfig() {
        log.info("Configuring class=" + getClass());

        final String propertyFileName = getFullyQualifiedPropertyFileName();
        properties = new PropertiesLoader().loadProperties(propertyFileName);

        try {
            configure(properties);
        } catch (Throwable e) {
            String msg = "ERROR during configuration of class=" + getClass();
            log.log(Level.SEVERE, msg, e);
            throw e;
        }
    }

    /** @return The property file name to load for this config. */
    protected abstract String getPropertyFileName();

    /**
     * @param properties
     *            The properties to use for configuration, loaded from the
     *            specified property file.
     */
    protected abstract void configure(Properties properties);

    protected String getStringPropertyValue(String propertyName,
                                            Properties props) {
        final String value = props.getProperty(propertyName);
        if (value == null) {
            String msg = "Property '" + propertyName
                    + "' not found in property file";
            throw new IllegalStateException(msg);
        }
        return value;
    }

    protected int[] getIntArrayPropertyValue(String propertyName,
                                             Properties props) {

        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        String rawValues = propertyValue.replace("[", "");
        rawValues = rawValues.replace("]", "");
        final String[] splitValues = rawValues.split(",");

        int[] returnValues = new int[splitValues.length];
        for (int i = 0; i < splitValues.length; i++) {
            returnValues[i] = Integer.parseInt(splitValues[i].trim());
        }

        return returnValues;
    }

    protected double getDoublePropertyValue(String propertyName,
                                            Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Double.parseDouble(propertyValue);
    }

    protected int getIntPropertyValue(String propertyName, Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Integer.parseInt(propertyValue);
    }

    protected boolean getBooleanPropertyValue(String propertyName,
                                              Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Boolean.parseBoolean(propertyValue);
    }

    public String getFullyQualifiedPropertyFileName() {
        return propertyFileDir + getPropertyFileName();
    }

    public static String getPropertyFileDir() {
        return propertyFileDir;
    }

    public static void setPropertyFileDir(String propertyFileDirectory) {
        propertyFileDir = propertyFileDirectory;
    }

    public Properties getProperties() {
        return properties;
    }
}
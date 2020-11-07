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

import org.tinylog.Logger;

import java.util.Properties;

/**
 * Base class for configuration from loading values from properties file.
 */
public abstract class BaseConfig {

    public static final String PROPERTY_FILE_DIR_SRC_RUN =
            "src/main/resources/properties/";
    public static final String PROPERTY_FILE_DIR_SRC_TESTS =
            "src/test/resources/properties/";

    private static String propertyFileDir = PROPERTY_FILE_DIR_SRC_RUN;

    private final Properties properties;

    public BaseConfig() {
        Logger.info("Configuring class " + getClass());

        final String propertyFileName = getFullyQualifiedPropertyFileName();
        properties = new PropertiesLoader().loadProperties(propertyFileName);

        try {
            configure(properties);
        } catch (Throwable e) {
            String msg = "ERROR during configuration of class " + getClass();
            Logger.error(e, msg);
            throw e;
        }
    }

    /**
     * @return The property file name to load for this config.
     */
    protected abstract String getPropertyFileName();

    /**
     * @param properties The properties to use for configuration, loaded from the
     *                   specified property file.
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

    protected double[] getDoubleArrayPropertyValue(String propertyName,
                                                   Properties props) {

        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        String rawValues = propertyValue.replace("[", "");
        rawValues = rawValues.replace("]", "");
        final String[] splitValues = rawValues.split(",");

        double[] returnValues = new double[splitValues.length];
        for (int i = 0; i < splitValues.length; i++) {
            returnValues[i] = Double.parseDouble(splitValues[i].trim());
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

    protected long getLongPropertyValue(String propertyName, Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Long.parseLong(propertyValue);
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
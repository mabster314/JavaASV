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
     * This method configures a BaseConfig implementation. The implementation should use this to populate fields from
     * the {@link Properties} instance.
     *
     * @param properties The properties to use for configuration, loaded from the
     *                   specified property file.
     */
    protected abstract void configure(Properties properties);

    /**
     * Parses an String from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return a String containing the property value
     */
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

    /**
     * Parses an int array from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return an int[] containing the property values
     */
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

    /**
     * Parses an double array from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return an double[] containing the property values
     */
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

    /**
     * Parses an double value from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return a double representing the property value
     */
    protected double getDoublePropertyValue(String propertyName,
                                            Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Double.parseDouble(propertyValue);
    }

    /**
     * Parses an int value from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return an int representing the property value
     */
    protected int getIntPropertyValue(String propertyName, Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Integer.parseInt(propertyValue);
    }

    /**
     * Parses a long int from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return a long representing the property value
     */
    protected long getLongPropertyValue(String propertyName, Properties props) {
        final String propertyValue =
                getStringPropertyValue(propertyName, props);
        return Long.parseLong(propertyValue);
    }

    /**
     * Parses a boolean from a {@link Properties} instance.
     *
     * @param propertyName The name of the property to parse
     * @param props The properties instance to parse from
     * @return a boolean representing the property value
     */
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

    /**
     * Used to set a property value directory for unit testing
     * @param propertyFileDirectory The test properties directory
     */
    public static void setPropertyFileDir(String propertyFileDirectory) {
        propertyFileDir = propertyFileDirectory;
    }

    public Properties getProperties() {
        return properties;
    }
}
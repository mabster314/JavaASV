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

import java.io.*;
import java.util.Properties;


/**
 * Utility class to handle loading properties from a file into a Properties
 * instance.
 */
public class PropertiesLoader {

    private static final String CAN_T_CONTINUE_MSG = "; can't continue";

    /**
     * Load the properties from the specified file name.
     *
     * @param fileName
     *            The file name, including any desired path (absolute or
     *            relative).
     * @return Properties instance loaded with the properties in the file.
     */
    public Properties loadProperties(String fileName) {
        File file = new File(fileName);
        return loadProperties(file);
    }

    private Properties loadProperties(File file) {
        InputStream inputStream = openPropertiesFile(file);
        Properties prop = loadPropertiesFromFile(file, inputStream);

        if (prop.isEmpty()) {
            final String msg = "No properties were loaded from file=" + file
                    + CAN_T_CONTINUE_MSG;
            throw new IllegalStateException(msg);
        }

        return prop;
    }

    private InputStream openPropertiesFile(File file) {
        InputStream inputStream;
        try {
            inputStream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            final String msg = "Error finding properties file=" + file
                    + CAN_T_CONTINUE_MSG;
            Logger.error(e, msg);
            throw new IllegalStateException(msg, e);
        }
        return inputStream;
    }

    private Properties loadPropertiesFromFile(File file, InputStream inputStream) {
        Properties prop = new Properties();
        try {
            prop.load(inputStream);
        } catch (IOException e) {
            final String msg = "Error reading properties file=" + file
                    + CAN_T_CONTINUE_MSG;
            Logger.error(e, msg);
            throw new IllegalStateException(msg, e);
        }
        return prop;
    }
}

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

package org.haland.javaasv;

import org.haland.javaasv.config.BaseConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.tinylog.Logger;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.Properties;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

public abstract class TestBase {

    public static final String PROPERTY_FILE_DIR_SRC_RUN =
            "src/main/resources/properties/";
    public static final String PROPERTY_FILE_DIR_SRC_TESTS =
            "src/test/resources/properties/";

    static {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_TESTS);
    }

    @BeforeEach
    public void processTestStarting(TestInfo testInfo) {
        Logger.info("Test starting: " + testInfo.getDisplayName());
    }

    @AfterEach
    public void processTestFinished(TestInfo testInfo) {
        Logger.info("Test finished: " + testInfo.getDisplayName());
    }

    protected void assertInstanceVariablesNotNull(Object sut)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = sut.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            if (!type.isPrimitive()) {
                String assertMessage =
                        "Field " + field.getName() + " was null.";
                assertNotNull(field.get(sut), assertMessage);
            }
        }
    }

    protected void assertDoublesNotZero(Object sut)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = sut.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            if (type.getName().equals("double")) {
                String assertMessage =
                        "Field " + field.getName() + " was zero.";
                assertNotEquals(0.0, field.get(sut), assertMessage);
            }
        }
    }

    protected void assertArraysNotZeroLength(Object sut)
            throws IllegalArgumentException, IllegalAccessException {
        Field[] fields = sut.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);

            Class<?> type = field.getType();
            if (type.isArray()) {
                Object array = field.get(sut);
                int length = Array.getLength(array);

                String assertMessage =
                        "Array field " + field.getName() + " was length zero.";
                assertNotEquals(0, field.get(sut), assertMessage);
            }
        }
    }

    protected void assertNoDuplicatePropertyValues(String propertyRegex,
                                                   BaseConfig config) {
        final Properties properties = config.getProperties();
        final Set<String> propertyNames = properties.stringPropertyNames();

        long propertiesCount = propertyNames.size();
        assertFalse(propertiesCount == 0, "No properties in config.");

        long matchedPropertiesCount = propertyNames.stream()
                .filter(key -> key.matches(propertyRegex)).count();
        long distinctValuesCount = propertyNames.stream()
                .filter(key -> key.matches(propertyRegex))
                .map(key -> properties.getProperty(key)).distinct().count();

        assertFalse(matchedPropertiesCount == 0,
                "Zero property keys matched regex=" + propertyRegex);

        assertThat(
                "Probable duplicate value in property sequence with regex ='"
                        + propertyRegex + "'",
                distinctValuesCount, is(matchedPropertiesCount));
    }
}
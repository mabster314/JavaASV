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

import org.haland.javaasv.TestBase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BaseConfigTest extends TestBase {

    private static final String TEST_ARRAY_KEY = "key.array";

    private Properties props;
    private BaseConfig sut;

    @BeforeEach
    public void setUp() {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_RUN);
        sut = new BaseConfigImplementation();
        props = new Properties();
    }

    private void assertIntArrayMatches(int[] expected, int[] actual) {
        String lengthMsg = "Actual array had length " + actual.length
                + ", expected " + expected.length;
        assertEquals(expected.length, actual.length, lengthMsg);

        for (int i = 0; i < expected.length; i++) {
            String valueMsg = "Actual array had value " + actual[i]
                    + ", expected " + expected[i];
            assertEquals(expected[i], actual[i], valueMsg);
        }
    }

    private void assertDoubleArrayMatches(double[] expected, double[] actual) {
        String lengthMsg = "Actual array had length " + actual.length
                + ", expected " + expected.length;
        assertEquals(expected.length, actual.length, lengthMsg);

        for (int i = 0; i < expected.length; i++) {
            String valueMsg = "Actual array had value " + actual[i]
                    + ", expected " + expected[i];
            assertEquals(expected[i], actual[i], valueMsg);
        }
    }

    /**
     * Test the int array reader
     */
    @Test
    public void testGetIntArrayPropertyValue() {
        int[] testArray = {1, 2, 3};

        props.setProperty(TEST_ARRAY_KEY, "[1, 2, 3]");
        int[] resultWithBrackets =
                sut.getIntArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertIntArrayMatches(testArray, resultWithBrackets);

        props.setProperty(TEST_ARRAY_KEY, "1, 2, 3");
        int[] resultWithoutBrackets =
                sut.getIntArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertIntArrayMatches(testArray, resultWithoutBrackets);

        props.setProperty(TEST_ARRAY_KEY, "[1,2,3]");
        int[] resultWithoutSpacesWithBrackets =
                sut.getIntArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertIntArrayMatches(testArray, resultWithoutSpacesWithBrackets);

        props.setProperty(TEST_ARRAY_KEY, "1,2,3");
        int[] resultWithoutSpacesWithoutBrackets =
                sut.getIntArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertIntArrayMatches(testArray, resultWithoutSpacesWithoutBrackets);

        int[] testArrayLonger = {0, 2, 4, 6, 8, 10};

        props.setProperty(TEST_ARRAY_KEY, "[0, 2, 4, 6, 8, 10]");
        int[] resultLonger =
                sut.getIntArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertIntArrayMatches(testArrayLonger, resultLonger);
    }

    /**
     * Test the double array reader
     */
    @Test
    public void testGetDoubleArrayPropertyValue() {
        double[] testArray = {1, 2, 3};

        props.setProperty(TEST_ARRAY_KEY, "[1, 2, 3]");
        double[] resultWithBrackets =
                sut.getDoubleArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertDoubleArrayMatches(testArray, resultWithBrackets);

        props.setProperty(TEST_ARRAY_KEY, "1, 2, 3");
        double[] resultWithoutBrackets =
                sut.getDoubleArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertDoubleArrayMatches(testArray, resultWithoutBrackets);

        props.setProperty(TEST_ARRAY_KEY, "[1,2,3]");
        double[] resultWithoutSpacesWithBrackets =
                sut.getDoubleArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertDoubleArrayMatches(testArray, resultWithoutSpacesWithBrackets);

        props.setProperty(TEST_ARRAY_KEY, "1,2,3");
        double[] resultWithoutSpacesWithoutBrackets =
                sut.getDoubleArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertDoubleArrayMatches(testArray, resultWithoutSpacesWithoutBrackets);

        double[] testArrayLonger = {0, 2, 4, 6, 8, 10};

        props.setProperty(TEST_ARRAY_KEY, "[0, 2, 4, 6, 8, 10]");
        double[] resultLonger =
                sut.getDoubleArrayPropertyValue(TEST_ARRAY_KEY, props);
        assertDoubleArrayMatches(testArrayLonger, resultLonger);
    }

    private class BaseConfigImplementation extends BaseConfig {
        @Override
        protected String getPropertyFileName() {
            return "controllers.properties"; // Anything that works
        }

        @Override
        protected void configure(Properties properties) {
        }
    }

}

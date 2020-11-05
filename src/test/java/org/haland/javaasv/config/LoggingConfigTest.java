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
import org.junit.jupiter.api.Test;

import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggingConfigTest extends TestBase {
    /**
     * Verifies can find and read the logging config properties file and
     * displays example logging output.
     */
    @Test
    public void testInitializeFileLog() {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_RUN);
        final LoggingConfig sut = new LoggingConfig();
        final String propertyFile = sut.getFullyQualifiedPropertyFileName();

        sut.initializeFileLog(propertyFile);

        final Logger log = Logger.getLogger(getClass().getName());

        final Exception e = new IllegalArgumentException("the exception msg");
        log.log(Level.SEVERE, "Exception msg", e);

        log.info("log an info msg");
        log.config("log a config msg");
        log.fine("log a fine msg");
        log.finer("log a finer msg");
        log.finest("log a finest msg");
    }

    @Test
    public void testInitializeSocketLog() {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_RUN);
        final LoggingConfig sut = new LoggingConfig();
        final String propertyFile = sut.getFullyQualifiedPropertyFileName();

        sut.initializeFileLog(propertyFile);

        final Logger log = Logger.getLogger(getClass().getName());
        log.info("log a test msg");
    }

    @Test
    public void testDummy() {
        // empty test so Jenkins passes with the other tests disabled
    }
}

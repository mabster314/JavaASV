/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2021  Max Haland
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

class ControllerConfigTest extends TestBase {
    @Test
    public void testControllerConfig_Test() throws IllegalAccessException {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_TESTS);
        ControllerConfig sut = new ControllerConfig();
        assertInstanceVariablesNotNull(sut);
        assertArraysNotZeroLength(sut);
    }

    @Test
    public void testControllerConfig_Runtime() throws IllegalAccessException {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_RUN);
        ControllerConfig sut = new ControllerConfig();
        assertInstanceVariablesNotNull(sut);
        assertArraysNotZeroLength(sut);
    }
}
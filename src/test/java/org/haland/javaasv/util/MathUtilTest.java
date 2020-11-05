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

package org.haland.javaasv.util;

import org.haland.javaasv.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Test class for {@link MathUtil}
 */
class MathUtilTest extends TestBase {
    private static final int INT_LOW = 5;
    private static final int INT_HIGH = 10;

    private static final int TEST_INT_LOW = 2;
    private static final int TEST_INT_HIGH = 12;
    private static final int TEST_INT_MED = 7;

    private static final double DOUBLE_LOW = 2.235;
    private static final double DOUBLE_HIGH = 1235.12343;

    private static final double TEST_DOUBLE_LOW = 2;
    private static final double TEST_DOUBLE_HIGH = 5961;
    private static final double TEST_DOUBLE_MED = 8.2356;

    @Test
    void testClamp() {
        // Higher number should be brought down
        assertEquals(INT_HIGH, MathUtil.clamp(TEST_INT_HIGH, INT_LOW, INT_HIGH));
        // Lower number should be brought up
        assertEquals(INT_LOW, MathUtil.clamp(TEST_INT_LOW, INT_LOW, INT_HIGH));
        // In between should not change
        assertEquals(TEST_INT_MED, MathUtil.clamp(TEST_INT_MED, INT_LOW, INT_HIGH));

        // Higher number should be brought down
        assertEquals(DOUBLE_HIGH, MathUtil.clamp(TEST_DOUBLE_HIGH, DOUBLE_LOW, DOUBLE_HIGH));
        // Lower number should be brought up
        assertEquals(DOUBLE_LOW, MathUtil.clamp(TEST_DOUBLE_LOW, DOUBLE_LOW, DOUBLE_HIGH));
        // In between should not change
        assertEquals(TEST_DOUBLE_MED, MathUtil.clamp(TEST_DOUBLE_MED, DOUBLE_LOW, DOUBLE_HIGH));
    }
}
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

package org.haland.javaasv.route;

import org.haland.javaasv.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class WaypointInterfaceTest extends TestBase {
    static WaypointInterface waypointA = new Waypoint(45,93, .001,
            WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);
    static WaypointInterface waypointB = new Waypoint(45,90, .001,
            WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);

    @Test
    void testEqual() {
        assertTrue(WaypointInterface.equals(waypointA, waypointA));
    }

    @Test
    void testNotEqual() {
        assertFalse(WaypointInterface.equals(waypointA, waypointB));
    }
}
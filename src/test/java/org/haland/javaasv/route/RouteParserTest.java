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

package org.haland.javaasv.route;

import org.haland.javaasv.TestBase;
import org.haland.javaasv.config.AllConfig;
import org.haland.javaasv.config.BaseConfig;
import org.haland.javaasv.config.RouteConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertTrue;

class RouteParserTest extends TestBase {
    public static final double[] START_COORDINATES = {44.9187, -92.8435};
    private static final double[] END_COORDINATES = {44.9187, -92.8439};

    private RouteParser sut;

    private AllConfig config;

    @BeforeEach
    void setupTests() {
        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_TESTS);
    }

    @AfterEach
    void teardownTests() {
        config = null;
        sut = null;
    }

    @Test
    void testTwoPointRouteParser() {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setRouteType(RouteType.TWO_POINT);
        config = new AllConfig(routeConfig);
        sut = new RouteParser(config);
        RouteInterface parsedRoute = sut.getRoute();
        double[] parsedStartCoordinates = parsedRoute.getPreviousWaypoint().getCoordinates();
        double[] parsedEndCoordinates = new double[0];
        parsedEndCoordinates = parsedRoute.getNextWaypoint().getCoordinates();
        assertTrue(Arrays.equals(START_COORDINATES, parsedStartCoordinates));
        assertTrue(Arrays.equals(END_COORDINATES, parsedEndCoordinates));
    }

    @Test
    void testCreateFileRoute() {
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setRouteType(RouteType.FILE);
        config = new AllConfig(routeConfig);
        sut = new RouteParser(config);
        RouteInterface parsedRoute = sut.getRoute();
        int foo;
        double[] parsedStartCoordinates = parsedRoute.getPreviousWaypoint().getCoordinates();
        double[] parsedEndCoordinates = new double[0];
        parsedEndCoordinates = parsedRoute.getNextWaypoint().getCoordinates();
        assertTrue(Arrays.equals(START_COORDINATES, parsedStartCoordinates));
        assertTrue(Arrays.equals(END_COORDINATES, parsedEndCoordinates));
    }
}
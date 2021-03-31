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

import org.haland.javaasv.config.AllConfig;
import org.haland.javaasv.config.RouteConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Scanner;
import org.tinylog.Logger;

/**
 * Parses {@link RouteInterface}s from a {@link RouteConfig} instance
 */
public class RouteParser {
    private static final String FILE_DELIMITER = ",";

    private RouteConfig config;
    private RouteInterface route;

    /**
     * Constructs a new RouteParser with the specified config
     * @param allConfig {@link RouteConfig} to load route from
     */
    public RouteParser(AllConfig allConfig) {
        this.config = allConfig.getRouteConfig();
        switch (this.config.getRouteType()) {
            case FILE:
                route = createFileRoute();
                break;
            case TWO_POINT:
                route = createTwoPointRoute();
        }
    }

    /**
     * Parses a route from a file input
     * @return a {@link RouteInterface} representing the route
     */
    private RouteInterface createFileRoute() {
        SegmentedRoute route = new SegmentedRoute();

        // Get the filename from the config
        String fileDir = config.getRouteFileDir();
        String filename = config.getRouteFileName();

        // Try to open the route file
        try (Scanner scanner = new Scanner(new File(fileDir + filename));) {
            // Iterate over the lines in the route file
            while (scanner.hasNextLine()) {
                // Split the line along delimiters
                String[] values = scanner.nextLine().strip().split(FILE_DELIMITER);

                // Strip whitespace
                Arrays.parallelSetAll(values, (i) -> values[i].strip());

                // Add a waypoint to the route for each line
                route.addWaypoint(new Waypoint(Double.parseDouble(values[0]),Double.parseDouble(values[1]),
                        Double.parseDouble(values[2]), WaypointInterface.WaypointBehavior.valueOf(values[3])));
            }
        } catch (FileNotFoundException e) {
            Logger.error(e);
        }

        return route;
    }

    /**
     * Makes a two point route from the config
     *
     * @return a {@link RouteInterface} representing the one-segment route
     */
    private RouteInterface createTwoPointRoute() {
        return new RouteInterface() {
            @Override
            public WaypointInterface getPreviousWaypoint() {
                return new Waypoint(config.getTwoPointRouteStart(), config.getTwoPointRouteTolerance(),
                        WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);
            }

            @Override
            public WaypointInterface getNextWaypoint() {
                return new Waypoint(config.getTwoPointRouteEnd(), config.getTwoPointRouteTolerance(),
                        WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);
            }

            @Override
            public boolean isComplete() {
                return true;
            }

            @Override
            public void advanceWaypoint() {
                Logger.error(new RouteEndException());
            }
        };
    }

    /**
     * Provides the parsed route
     * @return a {@link RouteInterface} representing the parsed route
     */
    public RouteInterface getRoute() {
        return route;
    }
}

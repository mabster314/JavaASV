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

public class RouteParser {
    private RouteConfig config;
    private RouteInterface route;

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

    private RouteInterface createFileRoute() {
        return null;
    }

    /**
     * Makes a two point route from the config
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
                return false;
            }
        };
    }

    public RouteInterface getRoute() {
        return route;
    }
}

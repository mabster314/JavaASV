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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SegmentedRoute implements RouteInterface {
    private List<WaypointInterface> waypointList;
    private int currentWaypoint;

    /**
     * Constructs an empty route
     */
    private SegmentedRoute() {
        this.waypointList = new ArrayList<WaypointInterface>();
        this.currentWaypoint = 0;
    }

    /**
     * Copy constructor
     * @param route the route to copy
     */
    public SegmentedRoute(SegmentedRoute route) {
        this.waypointList = new ArrayList<WaypointInterface>();
        this.waypointList.addAll(route.waypointList);
        this.currentWaypoint = route.currentWaypoint;
    }

    /**
     * Constructs a route using the specified waypoints
     *
     * @param waypoints An ordered array of waypoints defining the route
     */
    public SegmentedRoute(WaypointInterface... waypoints) {
        this();
        this.waypointList.addAll(Arrays.asList(waypoints));
    }

    public void addWaypoint(WaypointInterface waypoint) {
        waypointList.add(waypoint);
    }

    /**
     * Returns the waypoint with the specified index
     * @param index
     * @return
     */
    public WaypointInterface getWaypoint(int index) {
        return waypointList.get(index);
    }

    public int getRouteLength() {
        return waypointList.size();
    }

    @Override
    public WaypointInterface getPreviousWaypoint() {
        return waypointList.get(currentWaypoint);
    }

    @Override
    public WaypointInterface getNextWaypoint() {
        return waypointList.get(currentWaypoint + 1);
    }

    @Override
    public boolean isComplete() {
        return (currentWaypoint == waypointList.size() - 2);
    }

    @Override
    public void advanceWaypoint() throws RouteEndException {
        if (currentWaypoint >= waypointList.size() - 2) {
            throw new RouteEndException();
        } else {
            currentWaypoint++;
        }
    }
}

package org.haland.javaasv.route;

public interface RouteInterface {
    WaypointInterface getPreviousWaypoint();

    WaypointInterface getNextWaypoint();

    boolean isComplete();
}

package org.haland.javaasv.route;

public interface RouteInterface {
    WaypointInterface getNextWaypoint();

    boolean isComplete();
}

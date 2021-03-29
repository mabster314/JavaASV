package org.haland.javaasv.route;

public interface RouteInterface {
    /**
     * This method returns the previous waypoint for the current straight-line segment
     * @return a {@link WaypointInterface} representing the start point of the current segment
     */
    WaypointInterface getPreviousWaypoint();

    /**
     * This method returns the next waypoint for the current straight-line segment
     * @return a {@link WaypointInterface} representing the end point of the current segment
     */
    WaypointInterface getNextWaypoint();

    /**
     * Indicates whether the current route in complete
     * @return True if the rout is complete, false otherwise
     */
    boolean isComplete();
}

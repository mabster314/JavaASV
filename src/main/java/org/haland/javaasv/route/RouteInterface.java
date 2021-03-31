package org.haland.javaasv.route;

/**
 * An interface for route providers.
 */
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
     * @return True if the route is complete, false otherwise
     */
    boolean isComplete();

    /**
     * Advances between segments in a route.
     * @throws RouteEndException if called when {@link #isComplete()} returns true
     */
    void advanceWaypoint() throws RouteEndException;

}

package org.haland.javaasv.route;

/**
 * Data class representing a geographic waypoint
 */
public interface WaypointInterface {
    /**
     *
     * @return latitude of the waypoint
     */
    abstract double getLatitude();

    /**
     *
     * @return longitude of the waypoint
     */
    abstract double getLongitude();

    /**
     *
     * @return double[] containing {latitude, longitude} coordinates
     */
    abstract double[] getCoordinates();

    /**
     *
     * @return absolute tolerance to recognize having reached the destination
     */
    abstract double getTolerance();

    /**
     *
     * @return {@link WaypointBehavior} enum representing behavior to engage in after reaching waypoint
     */
    abstract WaypointBehavior getDestinationBehavior();

    /**
     * Determines whether two {@link WaypointInterface} objects are equal.
     * <p>
     * Two {@link WaypointInterface} objects are equal if they share the same latitide, longitude, tolerance, and
     * destination behavior
     * @param waypointA The first waypoint to compare
     * @param waypointB The second waypoint to compare
     * @return True if the two waypoints are equal, false otherwise
     */
    static boolean equals(WaypointInterface waypointA, WaypointInterface waypointB) {
        return (waypointA.getLatitude() == waypointB.getLatitude()) &&
                (waypointA.getLongitude() == waypointB.getLongitude()) &&
                (waypointA.getTolerance() == waypointB.getTolerance()) &&
                (waypointA.getDestinationBehavior() == waypointB.getDestinationBehavior());
    }

    /**
     * Defines behavior modes for at checkpoints
     *
     * <p>
     * NEXT_WAYPOINT: continue immediately to next waypoint
     * <p>
     * LOITER: keep station at the waypoint
     * </p>
     */
    public enum WaypointBehavior {
        NEXT_WAYPOINT, LOITER
    }
}

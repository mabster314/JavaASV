package org.haland.javaasv.route;

/**
 * Data class representing a geographic waypoint
 */
public interface WaypointInterface {
    /**
     *
     * @return latitude of the waypoint
     */
    double getLatitude();

    /**
     *
     * @return longitude of the waypoint
     */
    double getLongitude();

    /**
     *
     * @return double[] containing {latitude, longitude} coordinates
     */
    double[] getCoordinates();

    /**
     *
     * @return absolute tolerance to recognize having reached the destination
     */
    double getTolerance();

    /**
     *
     * @return {@link WaypointBehavior} enum representing behavior to engage in after reaching waypoint
     */
    WaypointBehavior atDestination();

    /**
     * Defines behavior modes for at checkpoints
     *
     * <p>
     * NEXT_WAYPOINT: continue immediately to next waypoint
     * <p>
     * LOITER: keep station at the waypoint
     * </p>
     */
    enum WaypointBehavior {
        NEXT_WAYPOINT, LOITER
    }
}

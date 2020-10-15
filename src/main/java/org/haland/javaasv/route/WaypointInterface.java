package org.haland.javaasv.route;

public interface WaypointInterface {
    double getLattitude();

    double getLongitude();

    double[] getCoordinates();

    double getTolerance();

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

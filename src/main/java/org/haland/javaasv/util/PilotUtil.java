package org.haland.javaasv.util;

import static java.lang.Math.*;

public class PilotUtil {
    /**
     * The earth's radius in nmi
     */
    public static final double EARTH_RADIUS_NMI = 3440.07;

    /**
     * The earth's radius in mi
     */
    public static final double EARTH_RADIUS_MI = 3958.761;

    /**
     * The earth's radius in km
     */
    public static final double EARTH_RADIUS_KM = 6371.009;

    /**
     * The earth's radius in m
     */
    public static final double EARTH_RADIUS_M = EARTH_RADIUS_KM * 1000;

    private PilotUtil() {
        throw new AssertionError("utility class");
    }

    /**
     * Calculates the initial bearing between two points, in radians
     *
     * @param previousWaypoint
     * @param nextWaypoint
     * @return the initial bearing in radians
     */
    public static double calculateInitialBearingRadians(double[] previousWaypoint, double[] nextWaypoint) {
        double firstLat = toRadians(previousWaypoint[0]);
        double firstLong = toRadians(previousWaypoint[1]);

        double secondLat = toRadians(nextWaypoint[0]);
        double secondLong = toRadians(nextWaypoint[1]);

        double deltaLong = secondLong - firstLong;

        double y = sin(deltaLong) * cos(secondLat);
        double x = cos(firstLat) * sin(secondLat) - sin(firstLat) * cos(secondLat) * cos(deltaLong);
        double theta = atan2(y, x);
        return (theta + 2 * PI) % (2 * PI);
    }

    /**
     * Calculates the initial bearing between two points, in degrees
     *
     * @param previousWaypoint
     * @param nextWaypoint
     * @return the initial bearing in degrees
     */
    public static double calculateInitialBearing(double[] previousWaypoint, double[] nextWaypoint) {
        return toDegrees(calculateInitialBearingRadians(previousWaypoint, nextWaypoint));
    }

    /**
     * Calculates the angular distance between two GPS points
     *
     * @param firstPoint
     * @param secondPoint
     * @return angular distance in radians
     */
    public static double calculateAngularDistance(double[] firstPoint, double[] secondPoint) {
        double firstLat = toRadians(firstPoint[0]);
        double firstLong = toRadians(firstPoint[1]);

        double secondLat = toRadians(secondPoint[0]);
        double secondLong = toRadians(secondPoint[1]);

        double deltaLong = secondLong - firstLong;
        double deltaLat = secondLat - firstLat;

        double centralAngleRadians = archav(hav(deltaLat) + cos(firstLat) * cos(secondLat) * hav(deltaLong));

        return centralAngleRadians;
    }

    /**
     * Calculates the great circle distance between two GPS points using the haversine formula
     *
     * @param firstPoint
     * @param secondPoint
     * @param earthRadius Radius of the earth in desired units
     * @return Great circle distance between the points in units of earthRadius
     */
    public static double calculateDistance(double[] firstPoint, double[] secondPoint, double earthRadius) {
        return calculateAngularDistance(firstPoint, secondPoint) * earthRadius;
    }

    /**
     * Calculates the great circle distance between two GPS points using the haversine formula
     *
     * @param firstPoint
     * @param secondPoint
     * @return Great circle distance between the points in units of nmi
     */
    public static double calculateDistance(double[] firstPoint, double[] secondPoint) {
        return calculateAngularDistance(firstPoint, secondPoint) * EARTH_RADIUS_NMI;
    }

    /**
     * Cross-track distance represents the length of a great circle normal to the path and passing through the ASV
     * position
     *
     * @param prevWaypoint start waypoint describing path
     * @param nextWaypoint end waypoint describing path
     * @param position     position of ASV
     * @return the cross-track distance in nmi
     */
    public static double calculateCrossTrackDistance(double[] prevWaypoint, double[] nextWaypoint, double[] position) {
        return asin(sin(calculateAngularDistance(nextWaypoint, prevWaypoint)) * sin(
                calculateInitialBearingRadians(prevWaypoint, position) - calculateInitialBearingRadians(prevWaypoint,
                        nextWaypoint))) * EARTH_RADIUS_NMI;
    }

    /**
     * Cross-track distance represents the length of a great circle normal to the path and passing through the ASV
     * position
     *
     * @param prevWaypoint start waypoint describing path
     * @param nextWaypoint end waypoint describing path
     * @param position     position of ASV
     * @return the cross-track distance in units of earthRadius
     */
    public static double calculateCrossTrackDistance(double[] prevWaypoint, double[] nextWaypoint, double[] position,
                                                     double earthRadius) {
        return asin(sin(calculateAngularDistance(nextWaypoint, prevWaypoint)) * sin(
                calculateInitialBearingRadians(prevWaypoint, position) - calculateInitialBearingRadians(prevWaypoint,
                        nextWaypoint))) * earthRadius;
    }

    /**
     * The haversine function
     *
     * @param theta
     * @return
     */
    public static double hav(double theta) {
        return pow(sin(theta / 2), 2);
    }

    /**
     * Inverse haversine function
     *
     * @param value
     * @return
     */
    public static double archav(double value) {
        return 2 * asin(sqrt(value));
    }
}

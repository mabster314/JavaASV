package org.haland.javaasv.pilot;

import net.sf.marineapi.nmea.util.Time;

/**
 * Interface for a class providing GPS data to the pilot
 */
public interface GPSProviderInterface {
    /**
     * Provides the current fix status of the GPS
     * @return True if the GPS currently has a fix, False otherwise
     */
    boolean getFixStatus();

    /**
     * Provides the time of the most recent GPS update
     * @return {@link Time} object containing the time of the last update
     */
    Time getUpdateTime();

    /**
     * Provides the latitude of the most recent GPS update
     * @return latitude as a double
     */
    double getLatitude();

    /**
     * Provides the longitude of the most recent GPS update
     * @return longitude as a double
     */
    double getLongitude();

    /**
     * Provides the coordinates of the most recent GPS update
     * @return double[] containing the current {latitude, longitude}
     */
    double[] getCoordinates();

    /**
     * Provides the heading of the most recent GPS update
     * @return the heading in degrees
     */
    double getHeading();
}

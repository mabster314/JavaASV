package org.haland.javaasv.pilot;

import net.sf.marineapi.nmea.util.Time;


public interface GPSProviderInterface {
    boolean getFixStatus();

    Time getUpdateTime();

    double getLatitude();

    double getLongitude();

    double[] getCoordinates();

    double getHeading();
}

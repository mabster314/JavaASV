package org.haland.javaasv.pilot;

public interface GPSProviderInterface extends Runnable{
    boolean getFixStatus();

    double getUpdateTime();

    double getLatitude();

    double getLongitude();

    double getHeading();
}

package org.haland.javaasv.pilot;

import java.util.concurrent.Callable;

public interface GPSProviderInterface extends Runnable{
    boolean getFixStatus();

    double getUpdateTime();

    double getLatitude();

    double getLongitude();

    double getHeading();
}

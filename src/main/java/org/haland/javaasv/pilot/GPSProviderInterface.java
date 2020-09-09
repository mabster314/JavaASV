package org.haland.javaasv.pilot;

import java.util.concurrent.Callable;

public interface GPSProviderInterface extends Callable<double[]> {
    /**
     * Call function to provide GPS data
     * @return double[2] containing latitude and longitude
     */
    @Override
    double[] call();
}

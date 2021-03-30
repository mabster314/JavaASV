package org.haland.javaasv.util;

/**
 * Provides various values of the Earth's radius for navigational purposes
 */
public enum EarthRadius {
    /**
     * Earth's radius in nautical miles
     */
    NMI(3440.07),

    /**
     * Earth's radius in miles
     */
    MI(3958.761),

    /**
     * Earth's radius in kilometers
     */
    KM(6371.009),

    /**
     * Earth's radius in meters
     */
    METERS(KM.getValue() * 1000);
    private double radius;

    EarthRadius(double radius) {
        this.radius = radius;
    }

    /**
     * Provides the radius value as a double in the units corresponding to the value of the enum
     * @return Earth's radius in the specified units
     */
    public double getValue() {
        return radius;
    }
}

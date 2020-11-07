package org.haland.javaasv.util;

public enum EarthRadius {
    NMI(3440.07),
    MI(3958.761),
    KM(6371.009),
    METERS(KM.getValue() * 1000);
    private double radius;

    private EarthRadius(double radius) {
        this.radius = radius;
    }

    public double getValue() {
        return radius;
    }
}

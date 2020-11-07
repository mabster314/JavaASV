package org.haland.javaasv.route;

public class Waypoint implements WaypointInterface {
    private double latitude;
    private double longitude;
    private double tolerance;
    private WaypointBehavior behavior;

    public Waypoint(double latitude, double longitude, double tolerance, WaypointBehavior behavior) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.tolerance = tolerance;
        this.behavior = behavior;
    }

    public Waypoint(double[] coordinates, double tolerance, WaypointBehavior behavior) {
        this.latitude = coordinates[0];
        this.longitude = coordinates[1];
        this.tolerance = tolerance;
        this.behavior = behavior;
    }

    @Override
    public double getLatitude() {
        return this.latitude;
    }

    @Override
    public double getLongitude() {
        return this.longitude;
    }

    @Override
    public double[] getCoordinates() {
        return new double[]{latitude, longitude};
    }

    @Override
    public double getTolerance() {
        return this.tolerance;
    }

    @Override
    public WaypointBehavior atDestination() {
        return this.behavior;
    }
}

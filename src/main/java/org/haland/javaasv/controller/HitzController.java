package org.haland.javaasv.controller;

import org.haland.javaasv.config.unit.HitzConfigUnit;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.util.MathUtil;

public class HitzController implements Controller {

    private double Kph;
    private double Kpx;
    private double Kix;
    private RouteInterface route;

    private boolean usePeriod;
    private double period;

    private long lastUpdate;

    private double previousError;
    private double positionError;
    double totalError;

    public HitzController(double Kph, double Kpx, double Kix) {
        this.Kph = Kph;
        this.Kpx = Kpx;
        this.Kix = Kix;
    }

    public HitzController(HitzConfigUnit config) {
        this.Kph = config.getKph();
        this.Kpx = config.getKpx();
        this.Kix = config.getKix();
    }

    public void setRoute(RouteInterface route) {
        this.route = route;
    }

    /**
     * Requires three process variables: cross-track distance, heading error, and throttle
     *
     * @param processVariables doubles for xtd, heading error, and throttle error. Exactly three doubles must be
     *                         provided, and in the correct order.
     * @return
     */
    @Override
    public double calculateNextOutput(double... processVariables) {
        double xtd;
        double headingError;
        double throttle;
        if (processVariables.length == 3) {
            xtd = processVariables[0];
            headingError = processVariables[1];
            throttle = processVariables[2];
        } else {
            throw new IllegalArgumentException("Hitz controller requires three parameters");
        }

        previousError = positionError;

        positionError = xtd;

        double deltaT;
        if (!usePeriod) {
            deltaT = ((double) (System.currentTimeMillis() - lastUpdate)) / 1000;
        } else {
            deltaT = period;
        }

        totalError = totalError + previousError + positionError * deltaT;

        double out = (Kph * headingError)
                + (Math.sin(headingError) / headingError) * (-Kpx * xtd + Kix * totalError * deltaT);
        return MathUtil.clamp(out, -100, 100);
    }

    @Override
    public void start() {
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public ControllerType getType() {
        return ControllerType.HITZ;
    }

    public double getKph() {
        return Kph;
    }

    public void setKph(double kph) {
        Kph = kph;
    }

    public double getKpx() {
        return Kpx;
    }

    public void setKpx(double kpx) {
        Kpx = kpx;
    }

    public double getKix() {
        return Kix;
    }

    public void setKix(double kix) {
        Kix = kix;
    }

    public RouteInterface getRoute() {
        return route;
    }

    public double getTotalError() {
        return totalError;
    }

    public void setTotalError(double totalError) {
        this.totalError = totalError;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
        this.usePeriod = true;
    }
}

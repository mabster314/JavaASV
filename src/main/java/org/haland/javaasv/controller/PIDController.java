/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2020  Max Haland
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.haland.javaasv.controller;

import org.haland.javaasv.config.unit.PIDConfigUnit;
import org.haland.javaasv.util.MathUtil;

/**
 * Implements a basic PID controller.
 */
public class PIDController implements Controller {
    /**
     * The default period for the PID controller, in seconds
     */
    private final static double DEFAULT_PERIOD = 0.05;

    private double proportionalityCoefficient;
    private double integralCoefficient;
    private double derivativeCoefficient;
    private double period;

    private long lastUpdate;

    // default minimum and maximum integral
    private double minimumIntegral = -1.0;
    private double maximumIntegral = 1.0;

    private double positionError;
    private double velocityError;

    private double previousError;

    private double totalError;

    // Allowed error from setpoint
    private double positionTolerance = 0.05;
    private double velocityTolerance = Double.POSITIVE_INFINITY;

    // setpoint of the controller
    private double setpoint;

    /**
     * Constructs a PIDController with the given constants and default controller period
     *
     * @param proportionalityCoefficient the proportionality coefficient for the controller
     * @param integralCoefficient        the integral coefficient for the controller
     * @param derivativeCoefficient      the derivative coefficient for the controller
     */
    public PIDController(double proportionalityCoefficient, double integralCoefficient, double derivativeCoefficient) {
        this(proportionalityCoefficient, integralCoefficient, derivativeCoefficient, DEFAULT_PERIOD);
    }

    /**
     * Constructs a PIDController with the given constants and controller period
     *
     * @param proportionalityCoefficient the proportionality coefficient for the controller
     * @param integralCoefficient        the integral coefficient for the controller
     * @param derivativeCoefficient      the derivative coefficient for the controller
     * @param period                     the period for the controller in seconds
     */
    public PIDController(double proportionalityCoefficient, double integralCoefficient, double derivativeCoefficient,
                         double period) {
        this.proportionalityCoefficient = proportionalityCoefficient;
        this.integralCoefficient = integralCoefficient;
        this.derivativeCoefficient = derivativeCoefficient;
        this.period = period;
    }

    public PIDController(PIDConfigUnit config) {
        this(config.getKp(), config.getKi(), config.getKd(), config.getPeriod());
    }

    /**
     * Returns true if the error is within the tolerance of the setpoint
     *
     * @return whether the error is within acceptable bounds
     */
    public boolean atSetpoint() {
        return Math.abs(positionError) < positionTolerance && Math.abs(velocityError) < velocityTolerance;
    }

    @Override
    public void start() {
        lastUpdate = System.currentTimeMillis();
        setSetpoint(0);
    }

    @Override
    public ControllerType getType() {
        return ControllerType.PID;
    }

    /**
     * Calculates the next output for the control variable. This must be called at the period of the controller
     *
     * @param processVariables the current measurement of the monitored process variable
     * @return the next output for the controller
     */
    @Override
    public double calculateNextOutput(double... processVariables) {
        if (processVariables.length == 1) {
            double processVariable = processVariables[0];

            previousError = positionError;

            positionError = setpoint - processVariable;

            Double deltaT = ((double) (System.currentTimeMillis() - lastUpdate)) / 1000;

            velocityError = (positionError - previousError) / deltaT;

            if (integralCoefficient != 0) {
                totalError = MathUtil.clamp(totalError + positionError * deltaT,
                        minimumIntegral / integralCoefficient, maximumIntegral / integralCoefficient);
            }

            lastUpdate = System.currentTimeMillis();
            double out = proportionalityCoefficient * positionError + integralCoefficient * totalError
                    + derivativeCoefficient * velocityError;
            return MathUtil.clamp(out, -100, 100);
        } else {
            throw new IllegalArgumentException("PID controller requires one argument");
        }
    }

    public void setPID(double proportionalityCoefficient, double integralCoefficient, double derivativeCoefficient) {
        this.proportionalityCoefficient = proportionalityCoefficient;
        this.integralCoefficient = integralCoefficient;
        this.derivativeCoefficient = derivativeCoefficient;
    }

    /**
     * Sets the minimum and maximum values for the integrator term
     * <p>
     * When the cap is reached, the max or min value is added to the controller instead of the integrator times the gain
     *
     * @param mimimumIntegral the minimum value of the integrator
     * @param maximumIntegral the maximum value of the integrator
     */
    public void setIntegratorRange(double mimimumIntegral, double maximumIntegral) {
        this.minimumIntegral = mimimumIntegral;
        this.maximumIntegral = maximumIntegral;
    }

    /**
     * Sets the tolerable error from setpoint
     *
     * @param positionTolerance position error which is tolerable
     */
    public void setTolerance(double positionTolerance) {
        setTolerance(positionTolerance, Double.POSITIVE_INFINITY);
    }

    /**
     * Sets the tolerable error from setpoint
     *
     * @param positionTolerance position error which is tolerable
     * @param velocityTolerance velocity error which is tolerable
     */
    public void setTolerance(double positionTolerance, double velocityTolerance) {
        this.positionTolerance = positionTolerance;
        this.velocityTolerance = velocityTolerance;
    }

    public double getProportionalityCoefficient() {
        return proportionalityCoefficient;
    }

    public void setProportionalityCoefficient(double proportionalityCoefficient) {
        this.proportionalityCoefficient = proportionalityCoefficient;
    }

    public double getIntegralCoefficient() {
        return integralCoefficient;
    }

    public void setIntegralCoefficient(double integralCoefficient) {
        this.integralCoefficient = integralCoefficient;
    }

    public double getDerivativeCoefficient() {
        return derivativeCoefficient;
    }

    public void setDerivativeCoefficient(double derivativeCoefficient) {
        this.derivativeCoefficient = derivativeCoefficient;
    }

    public double getPeriod() {
        return period;
    }

    public void setPeriod(double period) {
        this.period = period;
    }

    public double getSetpoint() {
        return setpoint;
    }

    public void setSetpoint(double setpoint) {
        this.setpoint = setpoint;
    }

    public double getPositionError() {
        return positionError;
    }

    public double getVelocityError() {
        return velocityError;
    }
}

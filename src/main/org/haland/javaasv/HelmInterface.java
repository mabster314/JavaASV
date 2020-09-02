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

package org.haland.javaasv;

/**
 * Defines the interface between the navigation system and the helm controls.
 * The helm has two requirements: control the rudder and control the throttle.
 */
public interface HelmInterface {
    /**
     * Gets the current throttle position
     * @return the current throttle position
     */
    double getThrottlePosition();

    /**
     * Gets the current throttle setpoint
     * @return the current throttle setpoint
     */
    double getThrottleSetpoint();

    /**
     * Sets a new throttle setpoint
     * @param newSetpoint the new throttle setpoint
     */
    void setThrottleSetpoint(double newSetpoint);

    /**
     * Gets the current rudder position
     * @return the current rudder position
     */
    double getRudderPosition();

    /**
     * Gets the current rudder setpoint
     * @return the current rudder setpoint
     */
    double getRudderSetpoint();

    /**
     * Sets a new rudder setpoint
     * @param newSetpoint the new rudder setpoint
     */
    void setRudderSetpoint(double newSetpoint);
}

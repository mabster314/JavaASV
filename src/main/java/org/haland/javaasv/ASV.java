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

import org.haland.javaasv.helm.ArduinoHelm;
import org.haland.javaasv.helm.HelmArduino;
import org.haland.javaasv.message.MessengerServer;
import org.haland.javaasv.pilot.GPSHatParser;
import org.haland.javaasv.pilot.SimplePilot;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.util.PIDController;

/**
 * Our entry point
 */
public class ASV {
    private MessengerServer server;
    private static final long SERVER_PERIOD = 10;

    private ArduinoHelm helm;
    private HelmArduino helmArduino;
    private static final String HELM_ID = "helm";
    private static final String ARDUINO_PORT = "/dev/ttyACM0";

    private SimplePilot pilot;
    private static final String PILOT_ID = "pilot";
    private static final long PILOT_PERIOD = 1000;

    private PIDController throttlePID;
    private PIDController rudderPID;

    private GPSHatParser gps;
    private static final String GPS_PORT = "/dev/ttyS0";

    private RouteInterface route;

    private ASV() {
        this.server = MessengerServer.getInstance();

        this.helmArduino = new HelmArduino(ARDUINO_PORT);
        this.helm = new ArduinoHelm(server, helmArduino, HELM_ID);

        this.gps = new GPSHatParser(GPS_PORT);

        this.pilot = new SimplePilot(PILOT_ID, server, helm, throttlePID, rudderPID, gps);
        pilot.setCurrentRoute(route);
    }

    private void start() {
        server.startServer(SERVER_PERIOD);
        pilot.startPilot(PILOT_PERIOD);
    }

    public static void main(String[] args) {
        ASV asv = new ASV();
        asv.start();
    }

}

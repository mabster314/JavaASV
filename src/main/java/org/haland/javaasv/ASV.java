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

import org.haland.javaasv.config.AllConfig;
import org.haland.javaasv.controller.HitzController;
import org.haland.javaasv.controller.PIDController;
import org.haland.javaasv.controller.TrivialController;
import org.haland.javaasv.helm.ArduinoHelm;
import org.haland.javaasv.helm.HelmArduino;
import org.haland.javaasv.message.MessengerServer;
import org.haland.javaasv.pilot.GPSHatParser;
import org.haland.javaasv.pilot.SimplePilot;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.route.RouteParser;
import org.tinylog.Logger;


/**
 * Our entry point
 */
public class ASV {
    // Load the configuration
    private final AllConfig config = new AllConfig();

    private MessengerServer server;
    private long serverPeriod;

    private ArduinoHelm helm;
    private HelmArduino helmArduino;
    private static final String HELM_ID = "helm";

    private SimplePilot pilot;
    private static final String PILOT_ID = "pilot";
    private long pilotPeriod;

    private TrivialController throttleController;
    private PIDController rudderController;

    private GPSHatParser gps;

    private RouteInterface route;

    private ASV() {
        Logger.info("Starting ASV configuration");

        // Get server and client data from config
        this.serverPeriod = config.getAsvConfig().getServerPeriod();
        this.pilotPeriod = config.getAsvConfig().getPilotPeriod();

        // Get the server instance
        this.server = MessengerServer.getInstance();

        // Construct a new HelmArduino and ArduinoHelm
        this.helmArduino = new HelmArduino(config);
        this.helm = new ArduinoHelm(server, helmArduino, HELM_ID);

        // Construct a new GPS
        this.gps = new GPSHatParser(config);

        // Create th*/`e throttle and rudder controllers
        this.throttleController = new TrivialController(config);
        this.rudderController = new PIDController(config.getControllerConfig().getRudderPIDConfig());

        // Get a simple route from the config
        this.route = new RouteParser(config).getRoute();

        // Construct the pilot and assign the route
        this.pilot = new SimplePilot(PILOT_ID, server, helm, throttleController, rudderController, gps);
        pilot.setCurrentRoute(route);
    }

    private void start() throws InterruptedException {
        server.startServer(serverPeriod);
        gps.startGPS();
        pilot.startPilot(pilotPeriod);
    }

    public static void main(String[] args) throws InterruptedException {
        ASV asv = new ASV();
        asv.start();
    }

}

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
import org.haland.javaasv.helm.ArduinoHelm;
import org.haland.javaasv.helm.HelmArduino;
import org.haland.javaasv.message.MessengerServer;
import org.haland.javaasv.pilot.GPSHatParser;
import org.haland.javaasv.pilot.SimplePilot;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.route.Waypoint;
import org.haland.javaasv.route.WaypointInterface;
import org.haland.javaasv.util.PIDController;
import org.haland.javaasv.util.TrivialController;
import org.tinylog.Logger;

/**
 * Our entry point
 */
public class ASV {
    private final AllConfig config = new AllConfig();

    private MessengerServer server;
    private static final long SERVER_PERIOD = 10;

    private ArduinoHelm helm;
    private HelmArduino helmArduino;
    private static final String HELM_ID = "helm";

    private SimplePilot pilot;
    private static final String PILOT_ID = "pilot";
    private static final long PILOT_PERIOD = 1000; //TODO propertize this

    private TrivialController throttleController;
    private PIDController rudderController;

    private GPSHatParser gps;

    private RouteInterface route = new RouteInterface() {
        @Override
        public WaypointInterface getPreviousWaypoint() {
            return new Waypoint(44.9187, -92.8435, 0.0001, WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);
        }

        @Override
        public WaypointInterface getNextWaypoint() {
            return new Waypoint(44.9187, -92.8439, 0.0001, WaypointInterface.WaypointBehavior.NEXT_WAYPOINT);
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    };

    private ASV() {
        Logger.info("Starting ASV configuration");
        this.server = MessengerServer.getInstance();

        this.helmArduino = new HelmArduino(config.getSerialConfig().getArduinoPort());
        this.helm = new ArduinoHelm(server, helmArduino, HELM_ID);

        this.gps = new GPSHatParser(config.getSerialConfig().getGpsPort());

        this.throttleController = new TrivialController(config.getControllerConfig().getThrottleValue());
        this.rudderController = new PIDController(config.getControllerConfig().getRudderKp(),
                config.getControllerConfig().getRudderKi(), config.getControllerConfig().getRudderKd(),
                config.getControllerConfig().getRudderPeriod());

        this.pilot = new SimplePilot(PILOT_ID, server, helm, throttleController, rudderController, gps);
        pilot.setCurrentRoute(route);
    }

    private void start() throws InterruptedException {
        server.startServer(SERVER_PERIOD);
        gps.startGPS();
        pilot.startPilot(PILOT_PERIOD);
    }

    public static void main(String[] args) throws InterruptedException {
        ASV asv = new ASV();
        asv.start();
    }

}

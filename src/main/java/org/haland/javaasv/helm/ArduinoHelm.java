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

package org.haland.javaasv.helm;

import org.haland.javaasv.message.MessageInterface;
import org.haland.javaasv.message.MessengerClientInterface;
import org.haland.javaasv.message.MessengerServer;
import org.haland.javaasv.util.PIDController;
import org.haland.javaasv.util.SerialArduino;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * Controls helm by interfacing with an Arduino. This is a singleton class, and only one should be run.
 */
public class ArduinoHelm implements HelmInterface {
    /**
     * The client ID for an ArduinoHelm
     */
    public final String DEFAULT_CLIENT_ID = "arduinoHelmClient";

    private MessengerServer server;
    private HelmArduino helmArduino;
    private String clientID;

    private ArduinoHelmMessageFactory messageFactory = null;

    private boolean isRunning = false;

    /**
     * Constructs a new instance with a given {@link MessengerServer}, {@link SerialArduino}, and clientID
     * @param server The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     * @param clientID ID of the client module. This must be unique.
     */
    public ArduinoHelm(MessengerServer server, HelmArduino helmArduino, String clientID) {
        this.helmArduino = helmArduino;
        this.server = server;
        this.clientID = clientID;
    }

    /**
     * Constructs a new instance with a given {@link MessengerServer}, {@link SerialArduino}, and default clientID
     * @param server The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     */
    public ArduinoHelm(MessengerServer server, HelmArduino helmArduino) {
        this.helmArduino = helmArduino;
        this.server = server;
        this.clientID = DEFAULT_CLIENT_ID;
    }

    /**
     * Dispatches a {@link HelmMessage} by sending its contents to the arduino and sending a return message with the
     * actual rudder and
     * @param message {@link HelmMessage} containing new throttle and rudder data
     */
    @Override
    public void dispatch(HelmMessage message) {
        try {
            helmArduino.sendSerialData(message.getMessageContents().getBytes(StandardCharsets.US_ASCII));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Initialize our message factory with the correct pilot client ID
        if (messageFactory == null) messageFactory = new ArduinoHelmMessageFactory(clientID, message.getOriginID());

        // Send a return message with the actual state of the controls
        server.dispatch(messageFactory.createMessage(helmArduino.getHelmState()));
    }

    @Override
    public String getClientID() {
        return clientID;
    }
}
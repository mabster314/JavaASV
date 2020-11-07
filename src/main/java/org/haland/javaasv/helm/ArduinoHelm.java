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

import org.haland.javaasv.message.*;
import org.haland.javaasv.util.SerialArduino;
import org.tinylog.Logger;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Controls helm by interfacing with an Arduino.
 */
public class ArduinoHelm implements HelmInterface {
    /**
     * The client ID for an ArduinoHelm
     */
    public static final String DEFAULT_CLIENT_ID = "arduinoHelmClient";

    private MessengerServerInterface server;
    private HelmArduino helmArduino;
    private String clientID;

    private ArduinoHelmMessageFactory messageFactory = null;
    private ExecutorService executor;

    private boolean isRunning = false;

    /**
     * Constructs a new instance with a given {@link MessengerServer}, {@link SerialArduino}, and clientID
     *
     * @param server      The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     * @param clientID    ID of the client module. This must be unique.
     */
    public ArduinoHelm(MessengerServerInterface server, HelmArduino helmArduino, String clientID) {
        Logger.info("Attempting to instantiate ArduinoHelm");

        this.helmArduino = helmArduino;
        this.server = server;
        this.clientID = clientID;
        executor = Executors.newFixedThreadPool(1);
        openPort();

        try {
            server.registerClientModule(this);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    /**
     * Constructs a new instance with a given {@link MessengerServer}, {@link SerialArduino}, and default clientID
     *
     * @param server      The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     */
    public ArduinoHelm(MessengerServerInterface server, HelmArduino helmArduino) {
        this(server, helmArduino, DEFAULT_CLIENT_ID);
    }

    /**
     * Opens the arduino port
     *
     * @return true if the operation was completed successfully
     */
    public boolean openPort() {
        return helmArduino.openPort();
    }

    /**
     * Closes the arduino port
     *
     * @return true if the operation was completed successfully
     */
    public boolean closePort() {
        return helmArduino.closePort();
    }

    /**
     * Dispatches a {@link MessageInterface} by sending its contents to the arduino
     *
     * @param message {@link MessageInterface} containing new throttle and rudder data
     */
    @Override
    public void dispatch(MessageInterface message) {
        if (message.getType() == getClientType()) {
            try {
                String data = message.getMessageContents().getHelmMessage();
                Logger.trace("Helm command sent: " + data);
                helmArduino.sendSerialData(data.getBytes(StandardCharsets.US_ASCII));
            } catch (MessageTypeException e) {
                e.printStackTrace();
            }
        } else {
            // TODO do something
        }
    }

    @Override
    public String getClientID() {
        return clientID;
    }

    @Override
    public MessageInterface.MessageType getClientType() {
        return MessageInterface.MessageType.HELM;
    }
}

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
import org.haland.javaasv.util.PIDController;
import org.haland.javaasv.util.SerialArduino;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Controls helm by interfacing with an Arduino. This is a singleton class, and only one should be run.
 */
public class ArduinoHelm implements MessengerClientInterface {
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
     * @param server The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     * @param clientID ID of the client module. This must be unique.
     */
    public ArduinoHelm(MessengerServerInterface server, HelmArduino helmArduino, String clientID) {
        this.helmArduino = helmArduino;
        this.server = server;
        this.clientID = clientID;
        executor = Executors.newFixedThreadPool(1);
    }

    /**
     * Constructs a new instance with a given {@link MessengerServer}, {@link SerialArduino}, and default clientID
     * @param server The {@link MessengerServer} being used by the navigation software
     * @param helmArduino The {@link SerialArduino} controlling the throttle and rudder
     */
    public ArduinoHelm(MessengerServerInterface server, HelmArduino helmArduino) {
        this(server, helmArduino, DEFAULT_CLIENT_ID);
    }

    /**
     * Opens the arduino port
     * @return true if the operation was completed successfully
     */
    public boolean openPort() {
        return helmArduino.openPort();
    }

    /**
     * Closes the arduino port
     * @return true if the operation was completed successfully
     */
    public boolean closePort() {
        return helmArduino.closePort();
    }

    /**
     * Dispatches a {@link MessageInterface} by sending its contents to the arduino and sending a return message with the
     * actual rudder and
     * @param message {@link MessageInterface} containing new throttle and rudder data
     */
    @Override
    public void dispatch(MessageInterface message) {
        if (message.getType() == getClientType()) {
            try {
                helmArduino.sendSerialData(message.getMessageContents().getHelmMessage()
                        .getBytes(StandardCharsets.US_ASCII));
            } catch (MessageTypeException e) {
                e.printStackTrace();
            }
        } else {
            // TODO do something
        }
        // Initialize our message factory with the correct pilot client ID
        if (messageFactory == null) {
            messageFactory = new ArduinoHelmMessageFactory(clientID, message.getOriginID());
        }

        // Get the actual state from the arduino
        Future<String> helmStateFuture;
        helmStateFuture = executor.submit(helmArduino);
        String helmState = null;
        try {
            helmState = helmStateFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        // Send a return message with the actual state of the controls
        try {
            server.dispatch(messageFactory.createMessage(helmState));
        } catch (MessageTypeException e) {
            e.printStackTrace();
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

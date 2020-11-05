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

package org.haland.javaasv.message;

import org.tinylog.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Handles message passing for the system. Only one should exist
 */
public class MessengerServer implements MessengerServerInterface {
    // Our instance of the server
    private static MessengerServer messengerServerInstance = null;
    // The server's executor service
    private static ScheduledExecutorService executor = null;

    /**
     * Stack representing messages to handle
     */
    private Stack<MessageInterface> messageStack;

    /**
     * Map representing registered client modules
     */
    private Map<String, MessengerClientInterface> clients;

    /**
     * Creates a new instance of the server
     */
    private MessengerServer(ScheduledExecutorService executor) {
        clients = new HashMap<String, MessengerClientInterface>();
        messageStack = new Stack<MessageInterface>();
        this.executor = executor;
        Logger.info("Server constructed");
    }

    /**
     * Gets the current instance of the server
     *
     * @return the current MessengerServer instance
     */
    public static MessengerServer getInstance() {
        // Make a new one if we don't have one
        if (messengerServerInstance == null)
            messengerServerInstance = new MessengerServer(Executors.newScheduledThreadPool(1));

        // Give the instance
        return messengerServerInstance;
    }

    public static void killInstance() {
        messengerServerInstance = null;
        executor.shutdownNow();
        executor = null;
    }

    /**
     * Adds a module to the registry with a specific client name
     *
     * @param clientID     the name of the module to register
     * @param clientModule the module to register
     * @throws DuplicateKeyException if a module with a duplicate ID is registered
     */
    @Override
    public void registerClientModule(String clientID,
                                     MessengerClientInterface clientModule) throws DuplicateKeyException {
        Logger.info("Attempting to attach client " + clientModule.getClass() + " with name " +clientID);
        if (clients.putIfAbsent(clientID, clientModule) != null) {
            DuplicateKeyException e = new DuplicateKeyException("Duplicate client registered to server: "
                    + clientModule.getClientID());
            Logger.error(e);
            throw e;
        }
    }

    /**
     * Adds a module to the registry
     *
     * @param clientModule the module to register
     * @throws DuplicateKeyException if a module with a duplicate ID is registered
     */
    @Override
    public void registerClientModule(MessengerClientInterface clientModule) throws DuplicateKeyException {
        registerClientModule(clientModule.getClientID(), clientModule);
    }

    /**
     * Dispatches a message to the stack
     *
     * @param message the message being dispatched
     */
    @Override
    public synchronized void dispatch(MessageInterface message) {
        Logger.trace("Message from module " + message.getOriginID() + " and to module " + message.getDestinationID()
                + " added to queue");
        messageStack.push(message);
    }

    /**
     * Calls the {@link MessengerClientInterface#dispatch(MessageInterface)} method of the destination client for each
     * message in the stack
     */
    @Override
    public synchronized void run() {
        while (!messageStack.empty()) {
            MessageInterface message = messageStack.pop();
            MessengerClientInterface client = clients.get(message.getDestinationID());

            if (message.getType() == client.getClientType()) {
                Logger.trace("Message from module " + message.getOriginID() + " dispatched to "
                        + message.getDestinationID());
                client.dispatch(message);
            } else {
                Logger.trace("Message from module " + message.getOriginID() + " wrong message type for "
                        + message.getDestinationID() + "; message was dropped");
            }
        }
    }

    /**
     * Starts the server by scheduling it repeatedly
     *
     * @param period execution period
     */
    public void startServer(long period) {
        executor.scheduleAtFixedRate(messengerServerInstance, 0, period, TimeUnit.MILLISECONDS);
        Logger.info("Started server at period " + period);
    }

    public void stopServer() {
        executor.shutdown();
        Logger.info("Stopping server");
    }
}

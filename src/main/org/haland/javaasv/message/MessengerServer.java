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

import java.util.HashMap;
import java.util.Map;

/**
 * Handles message passing for the system. Only one should exist
 */
public class MessengerServer implements MessengerServerInterface {
    // Our instance of the server
    private static MessengerServer messengerServerInstance = null;

    /**
     * Creates a new instance of the server
     */
    private MessengerServer(){
        clients = new HashMap<String, MessengerClientInterface>();
    }

    /**
     * Gets the current instance of the server
     * @return the current MessengerServer instance
     */
    public static MessengerServer getInstance(){
        // Make a new one if we don't have one
        if (messengerServerInstance == null)
            messengerServerInstance = new MessengerServer();

        // Give the instance
        return messengerServerInstance;
    }

    /**
     * Map representing registered client modules
     */
    private Map<String, MessengerClientInterface> clients;

    /**
     * Adds a module to the registry
     * @param clientID the name of the module to register
     * @param clientModule the module to register
     */
    @Override
    public void registerClientModule(String clientID, MessengerClientInterface clientModule) {
        clients.put(clientID, clientModule);
    }

    /**
     * Dispatches a message to a client by calling the {@link MessengerClientInterface#dispatch(MessageInterface)}
     * method of the destination module
     * @param message the message being dispatched
     */
    @Override
    public void dispatch(MessageInterface message) {
        clients.get(message.getDestinationID()).dispatch(message);
    }
}

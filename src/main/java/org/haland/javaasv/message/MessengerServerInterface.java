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

/**
 * Defines the messaging interface for server modules
 */
public interface MessengerServerInterface extends Runnable {
    /**
     * Registers a client module to the server
     *
     * @param clientID     the name of the module to register
     * @param clientModule the module to register
     */
    void registerClientModule(String clientID, MessengerClientInterface clientModule) throws DuplicateKeyException;

    void registerClientModule(MessengerClientInterface clientModule) throws DuplicateKeyException;

    /**
     * Dispatches a message to the destination module. The server should check that the client is able to receive
     * messages of that type.
     *
     * @param message the message being dispatched
     */
    void dispatch(MessageInterface message);
}

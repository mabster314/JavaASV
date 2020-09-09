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
 * Defines the required structure of an ASV message
 */
public interface MessageInterface {
    /**
     * Returns the origin module ID for the message
     *
     * @return the origin module ID
     */
    String getOriginID();

    /**
     * Returns the destination module ID for the message
     *
     * @return the destination module ID
     */
    String getDestinationID();

    /**
     * Returns the time the message was created
     *
     * @return the time the message was created
     */
    long getCreationTime();

    /**
     * Returns the priority of the message
     *
     * @return the message priority
     */
    MessagePriority getPriority();

    /**
     * Returns the type of message
     *
     * @return the message type
     */
    MessageType getType();

    /**
     * Returns the actual contents of the message
     *
     * @return the contents of the message
     */
    MessageContent getMessageContents();

    /**
     * Enum defining available message priorities. Priorities should be listed in order of severity.
     */
    enum MessagePriority {
        LOW, NORMAL, HIGH, CRITICAL
    }

    enum MessageType {
        STRING, DOUBLE, HELM
    }

}

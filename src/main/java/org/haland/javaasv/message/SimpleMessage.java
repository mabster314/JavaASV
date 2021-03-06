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

public class SimpleMessage implements MessageInterface {
    private final String originID;
    private final String destinationID;
    private final long creationTime;
    private final MessagePriority priority;
    private final MessageContent messageContents;

    public SimpleMessage(String originID, String destinationID, long creationTime, MessagePriority priority,
                         MessageContent messageContents) {
        this.originID = originID;
        this.destinationID = destinationID;
        this.creationTime = creationTime;
        this.priority = priority;
        this.messageContents = messageContents;
    }

    @Override
    public String getOriginID() {
        return this.originID;
    }

    @Override
    public String getDestinationID() {
        return this.destinationID;
    }

    @Override
    public long getCreationTime() {
        return this.creationTime;
    }

    @Override
    public MessagePriority getPriority() {
        return this.priority;
    }

    @Override
    public MessageType getType() {
        return messageContents.getContentType();
    }

    @Override
    public MessageContent getMessageContents() {
        return messageContents;
    }
}

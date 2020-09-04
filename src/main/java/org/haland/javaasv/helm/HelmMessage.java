package org.haland.javaasv.helm;

import org.haland.javaasv.message.SimpleMessage;

/**
 * Message class for helm communication
 */
public class HelmMessage extends SimpleMessage<String> {

    public HelmMessage(String originID, String destinationID, long creationTime, MessagePriority priority,
                       String messageContents) {
        super(originID, destinationID, creationTime, priority, messageContents);
    }
}

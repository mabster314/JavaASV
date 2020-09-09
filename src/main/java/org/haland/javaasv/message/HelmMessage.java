package org.haland.javaasv.message;

import org.haland.javaasv.helm.HelmArduino;
import org.haland.javaasv.util.SerialUtil;

public class HelmMessage extends SimpleMessage {
    private final MessageInterface.MessageType messageType = MessageInterface.MessageType.HELM;

    /**
     * Constructs a new helm message with the given parameters
     * @param originID
     * @param destinationID
     * @param creationTime
     * @param priority
     * @param throttleSetpoint
     * @param rudderSetpoint
     * @throws MessageTypeException
     */
    public HelmMessage(String originID, String destinationID, long creationTime, MessagePriority priority,
                       double throttleSetpoint, double rudderSetpoint) throws MessageTypeException {
        super(originID, destinationID, creationTime, priority,
                new MessageContent(null,null,
                        SerialUtil.START_MESSAGE_CHAR.concat(String.valueOf(throttleSetpoint)).concat(",")
                .concat(String.valueOf(rudderSetpoint)).concat(SerialUtil.END_MESSAGE_CHAR)));
    }

    public HelmMessage(String originID, String destinationID, long creationTime, MessagePriority priority,
                       String content) throws MessageTypeException {
        super(originID, destinationID, creationTime, priority, new MessageContent(null, null, content));
    }

    @Override
    public MessageType getType() {
        return messageType;
    }
}

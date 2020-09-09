package org.haland.javaasv.message;


/**
 * Class defining message content structure
 */
public class MessageContent {
    private final String stringMessage;
    private final Double doubleMessage;
    private final String helmMessage;
    private final MessageInterface.MessageType messageType;

    /**
     * Constructs a message content object. Exactly one parameter should be non-null.
     *
     * @param stringMessage String message, or null if using another type
     * @param doubleMessage Double message, or null if using another type
     * @param helmMessage Helm message, or null if using another type
     *
     * @throws MessageTypeException if the number of non-null parameters is not one
     */
    public MessageContent(String stringMessage, Double doubleMessage, String helmMessage)
            throws MessageTypeException {

        if (stringMessage != null) {
            this.messageType = MessageInterface.MessageType.STRING;

            // Throw if the other two are non-null
            if ((doubleMessage != null) || (helmMessage != null)) {
                throw new MessageTypeException("Message already has data: ");
            }
        } else if (doubleMessage != null) {
            this.messageType = MessageInterface.MessageType.DOUBLE;

            // Throw if the remaining parameter is non-null
            if (helmMessage != null) {
                throw new MessageTypeException("Message already has data: ");
            }
        } else if (helmMessage != null) {
            this.messageType = MessageInterface.MessageType.HELM;
        } else {
            messageType = null;
            throw new MessageTypeException("All message data null: ");
        }

        this.stringMessage = stringMessage;
        this.doubleMessage = doubleMessage;
        this.helmMessage = helmMessage;
    }

    public String getStringMessage() throws MessageTypeException{
        if (stringMessage == null) {
            throw new MessageTypeException("No string data in message contents: ");
        }
        return stringMessage;
    }

    public Double getDoubleMessage() throws MessageTypeException{
        if (doubleMessage == null) {
            throw new MessageTypeException("No double data in message contents: ");
        }
        return doubleMessage;
    }

    public String getHelmMessage() throws MessageTypeException{
        if (helmMessage == null) {
            throw new MessageTypeException("No helm data in message contents: ");
        }
        return helmMessage;
    }

    public double getHelmThrottleValue() throws MessageTypeException {
        if (helmMessage == null) {
            throw new MessageTypeException("No helm data in message contents: ");
        }
        String value = helmMessage.replaceAll("[<>]", "").split(",")[0];
        return Double.parseDouble(value);
    }

    public double getHelmRudderValue() throws MessageTypeException {
        if (helmMessage == null) {
            throw new MessageTypeException("No helm data in message contents: ");
        }
        String value = helmMessage.replaceAll("[<>]", "").split(",")[1];
        return Double.parseDouble(value);
    }

    public MessageInterface.MessageType getContentType() {
        return messageType;
    }

}

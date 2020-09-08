package org.haland.javaasv;

import org.haland.javaasv.message.*;

class SendingClient implements MessengerClientInterface, Runnable {
    private final MessengerServerInterface server;
    private final String clientName;
    private final String receiverName;

    public SendingClient(MessengerServerInterface server, String clientName, String receiverName) {
        this.server = server;
        this.clientName = clientName;
        this.receiverName = receiverName;
    }

    private SimpleMessage createMessage() throws MessageTypeException {
        long time = System.currentTimeMillis();
        return new SimpleMessage(clientName, receiverName, time, MessageInterface.MessagePriority.NORMAL,
                new MessageContent("Sent at: " + String.valueOf(time), null, null));
    }

    @Override
    public void run() {
        try {
            server.dispatch(createMessage());
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void dispatch(MessageInterface message) {
        try {
            System.out.println(message.getMessageContents().getStringMessage());
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getClientID() {
        return clientName;
    }

    @Override
    public MessageInterface.MessageType getClientType() {
        return MessageInterface.MessageType.STRING;
    }
}

package org.haland.javaasv.message;

/**
 * A client for integration testing
 */
class TestClient implements MessengerClientInterface, Runnable {
    private final MessengerServerInterface server;
    private final String clientName;
    private final String receiverName;
    private final Dispatcher dispatcher;

    /**
     * Initialize with a server, specified client name, and receiver name
     *
     * @param server       {@link MessengerServerInterface} to interact with
     * @param clientName   client ID as a string
     * @param receiverName receiver ID as a string
     */
    public TestClient(MessengerServerInterface server, String clientName, String receiverName, Dispatcher dispatcher) {
        this.server = server;
        this.clientName = clientName;
        this.receiverName = receiverName;
        this.dispatcher = dispatcher;
    }

    /**
     * Returns a new {@link SimpleMessage} containing test stuff
     *
     * @return the message
     * @throws MessageTypeException
     */
    private SimpleMessage createMessage() throws MessageTypeException {
        long time = System.currentTimeMillis();
        return new SimpleMessage(clientName, receiverName, time, MessageInterface.MessagePriority.NORMAL,
                new MessageContent("Sent at: " + String.valueOf(time), null, null));
    }

    /**
     * Dispatch a new message to the server
     */
    @Override
    public void run() {
        try {
            server.dispatch(createMessage());
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }
    }

    /**
     * Dispatch a received message by printing its contents
     *
     * @param message the received message
     */
    @Override
    public void dispatch(MessageInterface message) {
        dispatcher.dispatch();
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

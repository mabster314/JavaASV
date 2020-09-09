package org.haland.javaasv.message;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MessageContentTest {
    private static final String STRING_MESSAGE = "foo";
    private static final double DOUBLE_MESSAGE = 1.1;
    private static final double THROTTLE_VALUE = 0.73;
    private static final double RUDDER_VALUE = 22.0;
    private static final String HELM_MESSAGE =
            "<" + String.valueOf(THROTTLE_VALUE) + "," + String.valueOf(RUDDER_VALUE) + ">";

    final MessageContent stringContent;
    final MessageContent doubleContent;
    final MessageContent helmContent;

    MessageContentTest() throws MessageTypeException {
        this.stringContent = new MessageContent(STRING_MESSAGE, null, null);
        this.doubleContent = new MessageContent(null, DOUBLE_MESSAGE, null);
        this.helmContent = new MessageContent(null, null, HELM_MESSAGE);
    }

    @Test
    void testBadConstruction() throws MessageTypeException {
        // An exception should be thrown if the number of non-null arguments is not exactly one

        Exception exception = assertThrows(MessageTypeException.class,
                () -> new MessageContent(STRING_MESSAGE, DOUBLE_MESSAGE, null));
        Exception exception2 =
                assertThrows(MessageTypeException.class, () -> new MessageContent(STRING_MESSAGE, null, HELM_MESSAGE));
        Exception exception3 = assertThrows(MessageTypeException.class,
                () -> new MessageContent(STRING_MESSAGE, DOUBLE_MESSAGE, HELM_MESSAGE));
        Exception exception4 =
                assertThrows(MessageTypeException.class, () -> new MessageContent(null, DOUBLE_MESSAGE, HELM_MESSAGE));
        Exception exception5 = assertThrows(MessageTypeException.class, () -> new MessageContent(null, null, null));
    }

    @Test
    void testGetStringMessage() throws MessageTypeException {
        // This should work
        assertEquals(STRING_MESSAGE, stringContent.getStringMessage());

        // Trying to get the others should throw exception
        Exception exception = assertThrows(MessageTypeException.class, () -> stringContent.getDoubleMessage());
        Exception exception2 = assertThrows(MessageTypeException.class, () -> stringContent.getHelmMessage());
    }

    @Test
    void testGetDoubleMessage() throws MessageTypeException {
        // This should work
        assertEquals(DOUBLE_MESSAGE, doubleContent.getDoubleMessage());

        // Trying to get the others should throw exception
        Exception exception = assertThrows(MessageTypeException.class, () -> doubleContent.getStringMessage());
        Exception exception2 = assertThrows(MessageTypeException.class, () -> doubleContent.getHelmMessage());
    }

    @Test
    void testGetHelmMessage() throws MessageTypeException {
        assertEquals(HELM_MESSAGE, helmContent.getHelmMessage());
        assertEquals(helmContent.getHelmThrottleValue(), THROTTLE_VALUE);
        assertEquals(helmContent.getHelmRudderValue(), RUDDER_VALUE);

        // Trying to get the others should throw exception
        Exception exception = assertThrows(MessageTypeException.class, () -> helmContent.getDoubleMessage());
        Exception exception2 = assertThrows(MessageTypeException.class, () -> helmContent.getStringMessage());
    }

    @Test
    void testGetContentType() {
        assertEquals(stringContent.getContentType(), MessageInterface.MessageType.STRING);
        assertEquals(doubleContent.getContentType(), MessageInterface.MessageType.DOUBLE);
        assertEquals(helmContent.getContentType(), MessageInterface.MessageType.HELM);
    }
}
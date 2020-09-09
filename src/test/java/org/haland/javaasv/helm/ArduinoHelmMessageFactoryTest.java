package org.haland.javaasv.helm;

import org.haland.javaasv.message.MessageTypeException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArduinoHelmMessageFactoryTest {
    private static final double THROTTLE_SETPOINT = 0.75;
    private static final double RUDDER_SETPOINT = 25;
    private static final String ORIGIN_ID = "testOriginClient";
    private static final String DESTINATION_ID = "testDestinationClient";

    private static final String EXPECTED_CONTENTS = "<0.75,25.0>";
    private static final String NEGATIVE_EXPECTED_CONTENTS = "<-0.75,-25.0>";

    private ArduinoHelmMessageFactory testFactory = new ArduinoHelmMessageFactory(ORIGIN_ID, DESTINATION_ID);

    @Test
    void testCreateMessage() throws MessageTypeException {
        assertEquals(EXPECTED_CONTENTS,
                testFactory.createMessage(THROTTLE_SETPOINT, RUDDER_SETPOINT).getMessageContents().getHelmMessage());
    }

    @Test
    void testCreateMessageNegative() throws MessageTypeException {
        assertEquals(NEGATIVE_EXPECTED_CONTENTS,
                testFactory.createMessage(-1 * THROTTLE_SETPOINT, -1 * RUDDER_SETPOINT).getMessageContents()
                        .getHelmMessage());
    }
}
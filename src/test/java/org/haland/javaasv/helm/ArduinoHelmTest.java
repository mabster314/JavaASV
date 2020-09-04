package org.haland.javaasv.helm;

import org.haland.javaasv.message.MessageInterface;
import org.haland.javaasv.message.MessengerServer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith({MockitoExtension.class})
class ArduinoHelmTest {
    private static final String MESSAGE_CONTENTS = "0.734,33";
    private static final String HELM_STATE = "0.88,25";
    private static final String PILOT_ID = "pilotTestClient";
    private static final String HELM_ID = "helmTestClient";

    private final HelmMessage testMessage = new HelmMessage(PILOT_ID, HELM_ID,
            System.currentTimeMillis(), MessageInterface.MessagePriority.NORMAL, MESSAGE_CONTENTS);

    @Mock
    private MessengerServer mockServer;

    @Mock
    private HelmArduino mockHelmArduino;

    // Class to test
    private ArduinoHelm arduinoHelm;

    /**
     * Tests that the method {@link ArduinoHelm#dispatch(HelmMessage)} sends a correct response message to the server
     */
    @Test
    public void returnDispatchTest() throws IOException {
        byte[] messageBytes = testMessage.getMessageContents().getBytes(StandardCharsets.US_ASCII);

        when(mockHelmArduino.getHelmState()).thenReturn(HELM_STATE);
        when(mockHelmArduino.sendSerialData(messageBytes)).thenReturn(messageBytes.length);

        arduinoHelm = new ArduinoHelm(mockServer, mockHelmArduino);

        arduinoHelm.dispatch(testMessage);

        ArgumentCaptor<HelmMessage> captor = ArgumentCaptor.forClass(HelmMessage.class);
        verify(mockServer).dispatch(captor.capture());

        HelmMessage returnedMessage = captor.getValue();

        // Destination of returned message must be pilot module
        assertEquals(returnedMessage.getDestinationID(), PILOT_ID);

        // Contents of returned message must match helm state
        assertEquals(returnedMessage.getMessageContents(), HELM_STATE);
    }
}
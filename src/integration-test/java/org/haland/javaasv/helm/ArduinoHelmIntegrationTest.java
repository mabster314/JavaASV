package org.haland.javaasv.helm;

import org.haland.javaasv.message.*;
import org.haland.javaasv.util.SerialArduino;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.awaitility.Awaitility.*;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.openMocks;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class ArduinoHelmIntegrationTest {
    private static final String CLIENT_ID = "testArduinoHelm";
    private static final String SERIAL_PORT = "/dev/ttyACM1";
    private static final String MATCH_PATTERN = "<\\d*\\.\\d*,\\d*\\.\\d*>";

    private boolean receivedValidResponse;

    private ArduinoHelm arduinoHelm;
    private HelmArduino helmArduino;

    private MessengerServerInterface server;

    @BeforeAll
    public void setupTests() {
        server = mock(MessengerServerInterface.class);
        helmArduino = new HelmArduino(new SerialArduino(SERIAL_PORT));
        arduinoHelm = new ArduinoHelm(server, helmArduino, CLIENT_ID);
        arduinoHelm.openPort();
    }

    @Test
    public void testArduinoHelm() throws MessageTypeException {
        // Message to send
        HelmMessage testMessage = new HelmMessage("origin", CLIENT_ID, System.currentTimeMillis(),
                MessageInterface.MessagePriority.NORMAL, 0.72, 12.0);

        doAnswer(new Answer<Void>() {
            @Override
            public Void answer(InvocationOnMock invocation) throws Throwable {
                MessageInterface message = (MessageInterface) invocation.getArguments()[0];
                receivedValidResponse = (message.getMessageContents().getHelmMessage().matches(MATCH_PATTERN)
                                            && message.getDestinationID() == "origin");
                return null;
            }
        }).when(server).dispatch(any(MessageInterface.class));

        // Send the message
        arduinoHelm.dispatch(testMessage);

        await().until(() -> receivedValidResponse);
    }

    @AfterAll
    public void teardownTests() {
        arduinoHelm.closePort();
    }
}

package org.haland.javaasv.pilot;

import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.*;
import org.haland.javaasv.util.PIDController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;
import static org.mockito.MockitoAnnotations.openMocks;

@ExtendWith(MockitoExtension.class)
class SimplePilotTest {
    private static final String HELM_ID = "testHelm";
    private static final String PILOT_ID = "testSimplePilot";
    private static final double THROTTLE_VALUE = 0.75;
    private static final double RUDDER_VALUE = 15.5;

    private SimplePilot testPilot;

    @Spy
    private PIDController mockPIDController = new PIDController(0, 0, 0);
    @Spy
    private MessengerServerInterface mockServer;
    @Mock
    private HelmInterface mockHelm;
    @Mock
    private GPSProviderInterface mockGPSProvider;

    @BeforeEach
    void setupPilot() {
        // Mock helm should give a client ID
        when(mockHelm.getClientID()).thenReturn(HELM_ID);
        // Mock PID controller returns same value
        when(mockPIDController.calculateNextOutput(anyDouble())).thenAnswer(new Answer<Double>() {
            @Override
            public Double answer(InvocationOnMock invocation) throws Throwable {
                return (Double) invocation.getArguments()[0];
            }
        });

        testPilot = new SimplePilot(PILOT_ID, mockServer, mockHelm, mockPIDController, mockPIDController,
                mockGPSProvider);
    }

    @AfterEach
    void teardownTest() {
        testPilot = null;
    }

    @Test
    void testRun() throws MessageTypeException {
        // Dispatch a message to set up initial values
        try {
            testPilot.dispatch(new HelmMessage(mockHelm.getClientID(), testPilot.getClientID(),
                    System.currentTimeMillis(), MessageInterface.MessagePriority.NORMAL, THROTTLE_VALUE, RUDDER_VALUE));
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }

        // should dispatch return message
        testPilot.run();

        // Make sure server receives message
        ArgumentCaptor<HelmMessage> captor = ArgumentCaptor.forClass(HelmMessage.class);
        verify(mockServer).dispatch(captor.capture());

        // Check message
        assertEquals(captor.getValue().getMessageContents().getHelmThrottleValue(), THROTTLE_VALUE);
        assertEquals(captor.getValue().getMessageContents().getHelmRudderValue(), RUDDER_VALUE);
    }

    @Test
    void testDispatch() {
        try {
            testPilot.dispatch(new HelmMessage(mockHelm.getClientID(), testPilot.getClientID(),
                    System.currentTimeMillis(), MessageInterface.MessagePriority.NORMAL, THROTTLE_VALUE, RUDDER_VALUE));
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }

        assertEquals(testPilot.getThrottleSetpoint(), THROTTLE_VALUE);
        assertEquals(testPilot.getRudderSetpoint(), RUDDER_VALUE);
    }
}
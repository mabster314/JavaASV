package org.haland.javaasv.pilot;

import net.sf.marineapi.nmea.util.Time;
import org.haland.javaasv.TestBase;
import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.HelmMessage;
import org.haland.javaasv.message.MessageTypeException;
import org.haland.javaasv.message.MessengerServerInterface;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.route.WaypointFactory;
import org.haland.javaasv.route.WaypointInterface;
import org.haland.javaasv.util.PIDController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.Answer;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimplePilotTest extends TestBase {
    private static final String HELM_ID = "testHelm";
    private static final String PILOT_ID = "testSimplePilot";
    private static final double THROTTLE_VALUE = 0.75;
    private static final double RUDDER_VALUE = 15.5;
    private static final double[] GPS_POSITION = {40.884, -73.644};

    private SimplePilot testPilot;

    @Spy
    private PIDController mockPIDController = new PIDController(0, 0, 0);
    @Spy
    private MessengerServerInterface mockServer;
    @Mock
    private HelmInterface mockHelm;
    private GPSProviderInterface mockGPSProvider;
    private MockRoute mockRoute = new MockRoute();

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

        mockGPSProvider = new GPSProviderInterface() {
            @Override
            public boolean getFixStatus() {
                return true;
            }

            @Override
            public Time getUpdateTime() {
                return new Time();
            }

            @Override
            public double getLatitude() {
                return GPS_POSITION[0];
            }

            @Override
            public double getLongitude() {
                return GPS_POSITION[1];
            }

            @Override
            public double[] getCoordinates() {
                return GPS_POSITION;
            }

            @Override
            public double getHeading() {
                return 0;
            }
        };

        testPilot =
                new SimplePilot(PILOT_ID, mockServer, mockHelm, mockPIDController, mockPIDController, mockGPSProvider);
        testPilot.setCurrentRoute(mockRoute);
    }

    @AfterEach
    void teardownTest() {
        testPilot = null;
    }

    @Test
    void testRun() throws MessageTypeException {
        // Set up the test
        setupPilot();

        // should dispatch return message
        testPilot.run();

        // Make sure server receives message
        ArgumentCaptor<HelmMessage> captor = ArgumentCaptor.forClass(HelmMessage.class);
        verify(mockServer).dispatch(captor.capture());
    }

    protected class MockRoute implements RouteInterface {
        WaypointFactory factory = new WaypointFactory();

        WaypointInterface previousWaypoint = factory.createWaypoint(45, -93, 0);
        WaypointInterface nextWaypoint = factory.createWaypoint(46.7, -92, 0);

        @Override
        public WaypointInterface getPreviousWaypoint() {
            return previousWaypoint;
        }

        @Override
        public WaypointInterface getNextWaypoint() {
            return nextWaypoint;
        }

        @Override
        public boolean isComplete() {
            return false;
        }
    }
}
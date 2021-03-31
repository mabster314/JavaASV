package org.haland.javaasv.pilot;

import net.sf.marineapi.nmea.util.Time;
import org.haland.javaasv.TestBase;
import org.haland.javaasv.config.AllConfig;
import org.haland.javaasv.config.BaseConfig;
import org.haland.javaasv.config.RouteConfig;
import org.haland.javaasv.controller.Controller;
import org.haland.javaasv.controller.TrivialController;
import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.HelmMessage;
import org.haland.javaasv.message.MessageTypeException;
import org.haland.javaasv.message.MessengerServerInterface;
import org.haland.javaasv.route.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.tinylog.Logger;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SimplePilotTest extends TestBase {
    private static final String HELM_ID = "testHelm";
    private static final String PILOT_ID = "testSimplePilot";
    private static final double THROTTLE_VALUE = 0.75;
    private static final double RUDDER_VALUE = 15.5;
    private static final double[] GPS_POSITION = {44.9187, -92.8435};

    private SimplePilot testPilot;

    @Spy
    private Controller controller = new TrivialController(0);
    @Spy
    private MessengerServerInterface mockServer;
    @Mock
    private HelmInterface mockHelm;
    private GPSProviderInterface mockGPSProvider;
    private SegmentedRoute route;

    @BeforeEach
    void setupPilot() {
        // Mock helm should give a client ID
        when(mockHelm.getClientID()).thenReturn(HELM_ID);

        BaseConfig.setPropertyFileDir(PROPERTY_FILE_DIR_SRC_TESTS);
        RouteConfig routeConfig = new RouteConfig();
        routeConfig.setRouteType(RouteType.FILE);
        AllConfig allConfig = new AllConfig(routeConfig);
        RouteParser parser = new RouteParser(allConfig);
        route = (SegmentedRoute) parser.getRoute();

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
                return route.getNextWaypoint().getLatitude();
            }

            @Override
            public double getLongitude() {
                return route.getNextWaypoint().getLongitude();
            }

            @Override
            public double[] getCoordinates() {
                return route.getNextWaypoint().getCoordinates();
            }

            @Override
            public double getHeading() {
                return 0;
            }
        };

        testPilot =
                new SimplePilot(PILOT_ID, mockServer, mockHelm, controller, controller, mockGPSProvider);
        testPilot.setCurrentRoute(new SegmentedRoute((SegmentedRoute) route));
    }

    @AfterEach
    void teardownTest() {
        testPilot = null;
    }

    @Test
    void testRun() throws MessageTypeException {
        // should dispatch return message
        Logger.debug("Starting pilot for test");
        testPilot.startPilot(10);

        // Make sure server receives message
        ArgumentCaptor<HelmMessage> captor = ArgumentCaptor.forClass(HelmMessage.class);
        verify(mockServer, timeout(1000).atLeastOnce()).dispatch(captor.capture());
        testPilot.stopPilot();
    }

    @Test
    void testAdvanceWaypoint() throws RouteEndException {
        // Make sure we start at the same point
        assertTrue(WaypointInterface.equals(testPilot.getCurrentRoute().getPreviousWaypoint(),
                route.getPreviousWaypoint()));

        // Advance the route to completion
        while (!testPilot.checkWaypointAdvance()) {
            route.advanceWaypoint();
            continue;
        }

        // Make sure we end on the final waypoint
        assertTrue(WaypointInterface.equals(testPilot.getCurrentRoute().getNextWaypoint(),
                route.getWaypoint(route.getRouteLength() - 1)));


    }
}
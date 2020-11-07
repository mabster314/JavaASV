package org.haland.javaasv.util;

import org.haland.javaasv.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PilotUtilTest extends TestBase {
    private static final double[] JFK_LOCATION = {40.63, -73.78};
    private static final double[] LAX_LOCATION = {33.95, -118.40};
    private static final double[] CURRENT_LOCATION = {34.5, -116.5};
    private static final double EXPECTED_DISTANCE = 2145.340;
    private static final double EXPECTED_INITIAL_BEARING = 65.8975;
    private static final double EXPECTED_CROSS_TRACK_ERROR = 7.448;
    private static final double TOLERANCE = 0.001;

    @Test
    void testCalculateInitialBearing() {
        assertEquals(EXPECTED_INITIAL_BEARING, PilotUtil.calculateInitialBearing(LAX_LOCATION, JFK_LOCATION),
                TOLERANCE);
    }

    @Test
    void testCalculateDistance() {
        assertEquals(EXPECTED_DISTANCE,
                PilotUtil.calculateDistance(LAX_LOCATION, JFK_LOCATION, EarthRadius.NMI), TOLERANCE);
    }

    @Test
    void testCalculateCrossTrackDistance() {
        assertEquals(EXPECTED_CROSS_TRACK_ERROR, PilotUtil.calculateCrossTrackDistance(LAX_LOCATION, JFK_LOCATION,
                CURRENT_LOCATION, EarthRadius.NMI), TOLERANCE);
    }

    @Test
    void testHav() {
    }

    @Test
    void testArchav() {
    }
}
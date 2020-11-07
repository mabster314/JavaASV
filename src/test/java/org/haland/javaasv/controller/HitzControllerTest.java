package org.haland.javaasv.controller;

import org.haland.javaasv.TestBase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HitzControllerTest extends TestBase {
    private static final double HEADING_ERROR = Math.toRadians(45);
    private static final double XTD = -15;
    private static final double THROTTLE = 1;

    private static final double K_ph = 3;
    private static final double K_px = 5;
    private static final double K_ix = -0.5;
    private static final double PERIOD = 1;

    private static final double ACCUMULATED_ERROR = -100;

    private static final double EXPECTED_RESULT = 121.648;
    private static final double TOLERANCE = 0.01;

    private HitzController sut;

    @Test
    void testHitzControllerCalculateNextOutput() {
        sut = new HitzController(K_ph, K_px, K_ix);
        sut.setTotalError(ACCUMULATED_ERROR);
        sut.setPeriod(PERIOD);
        assertEquals(EXPECTED_RESULT, sut.calculateNextOutput(XTD, HEADING_ERROR, THROTTLE), TOLERANCE);
    }
}
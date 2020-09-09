package org.haland.javaasv.helm;

import org.haland.javaasv.util.SerialArduino;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HelmArduinoTest {
    private static final String HELM_STATE = "<0.88,25.0>";

    private HelmArduino helmArduino;

    private SerialArduino fakeSerialArduino = new SerialArduino() {
        @Override
        public byte[] getLastMessage() {
            return HELM_STATE.getBytes();
        }
    };

    @Test
    public void testGetHelmState() throws IOException {
        helmArduino = new HelmArduino(fakeSerialArduino);

        assertEquals(HELM_STATE, helmArduino.getHelmState());
    }
}
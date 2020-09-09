package org.haland.javaasv.helm;

import org.haland.javaasv.util.SerialArduino;
import org.haland.javaasv.util.SerialArduinoInterface;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoSettings;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

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
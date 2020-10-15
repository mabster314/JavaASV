package org.haland.javaasv.helm;

import org.haland.javaasv.util.SerialArduino;
import org.haland.javaasv.util.SerialArduinoInterface;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * Provides a specialized {@link SerialArduino} for use with an {@link ArduinoHelm}
 */
public class HelmArduino implements SerialArduinoInterface<String> {
    private final SerialArduinoInterface arduino;

    /**
     * The serial arduino is provided to the class and wrapped with the helm class. This method should not be called
     * directly outside of tests.
     *
     * @param arduino a {@link SerialArduinoInterface}.
     */
    public HelmArduino(SerialArduinoInterface<byte[]> arduino) {
        this.arduino = arduino;
    }

    /**
     * Constructs a new {@link HelmArduino} with a new {@link SerialArduino}
     */
    public HelmArduino() {
        this(new SerialArduino());
    }

    /**
     * Constructs a new {@link HelmArduino} with a new {@link SerialArduino} on a specified port
     *
     * @param port Serial port to connect over
     */
    public HelmArduino(String port) {
        this(new SerialArduino(port));
    }

    @Override
    public boolean openPort() {
        return arduino.openPort();
    }

    @Override
    public boolean closePort() {
        return arduino.closePort();
    }

    @Override
    public int sendSerialData(byte[] serialData) {
        return arduino.sendSerialData(serialData);
    }
}

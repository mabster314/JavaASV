package org.haland.javaasv.util;

public interface SerialArduinoInterface {
    boolean openPort();

    boolean closePort();

    int sendSerialData(byte[] serialData);

    byte[] getLastMessage();
}

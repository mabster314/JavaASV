package org.haland.javaasv.util;

import java.io.IOException;

public interface SerialDeviceInterface<T> {
    boolean openPort();

    boolean closePort();

    boolean isMessageAvailable();

    int sendSerialData(byte[] serialData);

    T getLastMessage() throws IOException;
}

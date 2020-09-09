package org.haland.javaasv.util;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface SerialArduinoInterface<T> extends Callable<T> {
    boolean openPort();

    boolean closePort();

    boolean isMessageAvailable();

    int sendSerialData(byte[] serialData);

    byte[] getLastMessage() throws IOException;
}

package org.haland.javaasv.util;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface SerialArduinoInterface<T> {
    boolean openPort();

    boolean closePort();

    int sendSerialData(byte[] serialData);
}

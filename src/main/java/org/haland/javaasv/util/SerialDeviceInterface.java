package org.haland.javaasv.util;

import jdk.nashorn.internal.codegen.CompilerConstants;

import java.io.IOException;
import java.util.concurrent.Callable;

public interface SerialDeviceInterface<T>{
    boolean openPort();

    boolean closePort();

    boolean isMessageAvailable();

    int sendSerialData(byte[] serialData);

    T getLastMessage() throws IOException;
}

package org.haland.javaasv.util;

import java.io.IOException;

/**
 * Defines the requirements for a serial device of a generic message type
 * @param <T> The message type being transmitted
 */
public interface SerialDeviceInterface<T> {
    /**
     * Opens the serial port
     * @return <code>true</code> if the port was opened successfully, <code>false</code> otherwise
     */
    boolean openPort();

    /**
     * Closes the serial port
     * @return <code>true</code> if the port was closed successfully, <code>false</code> otherwise
     */
    boolean closePort();

    /**
     * Indicates whether a serial message is available. This method must return True if there is currently new serial
     * data available and False otherwise.
     * @return True if a new message is available, False otherwise
     */
    boolean isMessageAvailable();

    /**
     * Sends an array of bytes over the serial connection
     *
     * @param serialData <code>byte[]</code> array containing data to send
     * @return the number of bytes actually sent (-1 if no bytes sent)
     */
    int sendSerialData(byte[] serialData);

    /**
     * Provides the last message received by the serial device
     * @return The last message received
     * @throws IOException if there is no exception available
     */
    T getLastMessage() throws IOException;
}

/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2020  Max Haland
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package org.haland.javaasv.util;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortEvent;
import com.fazecast.jSerialComm.SerialPortMessageListener;

import java.nio.charset.StandardCharsets;

/**
 * Serial interface for the arduino
 */
public class SerialArduino implements SerialDeviceInterface<byte[]> {
    /**
     * The default port name for an Arduino on Debian
     */
    private static final String DEFAULT_PORT_NAME = "/dev/ttyACM0"; // Default device name on Debian

    // Default arduino serial com settings
    private static final int DEFAULT_BAUD_RATE = 115200;
    private static final int DEFAULT_DATA_BITS = 8;
    private static final int DEFAULT_STOP_BITS = 1;
    private static final int DEFAULT_PARITY = 0;

    private String portName;
    private SerialPort serialPort;

    private ArduinoMessageListener messageListener;
    private byte[] lastMessage;
    private boolean messageAvailable;

    /**
     * Connect a new arduino with specified port name
     *
     * @param portName the serial port name
     */
    public SerialArduino(String portName) {
        this.portName = portName;
        this.serialPort = SerialPort.getCommPort(this.portName);
        this.serialPort.setComPortParameters(DEFAULT_BAUD_RATE, DEFAULT_DATA_BITS, DEFAULT_STOP_BITS, DEFAULT_PARITY);
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_WRITE_BLOCKING, 0, 0);
    }

    /**
     * Connect a new arduino to the default serial port
     */
    public SerialArduino() {
        this(DEFAULT_PORT_NAME);
    }

    /**
     * Attempts to open the serial port
     *
     * @return <code>true</code> if the port was opened successfully, <code>false</code> otherwise
     */
    @Override
    public synchronized boolean openPort() {
        boolean opened = serialPort.openPort();
        return opened;
    }

    /**
     * Attempts to close the serial port
     *
     * @return <code>true</code> if the port was closed successfully, <code>false</code> otherwise
     */
    @Override
    public synchronized boolean closePort() {
        return serialPort.closePort();
    }

    /**
     * Sends an array of bytes over the serial connection
     *
     * @param serialData <code>byte[]</code> array containing data to send
     * @return the number of bytes actually sent (-1 if no bytes sent)
     */
    @Override
    public int sendSerialData(byte[] serialData) {
        return serialPort.writeBytes(serialData, serialData.length);
    }

    private synchronized void setLastMessage(byte[] lastMessage) {
        this.lastMessage = lastMessage;
    }

    @Override
    public synchronized byte[] getLastMessage() {
        byte[] message = lastMessage;
        lastMessage = null;
        return message;
    }

    @Override
    public synchronized boolean isMessageAvailable() {
        return this.messageAvailable;
    }

    private void setMessageAvailable(boolean messageAvailability) {
        this.messageAvailable = messageAvailability;
    }

    /**
     * A class providing a message listener for the serial arduino
     */
    private final class ArduinoMessageListener implements SerialPortMessageListener {
        private final SerialArduino serialArduino;

        public ArduinoMessageListener(SerialArduino serialArduino) {
            this.serialArduino = serialArduino;
        }

        @Override
        public byte[] getMessageDelimiter() {
            return SerialUtil.END_MESSAGE_CHAR.getBytes(StandardCharsets.US_ASCII);
        }

        @Override
        public boolean delimiterIndicatesEndOfMessage() {
            return true;
        }

        @Override
        public int getListeningEvents() {
            return SerialPort.LISTENING_EVENT_DATA_RECEIVED;
        }

        @Override
        public void serialEvent(SerialPortEvent event) {
            byte[] delimitedMessage = event.getReceivedData();
            serialArduino.setLastMessage(delimitedMessage);
            serialArduino.setMessageAvailable(true);
        }
    }
}

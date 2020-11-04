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

package org.haland.javaasv.pilot;

import com.fazecast.jSerialComm.SerialPort;
import org.haland.javaasv.util.SerialDeviceInterface;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

/**
 * A GPSProvider that uses the Raspberry Pi GPS hat to provide GPS data
 */
public class GPSHat implements SerialDeviceInterface<String> {
    /**
     * Raspberry Pi UART serial port
     */
    public static final String DEFAULT_PORT = "/dev/ttyS0";

    private static final int DEFAULT_BAUD_RATE = 115200;
    private static final int DEFAULT_DATA_BITS = 8;
    private static final int DEFAULT_STOP_BITS = 1;
    private static final int DEFAULT_PARITY = 0;

    /**
     * Serial string to start the GPS module
     */
    public static final String START_GPS_MESSAGE = "AT+CGNSPWR=1\n";

    /**
     * Serial string to stop the GPS module
     */
    public static final String STOP_GPS_MESSAGE = "AT+CGNSPWR=0\n";

    /**
     * Serial string to start GPS data output in RMC NMEA format
     */
    public static final String START_GPS_DATA_MESSAGE = "AT+CGPSOUT=32\n";

    /**
     * Serial string to start GPS data output
     */
    public static final String STOP_GPS_DATA_MESSAGE = "AT+CGPSOUT=0\n";

    private SerialPort serialPort;
    private String portName;
    private InputStream in;
    private InputStreamReader inReader;
    private BufferedReader bufferedReader;

    public GPSHat(String portName) {
        this.portName = portName;
        this.serialPort = SerialPort.getCommPort(this.portName);
        this.serialPort.setComPortParameters(DEFAULT_BAUD_RATE, DEFAULT_DATA_BITS, DEFAULT_STOP_BITS, DEFAULT_PARITY);
        this.serialPort.setComPortTimeouts(SerialPort.TIMEOUT_READ_SEMI_BLOCKING, 0, 0);
        this.in = serialPort.getInputStream();
        this.inReader = new InputStreamReader(in, StandardCharsets.US_ASCII);
        this.bufferedReader = new BufferedReader(inReader);
    }

    public GPSHat() {
        this(DEFAULT_PORT);
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
     * Turns the GPS module on or off
     *
     * @param powerOn true for power on, false for power off
     */
    public void setGPSPower(boolean powerOn) {
        if (powerOn) {
            sendSerialData(START_GPS_MESSAGE.getBytes());
        } else {
            sendSerialData(STOP_GPS_MESSAGE.getBytes());
        }
    }

    /**
     * Starts and stops GPS reporting
     *
     * @param reportingOn true to start reporting, false to stop reporting
     */
    public void setGPSDataReporting(boolean reportingOn) {
        if (reportingOn) {
            sendSerialData(START_GPS_DATA_MESSAGE.getBytes());
        } else {
            sendSerialData(STOP_GPS_DATA_MESSAGE.getBytes());
        }
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

    public InputStream getInputStream() {
        return in;
    }

    @Override
    public synchronized String getLastMessage() {
        try {
            return bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            return "OOPS";
        }
    }

    @Override
    public synchronized boolean isMessageAvailable() {
        try {
            return bufferedReader.ready();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}

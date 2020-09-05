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

package org.haland.javaasv;

import org.haland.javaasv.util.SerialArduino;
import org.haland.javaasv.util.SerialUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnsupportedCharsetException;

/**
 * Our entry point
 */
public class ASV {
    public static void main(String[] args) {
        final String port = "/dev/ttyACM1";
        final String message = "<Message>";

        SerialArduino arduino = new SerialArduino(port);

        arduino.openPort();

        arduino.sendSerialData(message.getBytes(StandardCharsets.US_ASCII));
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            System.out.println(new String(arduino.getLastMessage(), "US-ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        arduino.sendSerialData("<Another>".getBytes(StandardCharsets.US_ASCII));
        try {
            Thread.sleep(10);
        } catch (Exception e) {
            e.printStackTrace();
        }

        arduino.closePort();

        try {
            System.out.println(new String(arduino.getLastMessage(), "US-ASCII"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}

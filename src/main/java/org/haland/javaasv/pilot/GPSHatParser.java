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

/**
 * A class to parse incoming data from the serial GPS hat
 */
public class GPSHatParser implements GPSProviderInterface{
    private GPSHat gpsHat;

    public GPSHatParser(GPSHat gpsHat) {
        this.gpsHat = gpsHat;
    }

    public GPSHatParser(String portName) {
        this.gpsHat = new GPSHat(portName);
    }

    public GPSHatParser() {
        this.gpsHat = new GPSHat();
    }

    private void startGPS() throws InterruptedException {
        gpsHat.setGPSPower(true);
        gpsHat.setGPSDataReporting(true);
    }

    @Override
    public boolean getFixStatus() {
        return false;
    }

    @Override
    public double getUpdateTime() {
        return 0;
    }

    @Override
    public double getLatitude() {
        return 0;
    }

    @Override
    public double getLongitude() {
        return 0;
    }

    @Override
    public double getHeading() {
        return 0;
    }

    @Override
    public void run() {

    }
}

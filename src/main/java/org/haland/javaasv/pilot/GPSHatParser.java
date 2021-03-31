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

import net.sf.marineapi.nmea.event.SentenceEvent;
import net.sf.marineapi.nmea.event.SentenceListener;
import net.sf.marineapi.nmea.io.SentenceReader;
import net.sf.marineapi.nmea.sentence.RMCSentence;
import net.sf.marineapi.nmea.sentence.Sentence;
import net.sf.marineapi.nmea.util.DataStatus;
import net.sf.marineapi.nmea.util.Position;
import net.sf.marineapi.nmea.util.Time;
import org.haland.javaasv.config.AllConfig;
import org.tinylog.Logger;

/**
 * A class to parse incoming data from the serial GPS hat. Implementation of {@link GPSProviderInterface}
 */
public class GPSHatParser implements GPSProviderInterface {
    private GPSHat gpsHat;
    private SentenceReader reader;


    private volatile Position position;
    private volatile DataStatus status;
    private volatile Time updateTime;
    private volatile double heading;

    /**
     * Configures and starts the GPS parser
     * @param gpsHat the {@link GPSHat} instance to use
     */
    public GPSHatParser(GPSHat gpsHat) {
        Logger.info("Attempting to start GPSHatParser");
        this.gpsHat = gpsHat;
        this.gpsHat.openPort();
        this.reader = new SentenceReader(gpsHat.getInputStream());
        reader.addSentenceListener(new RMCListener());
        reader.start();
    }

    /**
     * Configures and starts the GPS parser with a new {@link GPSHat} on the specified port name
     * @param portName The port to start the {@link GPSHat} on
     */
    public GPSHatParser(String portName) {
        this(new GPSHat(portName));
    }

    /**
     * Configures and starts the GPS parser with a new {@link GPSHat} with a port name specified in the provided
     * {@link AllConfig} instance
     * @param config The instance of {@link AllConfig} to use
     */
    public GPSHatParser(AllConfig config) {
        this(new GPSHat(config.getSerialConfig().getGpsPort()));
    }

    /**
     * Configures and starts the GPS parser with a new {@link GPSHat} on the default port
     */
    public GPSHatParser() {
        this(new GPSHat());
    }

    /**
     * Starts the GPS hat
     * @throws InterruptedException
     */
    public void startGPS() {
        gpsHat.setGPSPower(true);
        gpsHat.setGPSDataReporting(true);
    }

    @Override
    public boolean getFixStatus() {
        return (status == DataStatus.ACTIVE);
    }

    @Override
    public Time getUpdateTime() {
        return this.updateTime;
    }

    @Override
    public double getLatitude() {
        return this.position.getLatitude();
    }

    @Override
    public double getLongitude() {
        return this.position.getLongitude();
    }

    @Override
    public double[] getCoordinates() {
        return new double[]{getLatitude(), getLongitude()};
    }

    @Override
    public double getHeading() {
        return this.heading;
    }

    private void setPosition(Position position) {
        this.position = position;
    }

    private void setStatus(DataStatus status) {
        this.status = status;
    }

    private void setHeading(double heading) {
        this.heading = heading;
    }

    /**
     * Provides a {@link SentenceListener} that handles the serial event data
     */
    private class RMCListener implements SentenceListener {

        @Override
        public void readingPaused() {
            System.out.println("-- Paused --");
        }

        @Override
        public void readingStarted() {
            System.out.println("-- Started --");
        }

        @Override
        public void readingStopped() {
            System.out.println("-- Stopped --");
        }

        @Override
        public synchronized void sentenceRead(SentenceEvent event) {
            Sentence s = event.getSentence();

            if ("RMC".equals(s.getSentenceId())) {
                Logger.trace("Received sentence" + s);
                RMCSentence rmc = (RMCSentence) s;
                setPosition(rmc.getPosition());
                setStatus(rmc.getStatus());
                setHeading(rmc.getCourse());
            }
        }
    }
}

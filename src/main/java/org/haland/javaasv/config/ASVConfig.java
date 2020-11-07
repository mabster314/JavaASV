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

package org.haland.javaasv.config;

import java.util.Properties;

public class ASVConfig extends BaseConfig {
    private static final String PROPERTY_FILE_NAME = "asv.properties";

    private long serverPeriod;
    private long pilotPeriod;


    @Override
    protected String getPropertyFileName() {
        return PROPERTY_FILE_NAME;
    }

    @Override
    protected void configure(Properties properties) {
        this.serverPeriod = getLongPropertyValue("server.period", properties);
        this.pilotPeriod = getLongPropertyValue("pilot.period", properties);
    }

    public long getServerPeriod() {
        return serverPeriod;
    }

    public long getPilotPeriod() {
        return pilotPeriod;
    }
}

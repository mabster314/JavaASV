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

import org.haland.javaasv.route.RouteType;

import java.util.Properties;

/**
 * {@link BaseConfig} implementation to read route data from a properties file
 */
public class RouteConfig extends BaseConfig {
    private static String propertyFileName = "route.properties";

    private RouteType routeType;

    private String routeFileName;

    private double[] twoPointRouteStart;
    private double[] twoPointRouteEnd;
    private double twoPointRouteTolerance;

    @Override
    protected String getPropertyFileName() {
        return propertyFileName;
    }

    /**
     * Set the property file name at runtime for testing
     *
     * @param name
     */
    public static void setPropertyFileName(String name) {
        propertyFileName = name;
    }

    @Override
    protected void configure(Properties properties) {
        String routeTypeStr = getStringPropertyValue("route.type", properties);
        this.routeType = RouteType.valueOf(routeTypeStr);
        switch (routeType) {
            case FILE:
                this.routeFileName = getStringPropertyValue("route.filename", properties);
                break;

            case TWO_POINT:
                this.twoPointRouteStart = getDoubleArrayPropertyValue("route.two_point.start", properties);
                this.twoPointRouteEnd = getDoubleArrayPropertyValue("route.two_point.end", properties);
                this.twoPointRouteTolerance = getDoublePropertyValue("route.two_point.tolerance",
                        properties);
                break;
        }
    }

    public String getRouteFileName() {
        return routeFileName;
    }

    public RouteType getRouteType() {
        return routeType;
    }

    public double[] getTwoPointRouteStart() {
        return twoPointRouteStart;
    }

    public double[] getTwoPointRouteEnd() {
        return twoPointRouteEnd;
    }

    public double getTwoPointRouteTolerance() {
        return twoPointRouteTolerance;
    }
}

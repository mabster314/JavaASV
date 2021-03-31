/*
 * This file is part of JavaASV, an open-source ASV navigation controller.
 * Copyright (C) 2021  Max Haland
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

import org.haland.javaasv.config.*;

/**
 * Class used to load and provide {@link BaseConfig} implementations
 */
public class AllConfig {
    private final ASVConfig asvConfig;
    private final ControllerConfig controllerConfig;
    private final SerialConfig serialConfig;
    private final RouteConfig routeConfig;


    public AllConfig() {
        this.asvConfig = new ASVConfig();
        this.controllerConfig = new ControllerConfig();
        this.serialConfig = new SerialConfig();
        this.routeConfig = new RouteConfig();
    }

    /**
     * Creates an AllConfig with the specified config instances. This should only be called for testing
     * @param asvConfig
     * @param controllerConfig
     * @param serialConfig
     * @param routeConfig
     */
    public AllConfig(ASVConfig asvConfig, ControllerConfig controllerConfig, SerialConfig serialConfig,
                     RouteConfig routeConfig) {
        this.asvConfig = asvConfig;
        this.controllerConfig = controllerConfig;
        this.serialConfig = serialConfig;
        this.routeConfig = routeConfig;
    }

    /**
     * Creates an AllConfig with the specified config instances. This should only be called for testing
     * @param routeConfig
     */
    public AllConfig(RouteConfig routeConfig) {
        this.asvConfig = new ASVConfig();
        this.controllerConfig = new ControllerConfig();
        this.serialConfig = new SerialConfig();
        this.routeConfig = routeConfig;
    }

    public ControllerConfig getControllerConfig() {
        return controllerConfig;
    }

    public SerialConfig getSerialConfig() {
        return serialConfig;
    }

    public RouteConfig getRouteConfig() {
        return routeConfig;
    }

    public ASVConfig getAsvConfig() {
        return asvConfig;
    }
}

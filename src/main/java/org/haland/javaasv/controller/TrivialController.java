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

package org.haland.javaasv.controller;

import org.haland.javaasv.config.AllConfig;

public class TrivialController implements Controller {
    private double output;

    public TrivialController(double output) {
        this.output = output;
    }

    public TrivialController(AllConfig config) {
        this.output = config.getControllerConfig().getThrottleValue();
    }

    @Override
    public void start() {
    }

    @Override
    public ControllerType getType() {
        return ControllerType.TRIVIAL;
    }

    @Override
    public double calculateNextOutput(double... processVariable) {
        return output;
    }
}

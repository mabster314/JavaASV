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

import java.util.Date;

public class MessageTest implements MessageInterface<Integer> {
    private Integer contents = 42;
    private String destination;

    public MessageTest(String destination) {
        this.destination = destination;
    }

    @Override
    public String getOriginID() {
        return null;
    }

    @Override
    public String getDestinationID() {
        return this.destination;
    }

    @Override
    public Date getCreationTime() {
        return null;
    }

    @Override
    public String getPriority() {
        return null;
    }

    @Override
    public Class<?> getType() {
        return this.contents.getClass();
    }

    @Override
    public Integer getMessageContents() {
        return this.contents;
    }
}

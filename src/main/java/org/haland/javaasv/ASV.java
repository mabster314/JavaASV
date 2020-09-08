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

import org.haland.javaasv.message.*;

import java.util.concurrent.*;

/**
 * Our entry point
 */
public class ASV {
    public static void main(String[] args) {
        final String port = "/dev/ttyACM1";
        final String sendingClientID = "sendingClient";
        final String receivingClientID = "receivingClient";

        MessengerServer server = MessengerServer.getInstance();
        SendingClient sendingClient = new SendingClient(server, sendingClientID, receivingClientID);
        MessengerClientInterface receivingClient = new MessengerClientInterface() {
            @Override
            public void dispatch(MessageInterface message) {
                try {
                    System.out.println(message.getMessageContents().getStringMessage());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if ((message.getCreationTime() % 10000) < 1000) {
                    try {
                        server.dispatch(new SimpleMessage(receivingClientID, sendingClientID, System.currentTimeMillis(),
                                MessageInterface.MessagePriority.NORMAL,
                                new MessageContent("Return", null, null)));
                    } catch (MessageTypeException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public String getClientID() {
                return receivingClientID;
            }

            @Override
            public MessageInterface.MessageType getClientType() {
                return MessageInterface.MessageType.STRING;
            }
        };

        try {
            server.registerClientModule(sendingClient.getClientID(), sendingClient);
            server.registerClientModule(receivingClient.getClientID(), receivingClient);
        } catch (Exception e) {
            e.printStackTrace();
        }

        Runnable runBoth = new Runnable() {
            @Override
            public void run() {
                sendingClient.run();
                server.run();
            }
        };

        ScheduledExecutorService clientExecutor = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService serverExecutor = Executors.newScheduledThreadPool(1);

        int initialDelay = 0;
        int clientPeriod = 1000;
        int serverPeriod = 100;
        clientExecutor.scheduleAtFixedRate(sendingClient, initialDelay, clientPeriod, TimeUnit.MILLISECONDS);
        serverExecutor.scheduleAtFixedRate(server, initialDelay, serverPeriod, TimeUnit.MILLISECONDS);
    }

}

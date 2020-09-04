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

package org.haland.javaasv.message;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessengerServerTest {
    // Test constants
    private static final String originID = "testID";
    private static final MessageInterface.MessagePriority priority = MessageInterface.MessagePriority.NORMAL;

    private static final String stringMessageContents = "foo";
    private static final Integer integerMessageContents = 5;

    private final MessageFactory testFactory;
    private final MessengerTestClient testClient1;
    private final MessengerTestClient testClient2;
    private final MessageInterface testMessage1;
    private final MessageInterface testMessage2;

    // The server is spawned for testing
    private MessengerServer testServer;

    // Register the clients to the server
    private MessengerServerTest() throws DuplicateKeyException {
        // Make our messages and clients
        this.testFactory = new MessageFactory(originID);

        this.testClient1 = new MessengerTestClient("testClient1", stringMessageContents);
        this.testClient2 = new MessengerTestClient("testClient2", integerMessageContents);

        this.testMessage1 = testFactory.<String>createMessage(testClient1.getClientID(),
                priority, stringMessageContents);
        this.testMessage2 = testFactory.<Integer>createMessage(testClient2.getClientID(),
                priority, integerMessageContents);

        // Get the server
        testServer = MessengerServer.getInstance();
    }

    @BeforeAll
    void registerTestClients() throws DuplicateKeyException {
        // Register the test clients
        testServer.registerClientModule(testClient1.getClientID(), testClient1);
        testServer.registerClientModule(testClient2.getClientID(), testClient2);
    }

    @Test
    void testRegisterDuplicateClientModule() {
        // Adding a duplicate should throw an exception
        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> testServer.registerClientModule(testClient1.getClientID(), testClient1));
    }

    @Test
    void testSingletonInstance(){
        // We should get the same instance if we ask for the server
        assertEquals(testServer, MessengerServer.getInstance());
    }

    @Test
    void testDispatch() {
        testServer.dispatch(testMessage1);
        testServer.dispatch(testMessage2);
    }

    private final class MessengerTestClient<T> implements MessengerClientInterface{
        private final String clientID;
        private final T messageContents;

        MessengerTestClient(String clientID, T messageContents) {
            this.clientID = clientID;
            this.messageContents = messageContents;
        }

        // Make sure we got the right message
        @Override
        public void dispatch(MessageInterface message) {
            assertEquals(messageContents, message.getMessageContents());
        }

        @Override
        public String getClientID() {
            return this.clientID;
        }
    }
}
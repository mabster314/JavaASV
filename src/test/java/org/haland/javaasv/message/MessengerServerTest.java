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
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessengerServerTest {
    // Test constants
    private static final String TEST_CLIENT_ID = "testID";
    private static final String MESSAGE_CONTENTS = "foo";


    private MessengerClientInterface client = new MessengerClientInterface() {
        @Override
        public void dispatch(MessageInterface message) {

        }

        @Override
        public String getClientID() {
            return null;
        }
    };

    private MessageInterface message = new MessageInterface() {
        @Override
        public String getOriginID() {
            return null;
        }

        @Override
        public String getDestinationID() {
            return null;
        }

        @Override
        public long getCreationTime() {
            return 0;
        }

        @Override
        public MessagePriority getPriority() {
            return null;
        }

        @Override
        public Class<?> getType() {
            return null;
        }

        @Override
        public Object getMessageContents() {
            return null;
        }
    };

    // The server is spawned for testing
    private MessengerServer testServer = MessengerServer.getInstance();

    private MessengerClientInterface spyClient;

    @BeforeAll
    void setupTests() throws DuplicateKeyException {
        spyClient = Mockito.spy(client);
        doReturn(TEST_CLIENT_ID).when(spyClient).getClientID();

        testServer.registerClientModule(spyClient.getClientID(), spyClient);
    }

    @Test
    void testSingletonInstance(){
        // We should get the same instance if we ask for the server
        assertEquals(testServer, MessengerServer.getInstance());
    }

    @Test
    void testDispatch() {
        MessageInterface spyMessage = Mockito.spy(message);
        when(spyMessage.getDestinationID()).thenReturn(TEST_CLIENT_ID);
        when(spyMessage.getMessageContents()).thenReturn(MESSAGE_CONTENTS);

        testServer.dispatch(spyMessage);

        ArgumentCaptor<MessageInterface> captor = ArgumentCaptor.forClass(MessageInterface.class);
        verify(spyClient).dispatch(captor.capture());

        MessageInterface dispatchedMessage = captor.getValue();

        assertEquals(dispatchedMessage.getMessageContents(), MESSAGE_CONTENTS);
    }

    @Test
    void testDuplicateRegistrations() throws DuplicateKeyException {
        // Registering again should throw exception
        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> testServer.registerClientModule(spyClient.getClientID(), spyClient));
    }
}
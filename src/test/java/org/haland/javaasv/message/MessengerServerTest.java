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
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessengerServerTest {
    // Test constants
    private static final String TEST_CLIENT_ID_1 = "testID1";
    private static final String TEST_CLIENT_ID_2 = "testID2";
    private static final String STRING_MESSAGE_CONTENT = "foo";
    private static final double DOUBLE_MESSAGE_CONTENT = 1.1;


    private MessengerClientInterface client = new MessengerClientInterface() {
        @Override
        public void dispatch(MessageInterface message) {

        }

        @Override
        public String getClientID() {
            return TEST_CLIENT_ID_1;
        }

        /**
         * Returns the type of messages the client can handle
         *
         * @return
         */
        @Override
        public MessageInterface.MessageType getClientType() {
            return MessageInterface.MessageType.STRING;
        }
    };

    private MessageContent content1;
    @Mock
    private MessageInterface message1;


    private MessageContent content2;
    @Mock
    private MessageInterface message2;

    // The server is spawned for testing
    private MessengerServer testServer = MessengerServer.getInstance();

    private MessengerClientInterface spyClient1;
    private MessengerClientInterface spyClient2;

    @BeforeAll
    void setupTests() throws DuplicateKeyException, MessageTypeException {
        spyClient1 = Mockito.spy(client);
        doReturn(TEST_CLIENT_ID_1).when(spyClient1).getClientID();
        spyClient2 = Mockito.spy(client);
        doReturn(TEST_CLIENT_ID_2).when(spyClient2).getClientID();
        doReturn(MessageInterface.MessageType.DOUBLE).when(spyClient2).getClientType();

        testServer.registerClientModule(spyClient1.getClientID(), spyClient1);
        testServer.registerClientModule(spyClient2.getClientID(), spyClient2);

        content1 = new MessageContent(STRING_MESSAGE_CONTENT, null, null);
        message1 = new SimpleMessage(null, TEST_CLIENT_ID_1, System.currentTimeMillis(), null, content1);

        content2 = new MessageContent(null, DOUBLE_MESSAGE_CONTENT, null);
        message2 = new SimpleMessage(null, TEST_CLIENT_ID_2, System.currentTimeMillis(), null, content2);
    }

    @Test
    void testSingletonInstance(){
        // We should get the same instance if we ask for the server
        assertEquals(testServer, MessengerServer.getInstance());
    }

    @Test
    void testDispatch() throws MessageTypeException {
        MessageInterface spyMessage1 = message1;
        MessageInterface spyMessage2 = message2;

        // Dispatch to the stack
        testServer.dispatch(spyMessage1);
        testServer.dispatch(spyMessage2);
        
        // Distribute
        testServer.run();

        ArgumentCaptor<MessageInterface> captor1 = ArgumentCaptor.forClass(MessageInterface.class);
        verify(spyClient1).dispatch(captor1.capture());
        MessageInterface dispatchedMessage1 = captor1.getValue();
        assertEquals(dispatchedMessage1.getMessageContents().getStringMessage(), STRING_MESSAGE_CONTENT);
        
        ArgumentCaptor<MessageInterface> captor2 = ArgumentCaptor.forClass(MessageInterface.class);
        verify(spyClient2).dispatch(captor2.capture());
        MessageInterface dispatchedMessage2 = captor2.getValue();
        assertEquals(dispatchedMessage2.getMessageContents().getDoubleMessage(), DOUBLE_MESSAGE_CONTENT);
    }

    @Test
    void testDuplicateRegistrations() throws DuplicateKeyException {
        // Registering again should throw exception
        Exception exception = assertThrows(DuplicateKeyException.class,
                () -> testServer.registerClientModule(spyClient1.getClientID(), spyClient1));
    }
}
package org.haland.javaasv.message;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class MessengerIntegrationTest {
    private MessengerServer server;
    private final long serverPeriod = 10;

    private TestClient testClientOne;
    private final String testClientOneID = "testClientOne";
    private final long testClientOnePeriod = 2000;
    @Mock
    private TestDispatcher testDispatcherOne;


    private TestClient testClientTwo;
    private final String testClientTwoID = "testClientTwo";
    private final long testClientTwoPeriod = 7000;
    @Mock
    private TestDispatcher testDispatcherTwo;

    @BeforeAll
    public void setupTest() {
        server = MessengerServer.getInstance();



        testClientOne = new TestClient(server, testClientOneID, testClientTwoID, testDispatcherOne);
        testClientTwo = new TestClient(server, testClientTwoID, testClientOneID, testDispatcherTwo);

        try {
            server.registerClientModule(testClientOne);
            server.registerClientModule(testClientTwo);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    @AfterAll
    public void teardownTest() {
        server.killInstance();
    }

    @Test
    public void testMessageSending() {
        ScheduledExecutorService serverExecutor = Executors.newScheduledThreadPool(1);

        ScheduledExecutorService clientOneExecutor = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService clientTwoExecutor = Executors.newScheduledThreadPool(1);

        // Run the server continuously
        serverExecutor.scheduleAtFixedRate(server, 0, serverPeriod, TimeUnit.MILLISECONDS);

        // Send each message once
        clientOneExecutor.schedule(testClientOne, 200, TimeUnit.MILLISECONDS);
        clientTwoExecutor.schedule(testClientTwo, 200, TimeUnit.MILLISECONDS);

        verify(testDispatcherOne, times(1)).dispatch();
        verify(testDispatcherTwo, times(1)).dispatch();
    }
}

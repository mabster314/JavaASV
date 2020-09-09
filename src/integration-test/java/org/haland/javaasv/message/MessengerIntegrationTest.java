package org.haland.javaasv.message;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith(MockitoExtension.class)
public class MessengerIntegrationTest {
    private final long serverPeriod = 10;
    private final String testClientOneID = "testClientOne";
    private final String testClientTwoID = "testClientTwo";
    private MessengerServer server;
    private TestClient testClientOne;
    private int testClientOneDispatched;
    private Dispatcher dispatcherOne;
    private TestClient testClientTwo;
    private int testClientTwoDispatched;
    private Dispatcher dispatcherTwo;

    @BeforeAll
    public void setupTest() {
        server = MessengerServer.getInstance();

        testClientOneDispatched = 0;
        dispatcherOne = () -> testClientOneDispatched++;

        testClientTwoDispatched = 0;
        dispatcherTwo = () -> testClientTwoDispatched++;


        testClientOne = new TestClient(server, testClientOneID, testClientTwoID, dispatcherOne);
        testClientTwo = new TestClient(server, testClientTwoID, testClientOneID, dispatcherTwo);

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
        server.startServer(serverPeriod);

        ScheduledExecutorService clientOneExecutor = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService clientTwoExecutor = Executors.newScheduledThreadPool(1);

        // Send each message once
        clientOneExecutor.schedule(testClientOne, 0, TimeUnit.MILLISECONDS);
        clientTwoExecutor.schedule(testClientTwo, 0, TimeUnit.MILLISECONDS);

        // Make sure we receive both packets
        await().until(() -> (testClientOneDispatched == 1) && (testClientTwoDispatched == 1));

        // Shutdown the server
        server.stopServer();
    }
}

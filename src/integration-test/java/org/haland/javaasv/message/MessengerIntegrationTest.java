package org.haland.javaasv.message;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.awaitility.Awaitility.*;

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

    private int testClientOneDispatched;
    private Dispatcher dispatcherOne ;


    private TestClient testClientTwo;
    private final String testClientTwoID = "testClientTwo";

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
        ScheduledExecutorService serverExecutor = Executors.newScheduledThreadPool(1);

        ScheduledExecutorService clientOneExecutor = Executors.newScheduledThreadPool(1);
        ScheduledExecutorService clientTwoExecutor = Executors.newScheduledThreadPool(1);

        // Run the server continuously
        serverExecutor.scheduleAtFixedRate(server, 0, serverPeriod, TimeUnit.MILLISECONDS);

        // Send each message once
        clientOneExecutor.schedule(testClientOne, 0, TimeUnit.MILLISECONDS);
        clientTwoExecutor.schedule(testClientTwo, 0, TimeUnit.MILLISECONDS);

        // Make sure we receive both packets
        await().until(() -> (testClientOneDispatched == 1) && (testClientTwoDispatched == 1));
    }
}

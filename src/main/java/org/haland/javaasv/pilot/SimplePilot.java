package org.haland.javaasv.pilot;

import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.*;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.util.Controller;
import org.haland.javaasv.util.PIDController;
import org.haland.javaasv.util.PilotUtil;
import org.tinylog.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SimplePilot implements MessengerClientInterface, Runnable {
    private static final String DEFAULT_CLIENT_ID = "simplePilot";

    private final String clientID;
    private final MessengerServerInterface server;
    private final HelmInterface helm;
    private Controller throttleController;
    private Controller rudderController;
    private GPSProviderInterface gps;
    private ScheduledExecutorService executor;

    private PilotMessageFactory messageFactory;
    private RouteInterface currentRoute;

    /**
     * Construct a new SimplePilot. PID controllers should be configured before injection.
     *
     * @param server             Server to dispatch through
     * @param helm               Helm to send messages to
     * @param throttleController PID controller for the throttle
     * @param rudderController   PID controller for the rudder
     * @param gps                GPS provider for the pilot
     */
    public SimplePilot(String clientID, MessengerServerInterface server, HelmInterface helm,
                       Controller throttleController, Controller rudderController,
                       GPSProviderInterface gps) {
        Logger.info("Attempting to instantiate SimplePilot");
        this.clientID = clientID;
        this.server = server;
        this.helm = helm;
        this.throttleController = throttleController;
        this.rudderController = rudderController;
        this.gps = gps;

        this.executor = Executors.newScheduledThreadPool(10);

        messageFactory = new PilotMessageFactory(clientID, helm.getClientID());

        try {
            server.registerClientModule(this);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    public SimplePilot(MessengerServerInterface server, HelmInterface helm, PIDController throttleController,
                       PIDController rudderController, GPSProviderInterface gps) {
        this(DEFAULT_CLIENT_ID, server, helm, throttleController, rudderController, gps);
    }

    public void setUpControllers() {
    }

    /**
     * Dispatches a message to the helm with the new throttle and rudder setpoints
     */
    @Override
    public void run() {
        // Calculate the XTD
        double xtd = calculateCrossTrackDistance();

        // Now dispatch the new helm instructions
        MessageInterface message = null;
        try {
            message = messageFactory.createMessage(throttleController.calculateNextOutput(xtd),
                    rudderController.calculateNextOutput(xtd));
        } catch (MessageTypeException e) {
            e.printStackTrace();
        }
        server.dispatch(message);
    }
    
    public void startPilot(long period) {
        Logger.info("Attempting to start SimplePilot");
        if (gps.getFixStatus()) {
            Logger.info("GPS ready, Starting SimplePilot");
            executor.scheduleAtFixedRate(this, 0, period, TimeUnit.MILLISECONDS);
        } else {
            Logger.warn("GPS not ready, delaying start by 5s");
            executor.schedule(new PilotStarter(period), 10, TimeUnit.SECONDS);
        }
    }

    public void stopPilot() {
        Logger.info("Stopping SimplePilot");
        executor.shutdown();
    }

    /**
     * Executes when receiving message from server. Currently unused.
     *
     * @param message Message from the server.
     */
    @Override
    public synchronized void dispatch(MessageInterface message) {
        Logger.debug("Pilot received message from " + message.getOriginID() + " for some reason");
    }

    /**
     * Calculates the cross-track distance using the current route segment
     *
     * @return The XTD in nmi
     */

    public double calculateCrossTrackDistance() {
        return PilotUtil.calculateCrossTrackDistance(currentRoute.getPreviousWaypoint().getCoordinates(),
                currentRoute.getNextWaypoint().getCoordinates(), gps.getCoordinates());
    }

    /**
     * Returns the ID of the messenger client
     *
     * @return the ID
     */
    @Override
    public String getClientID() {
        return clientID;
    }

    public void setCurrentRoute(RouteInterface newRoute) {
        this.currentRoute = newRoute;
    }

    /**
     * Returns the type of messages the client can handle
     *
     * @return
     */
    @Override
    public MessageInterface.MessageType getClientType() {
        return MessageInterface.MessageType.HELM;
    }

    public double[] getGPSCoordinates() {
        return gps.getCoordinates();
    }

    private class PilotMessageFactory {
        private final String originID;
        private final String destinationID;

        public PilotMessageFactory(String originID, String destinationID) {
            this.originID = originID;
            this.destinationID = destinationID;
        }

        /**
         * Creates a new message with origin <code>originID</code>, destination <code>arduinoHelmID</code>, message priority
         * <code>NORMAL</code>, the current system time, and a <code>String</code> message content containing data for the
         * Arduino helm.
         *
         * @param throttleSetpoint the new throttle setpoint to use
         * @param rudderSetpoint   the new rudder setpoint to use
         * @return a message representing the new helm instructions
         * @throws {@link MessageTypeException}
         */
        public HelmMessage createMessage(double throttleSetpoint, double rudderSetpoint) throws MessageTypeException {
            return new HelmMessage(originID, destinationID, System.currentTimeMillis(),
                    MessageInterface.MessagePriority.NORMAL, throttleSetpoint, rudderSetpoint);
        }
    }

    /**
     * Runnable to start pilot
     */
    private class PilotStarter implements Runnable {
        long period;

        private PilotStarter(long period) {
            this.period = period;
        }

        @Override
        public void run() {
            startPilot(period);
        }
    }
}

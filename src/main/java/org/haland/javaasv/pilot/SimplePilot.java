package org.haland.javaasv.pilot;

import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.*;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.util.PIDController;
import org.haland.javaasv.util.PilotUtil;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class SimplePilot implements MessengerClientInterface, Runnable {
    private static final String DEFAULT_CLIENT_ID = "simplePilot";

    private final String clientID;
    private final MessengerServerInterface server;
    private final HelmInterface helm;
    private PIDController throttleController;
    private PIDController rudderController;
    private GPSProviderInterface gpsProvider;
    private ExecutorService executor;

    private PilotMessageFactory messageFactory;

    private double[] gpsCoordinates;

    private RouteInterface currentRoute;

    /**
     * Construct a new SimplePilot. PID controllers should be configured before injection.
     *
     * @param server             Server to dispatch through
     * @param helm               Helm to send messages to
     * @param throttleController PID controller for the throttle
     * @param rudderController   PID controller for the rudder
     * @param gpsProvider        GPS provider for the pilot
     */
    public SimplePilot(String clientID, MessengerServerInterface server, HelmInterface helm,
                       PIDController throttleController, PIDController rudderController,
                       GPSProviderInterface gpsProvider) {
        this.clientID = clientID;
        this.server = server;
        this.helm = helm;
        this.throttleController = throttleController;
        this.rudderController = rudderController;
        this.gpsProvider = gpsProvider;

        this.executor = Executors.newFixedThreadPool(1);

        messageFactory = new PilotMessageFactory(clientID, helm.getClientID());

        try {
            server.registerClientModule(this);
        } catch (DuplicateKeyException e) {
            e.printStackTrace();
        }
    }

    public SimplePilot(MessengerServerInterface server, HelmInterface helm, PIDController throttleController,
                       PIDController rudderController, GPSProviderInterface gpsProvider) {
        this(DEFAULT_CLIENT_ID, server, helm, throttleController, rudderController, gpsProvider);
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

    /**
     * Executes when receiving message from server. Currently unused.
     *
     * @param message Message from the server.
     */
    @Override
    public synchronized void dispatch(MessageInterface message) {

    }

    /**
     * Updates the stored GPS coordinates
     */
    public void updateGPS() {

    }

    /**
     * Calculates the cross-track distance using the current route segment
     *
     * @return The XTD in nmi
     */

    public double calculateCrossTrackDistance() {
        return PilotUtil.calculateCrossTrackDistance(currentRoute.getPreviousWaypoint().getCoordinates(),
                currentRoute.getNextWaypoint().getCoordinates(), gpsCoordinates);
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
        return this.gpsCoordinates;
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
         */
        public HelmMessage createMessage(double throttleSetpoint, double rudderSetpoint) throws MessageTypeException {
            return new HelmMessage(originID, destinationID, System.currentTimeMillis(),
                    MessageInterface.MessagePriority.NORMAL, throttleSetpoint, rudderSetpoint);
        }
    }
}

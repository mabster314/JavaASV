package org.haland.javaasv.pilot;

import org.haland.javaasv.controller.Controller;
import org.haland.javaasv.controller.ControllerType;
import org.haland.javaasv.controller.PIDController;
import org.haland.javaasv.helm.HelmInterface;
import org.haland.javaasv.message.*;
import org.haland.javaasv.route.RouteInterface;
import org.haland.javaasv.util.EarthRadius;
import org.haland.javaasv.util.PilotUtil;
import org.tinylog.Logger;

import static java.lang.Thread.sleep;

public class SimplePilot implements MessengerClientInterface {
    private static final String DEFAULT_CLIENT_ID = "simplePilot";

    // Thread to run on
    private Thread thread;

    // Thread worker
    private PilotWorker worker;

    private final String clientID;
    private final MessengerServerInterface server;
    private final HelmInterface helm;
    private Controller throttleController;
    private Controller rudderController;
    private GPSProviderInterface gps;

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

    public void startControllers() {
        throttleController.start();
        rudderController.start();
    }

    public void startPilot(long period) {
        if (gps.getFixStatus()) {
            Logger.info("GPS ready, Starting SimplePilot");

            if (thread != null && thread.isAlive() && worker != null
                    && worker.isRunning()) {
                throw new IllegalStateException("Pilot worker is already running");
            }
            worker = new PilotWorker(this, period);
            thread = new Thread(worker);
            startControllers();
            thread.start();
        } else {
            Logger.warn("GPS not ready, delaying start by 5s");
            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            startPilot(period);
        }
    }

    public void stopPilot() {
        Logger.info("Stopping SimplePilot");
        if (worker != null) {
            worker.stop();
        }
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

    public double calculateCrossTrackDistance(EarthRadius earthRadius) {
        return PilotUtil.calculateCrossTrackDistance(currentRoute.getPreviousWaypoint().getCoordinates(),
                currentRoute.getNextWaypoint().getCoordinates(), gps.getCoordinates(), earthRadius);
    }

    private double calculateHeadingError() {
        return PilotUtil.calculateInitialBearingRadians(gps.getCoordinates(),
                currentRoute.getNextWaypoint().getCoordinates()) - gps.getHeading();
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
     * Runnable for pilot class
     */
    private class PilotWorker implements Runnable {
        private volatile boolean isRunning = true;

        private final SimplePilot pilot;
        private final long period;

        private PilotWorker(SimplePilot pilot, long period) {
            this.pilot = pilot;
            this.period = period;
        }

        /**
         * Dispatches a message to the helm with the new throttle and rudder setpoints
         */
        @Override
        public void run() {
            Logger.info("Starting pilot worker");
            while (isRunning) {
                try {
                    // Calculate the XTD
                    double xtd = calculateCrossTrackDistance(EarthRadius.METERS);
                    double headingError = calculateHeadingError();

                    double throttleOut = throttleController.calculateNextOutput();

                    double rudderOut = 0;
                    ControllerType type = rudderController.getType();
                    switch (type) {
                        case HITZ:
                            rudderOut = rudderController.calculateNextOutput(xtd, headingError, throttleOut);
                            break;
                        case PID:
                            rudderOut = rudderController.calculateNextOutput(xtd);
                            break;
                    }

                    Logger.trace("Xtd:" + xtd + "Rudder PID out: " + rudderOut);

                    // Now dispatch the new helm instructions
                    MessageInterface message = null;
                    try {
                        message = messageFactory.createMessage(throttleController.calculateNextOutput(), rudderOut / 100);
                    } catch (MessageTypeException e) {
                        e.printStackTrace();
                    }
                    server.dispatch(message);
                    try {
                        sleep(period);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    Logger.error(e);
                }
            }
        }

        public boolean isRunning() {
            return isRunning;
        }

        public void stop() {
            Logger.info("Stopping SimplePilot");
            isRunning = false;
        }
    }
}

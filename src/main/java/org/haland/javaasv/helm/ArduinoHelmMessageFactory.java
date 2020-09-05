package org.haland.javaasv.helm;

import org.haland.javaasv.message.*;
import org.haland.javaasv.util.SerialUtil;

import java.io.UnsupportedEncodingException;

/**
 * Creates messages to send between a {@link org.haland.javaasv.pilot.PilotInterface} and a helm.
 *
 * Messages sent to a helm contain new setpoints for the throttle and rudder. The helm will then return a message
 * containing the actual throttle and rudder position
 */
public class ArduinoHelmMessageFactory {
    private final String originID;
    private final String destinationID;

    /**
     * Constructor for a new factory
     *
     * @param originID name of the origin client module
     * @param destinationID name of the destination client module
     */
    public ArduinoHelmMessageFactory(String originID, String destinationID) {
        this.originID = originID;
        this.destinationID = destinationID;
    }

    /**
     * Creates a new message with origin <code>originID</code>, destination <code>arduinoHelmID</code>, message priority
     * <code>NORMAL</code>, the current system time, and a <code>String</code> message content containing data for the
     * Arduino helm. This should be called by a {@link org.haland.javaasv.pilot.PilotInterface}.
     *
     * @param throttleSetpoint the new throttle setpoint to use
     * @param rudderSetpoint the new rudder setpoint to use
     * @return a message representing the new helm instructions
     */
    public MessageInterface createMessage(double throttleSetpoint, double rudderSetpoint) throws MessageTypeException {
        return new HelmMessage(originID, destinationID, System.currentTimeMillis(),
                MessageInterface.MessagePriority.NORMAL, throttleSetpoint, rudderSetpoint);
    }

    /**
     * Creates a message with a given state string. This should be called by a helm.
     *
     * @param actualState string representing arduino state, generated with {@link HelmArduino#getHelmState()}
     * @return a message representing the helm state
     */
    public MessageInterface createMessage(String actualState) throws MessageTypeException {
        return new HelmMessage(originID, destinationID, System.currentTimeMillis(),
                MessageInterface.MessagePriority.NORMAL, actualState);
    }
}

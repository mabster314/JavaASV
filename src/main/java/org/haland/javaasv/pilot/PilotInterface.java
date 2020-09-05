package org.haland.javaasv.pilot;

import org.haland.javaasv.message.MessengerClientInterface;

public interface PilotInterface extends MessengerClientInterface {
    void readGPSData();

    double calculateThrottlePID(double throttlePosition);

    double calculateRudderPID(double rudderPosition);
}

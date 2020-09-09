package org.haland.javaasv.helm;

import org.haland.javaasv.message.MessageInterface;
import org.haland.javaasv.message.MessengerClientInterface;

/**
 * Helms should receive messages of type {@link org.haland.javaasv.message.MessageInterface.MessageType#HELM}.
 */
public interface HelmInterface extends MessengerClientInterface {
    /**
     * Dispatch a message. Should send actual state message in return
     *
     * @param message a message of type {@link org.haland.javaasv.message.MessageInterface.MessageType#HELM}
     */
    @Override
    void dispatch(MessageInterface message);
}

package ovh.fedox.ampbot.listener.types;

import ovh.fedox.ampbot.core.AMPClient;

/**
 * © 2024 Florian O and Fabian W.
 * Created on: 10/29/2024 8:25 AM
 * <p>
 * https://www.youtube.com/watch?v=tjBCjfB3Hq8
 */

public interface AMPListener<T> {
    void dispatch(T event, AMPClient client);
}
package core.util.platform.host.network;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkHelper {

    private NetworkHelper() { }

    private static final Logger LOGGER = LogManager.getLogger(NetworkHelper.class);

    public static String getMachineIPAddress(){
        try {
            return InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            LOGGER.debug(e.getMessage());
        }
        return getLoopBackAddress();
    }

    public static String getLoopBackAddress(){
        return InetAddress.getLoopbackAddress().getHostAddress();
    }
}

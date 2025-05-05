package src.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IPAdress {

    public static String getPrivateIP() {
        try {
            InetAddress localhost = InetAddress.getLocalHost();
            return localhost.getHostAddress();
        } catch (UnknownHostException e) {
            e.printStackTrace();
            return "127.0.0.1";
        }
    }
}

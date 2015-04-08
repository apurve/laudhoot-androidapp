package com.laudhoot.web;

/**
 * Created by apurve on 3/4/15.
 */
public class ServerIPAddress {

    String protocol = "http";

    String ipAddress = "192.168.1.7";

    String port = "8080";

    String serverContext = "laudhoot";

    public ServerIPAddress(String protocol, String ipAddress, String port, String serverContext) {
        this.protocol = protocol;
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverContext = serverContext;
    }

    public ServerIPAddress(String ipAddress, String port, String serverContext) {
        this.ipAddress = ipAddress;
        this.port = port;
        this.serverContext = serverContext;
    }

    public ServerIPAddress(String ipAddress, String port) {
        this.ipAddress = ipAddress;
        this.port = port;
    }

    public ServerIPAddress() {
        super();
    }

    public String getURL() {
        if(port.isEmpty()){
            return protocol + "://" +
                    ipAddress + "/" +
                    serverContext;
        }else{
            return protocol + "://" +
                    ipAddress + ":" +
                    port + "/" +
                    serverContext;
        }
    }

}

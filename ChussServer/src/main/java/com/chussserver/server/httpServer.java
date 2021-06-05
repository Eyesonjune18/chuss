package com.chussserver.server;

import com.chussserver.server.config.Configuration;
import com.chussserver.server.config.ConfigurationManager;
import com.chussserver.server.core.ServerListenerThread;
import com.chussserver.server.core.MultiplayerChuss;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Queue;

import static com.chussserver.server.core.ServerListenerThread.socketArray;


/**
 * Driver class for the server
 */


public class httpServer {

    private final static Logger LOGGER = LoggerFactory.getLogger(httpServer.class);

    public static void main(String[] args) {

        LOGGER.info("Server starting...");

        ConfigurationManager.getInstance().loadConfigurationFile("src/main/resources/http.json");
        Configuration conf = ConfigurationManager.getInstance().getCurrentConfiguration();

        LOGGER.info("Using Port: " + conf.getPort());
        LOGGER.info("Using WebRoot: " + conf.getWebroot());

        try {
            ServerListenerThread serverListenerThread = new ServerListenerThread(conf.getPort(), conf.getWebroot());
            serverListenerThread.start();



        } catch (IOException e) {
            e.printStackTrace();
            //TODO handle later.
        }
    }
}

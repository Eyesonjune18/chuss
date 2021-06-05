package com.chussserver.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


public class ServerListenerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(ServerListenerThread.class);

    private int port;
    private String webroot;
    private ServerSocket serverSocket;

    public ServerListenerThread(int port, String webroot) throws IOException {
        this.port = port;
        this.webroot = webroot;
        this.serverSocket = new ServerSocket(this.port);
    }

   public static ArrayList socketArray = new ArrayList();

    @Override
    public void run() {

        try {
            Queue inetQueue = new LinkedList();

            while (serverSocket.isBound() && !serverSocket.isClosed()) {
                Socket socket = serverSocket.accept();
                if (!inetQueue.contains(socket.getInetAddress())) {
                    socketArray.add(socket);
                    inetQueue.add(socket.getInetAddress());
                }


                LOGGER.info(" * Connection accepted: " + socket.getInetAddress());
                LOGGER.info(" * The Socket is: " + socket);
                LOGGER.info(" * Connected Sockets: " + socketArray);
                LOGGER.info(" * Connected IPs: " + inetQueue);


                ChussConnetionWorkerThread workerThread = new ChussConnetionWorkerThread(socket);
                workerThread.start();


                if(socketArray.size() == 2) {
                    MultiplayerChuss multiplayerChuss = new MultiplayerChuss();
                    multiplayerChuss.start();
                    LOGGER.info(" * The Game Has Started!");
                }
            }

        } catch (IOException e) {
            LOGGER.error("Problem with setting socket", e);
        } finally {
            if (serverSocket != null) {
                try {
                    serverSocket.close();
                } catch (IOException e) {}
            }
        }

    }
}

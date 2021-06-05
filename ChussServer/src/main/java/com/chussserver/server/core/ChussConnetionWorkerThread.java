package com.chussserver.server.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.Socket;

public class ChussConnetionWorkerThread extends Thread {

    private final static Logger LOGGER = LoggerFactory.getLogger(ChussConnetionWorkerThread.class);

    private Socket socket;
    public ChussConnetionWorkerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        InputStream inputStream = null;
        OutputStream outputStream = null;

        try {
            inputStream = socket.getInputStream();
            outputStream = socket.getOutputStream();

            String html = "<html><head><title>Chuss Server</title></head><body><h1>This is a test for the Chuss multiplayer server</h1></body></html>";


            final String CRLF = "\n\r"; //13, 10

            String response =
                    "HTTP/1.1 200 OK" + CRLF + //Status Line   :   HTTP_VERSION RESPONSE_CODE RESPONSE_MESSAGE
                            "Content-Length: " + html.getBytes().length + CRLF +// HEADER
                            CRLF +
                            html +
                            CRLF + CRLF;

            outputStream.write(response.getBytes());


            try {
                sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LOGGER.info(" * Connection Processing Finished.");
        } catch (IOException e) {
            LOGGER.error("Problem with Communication", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {}
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {}
            }
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {}
            }
        }
    }
}

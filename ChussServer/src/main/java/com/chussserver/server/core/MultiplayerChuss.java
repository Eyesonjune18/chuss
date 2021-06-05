package com.chussserver.server.core;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;

import static com.chussserver.server.core.ServerListenerThread.socketArray;

public class MultiplayerChuss extends Thread {

    private ServerSocket serverSocket;
    Socket white = (Socket) socketArray.get(0);
    Socket black = (Socket) socketArray.get(1);

    OutputStream whiteOutput = null;
    OutputStream blackOutput = null;


    @Override
    public void run() {

        try{

            String whiteMove = "W";
            String blackMove = "B";

            InputStreamReader whiteReader = new InputStreamReader(white.getInputStream());
            BufferedReader whiteBuffer = new BufferedReader(whiteReader);
            InputStreamReader blackReader = new InputStreamReader(black.getInputStream());
            BufferedReader blackBuffer = new BufferedReader(blackReader);



            while (serverSocket.isBound() && !serverSocket.isClosed()) {

                if(!whiteMove.equals(String.valueOf(whiteBuffer))) {
                    whiteMove = String.valueOf(whiteBuffer);
                    blackOutput.write(whiteMove.getBytes(Charset.forName("UTF-8")));
                } else{
                    if(!blackMove.equals(String.valueOf(blackBuffer))) {
                        blackMove = String.valueOf(blackBuffer);
                        whiteOutput.write(blackMove.getBytes(Charset.forName("UTF-8")));
                    }

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

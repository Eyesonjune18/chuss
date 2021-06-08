package chuss;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Client {

    static Socket socket;
    private static OutputStream myMoveStream = null;
    private static BufferedReader theirBuffer;

    public static void setupClient() throws IOException {

        socket = new Socket("73.35.247.165", 80);
        InputStreamReader theirMoveReader = new InputStreamReader(socket.getInputStream());
        theirBuffer = new BufferedReader(theirMoveReader);
        myMoveStream = socket.getOutputStream();
        InputStream theirMoveStream = socket.getInputStream();

    }

    public static String getEnemyMove() throws IOException {

        /*
        wait for enemy move, parse and return as string
         */

        String theirMove = "";
        theirMove = theirBuffer.readLine();

        return theirMove;

    }

    public static void sendMove(String myMove) throws IOException {

        /*
        send the passed move to the enemy, to be retrieved by their getEnemyMove
         */

        myMove += "\n";

        myMoveStream.write(myMove.getBytes(StandardCharsets.UTF_8));

    }

}
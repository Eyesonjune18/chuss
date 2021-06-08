import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MultiplayerChuss extends Thread {

    private ServerSocket serverSocket;
    Socket white = (Socket) ChussServerFinal.socketArray.get(0);
    Socket black = (Socket) ChussServerFinal.socketArray.get(1);

    OutputStream whiteOutput;
    OutputStream blackOutput;

    {
        try {
            whiteOutput = white.getOutputStream();
            blackOutput = black.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try{

            String whiteMove = "";
            String blackMove = "";

            InputStreamReader whiteReader = new InputStreamReader(white.getInputStream());
            InputStreamReader blackReader = new InputStreamReader(black.getInputStream());

            do {

                ArrayList<Character> whiteMessage = new ArrayList<>();
                ArrayList<Character> blackMessage = new ArrayList<>();

                while(whiteReader.ready()) whiteMessage.add((char) whiteReader.read());
                while(blackReader.ready()) blackMessage.add((char) blackReader.read());

                Character[] whiteMessageArr = new Character[0];
                whiteMessageArr = whiteMessage.toArray(whiteMessageArr);
                Character[] blackMessageArr = new Character[0];
                blackMessageArr = blackMessage.toArray(blackMessageArr);

                whiteMove = new String(getPrimitiveArray(whiteMessageArr));
                blackMove = new String(getPrimitiveArray(blackMessageArr));

                if(!whiteMove.equals("")) {
                    System.out.print("White moved:" + whiteMove);
                    blackOutput.write(whiteMove.getBytes(StandardCharsets.UTF_8));
                }
                if(!blackMove.equals("")) {
                    System.out.print("Black moved:" + blackMove);
                    whiteOutput.write(blackMove.getBytes(StandardCharsets.UTF_8));
                }

            } while(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private char[] getPrimitiveArray(Character[] objArr) {

        char[] primitiveArray = new char[objArr.length];

        for(int i = 0; i < objArr.length; i++) {

            primitiveArray[i] = objArr[i];

        }

        return primitiveArray;

    }
}

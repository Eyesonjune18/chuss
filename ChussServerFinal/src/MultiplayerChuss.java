import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
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
            BufferedReader whiteBuffer = new BufferedReader(whiteReader);
            InputStreamReader blackReader = new InputStreamReader(black.getInputStream());
            BufferedReader blackBuffer = new BufferedReader(blackReader);

            ArrayList<Character> whiteMessage = new ArrayList<>();
            ArrayList<Character> blackMessage = new ArrayList<>();

            boolean whiteHasBeenPrinted = false;
            boolean blackHasBeenPrinted = false;

            do {

                while(whiteReader.ready()) whiteMessage.add((char) whiteReader.read());
                while(blackReader.ready()) blackMessage.add((char) blackReader.read());

                if(!whiteMove.equals(String.valueOf(whiteBuffer))) {
                    whiteMove = String.valueOf(whiteBuffer);
                    blackOutput.write(whiteMove.getBytes(StandardCharsets.UTF_8));
                } else{
                    if(!blackMove.equals(String.valueOf(blackBuffer))) {
                        blackMove = String.valueOf(blackBuffer);
                        whiteOutput.write(blackMove.getBytes(StandardCharsets.UTF_8));
                    }

                }

                Character[] whiteMessageArr = new Character[0];
                whiteMessageArr = whiteMessage.toArray(whiteMessageArr);
                Character[] blackMessageArr = new Character[0];
                blackMessageArr = blackMessage.toArray(blackMessageArr);

                whiteMove = new String(getPrimitiveArray(whiteMessageArr));
                blackMove = new String(getPrimitiveArray(blackMessageArr));

                if(!whiteMove.equals("") && !whiteHasBeenPrinted) { System.out.println(whiteMove); whiteHasBeenPrinted = true; }
                if(!blackMove.equals("") && !blackHasBeenPrinted) { System.out.println(blackMove); blackHasBeenPrinted = true; }

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

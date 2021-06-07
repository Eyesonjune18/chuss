import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    static Socket socket;

    private static OutputStream myMoveStream = null;
    private static InputStream theirMoveStream = null;
    private static InputStreamReader theirMoveReader;
    private static BufferedReader theirBuffer;
    public static String myMove = null;
    public static String theirMove = null;

    public static void main(String[] args) throws IOException {
        socket = new Socket("73.35.247.165", 80);
        theirMoveReader = new InputStreamReader(socket.getInputStream());
        theirBuffer = new BufferedReader(theirMoveReader);
        myMoveStream = socket.getOutputStream();
        theirMoveStream = socket.getInputStream();
        Scanner textTest = new Scanner(System.in);
        System.out.println("Say Something");

        myMove = textTest.nextLine();
        System.out.println("You Said: " + myMove);

        //myMove = "YOUR_STRING_HERE";

        myMoveStream.write(myMove.getBytes(Charset.forName("UTF-8")));
        String bufferLine = null;
        do {
            bufferLine = theirBuffer.readLine();
            if(theirMoveStream != null){
                theirMove = theirBuffer.readLine();
            }
        } while(!theirMove.equals(bufferLine));
        System.out.println("They Said: " + theirMove);
    }
}

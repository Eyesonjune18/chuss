import java.io.*;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Client {
    static Socket socket;

    {
        try {
            socket = new Socket("http://73.35.247.165/", 80);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static OutputStream myMoveStream = null;
    private static InputStream theirMoveStream = null;

    private static InputStreamReader theirMoveReader;

    static {
        try {
            theirMoveReader = new InputStreamReader(socket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static BufferedReader theirBuffer = new BufferedReader(theirMoveReader);


    public static String myMove = null;
    public static String theirMove = null;


    public static void main(String[] args) throws IOException {
        Scanner textTest = new Scanner(System.in);
        System.out.println("Say Something");

        myMove = textTest.nextLine();
        System.out.println("You Said: " + myMove);

        //myMove = "YOUR_STRING_HERE";
        myMoveStream.write(myMove.getBytes(Charset.forName("UTF-8")));
        while(!theirMove.equals(String.valueOf(theirBuffer))) {
            if(theirMoveStream != null){
                theirMove = String.valueOf(theirBuffer);
            }
        }
        System.out.println("They Said: " + theirMove);
    }
}

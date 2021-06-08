import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class ChussServerFinal {
    private ServerSocket serverSocket;
    static ArrayList socketArray = new ArrayList();
    static Queue inetQueue = new LinkedList();

    public void start() throws IOException {
        serverSocket = new ServerSocket(80);
        System.out.println("Server Starting...");
        while (true && socketArray.size() != 2)
            new ClientHandler(serverSocket.accept()).start();
    }

    public void stop() throws IOException {
        serverSocket.close();
        System.out.println("The Socket has closed.");
    }

    private static class ClientHandler extends Thread {
        private Socket clientSocket;
        private PrintWriter out;
        private BufferedReader in;


        public ClientHandler(Socket socket) {
            this.clientSocket = socket;
            if (!inetQueue.contains(socket.getInetAddress())) {
                socketArray.add(socket);
                inetQueue.add(socket.getInetAddress());
                System.out.println("Player Connected.");
                if (socketArray.size() == 2) {
                    MultiplayerChuss multiplayerChuss = new MultiplayerChuss();
                    multiplayerChuss.start();
                    System.out.println("The game has started.");
                }
            }
        }
    }

    public static void main(String[] args) throws IOException {
    ChussServerFinal server = new ChussServerFinal();
    server.start();
    }
}
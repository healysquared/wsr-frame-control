
import java.net.*;
import java.io.*;
import java.util.stream.Collectors;

public class TCPDataReceiver implements Runnable {

    @Override
    public void run() {
        String line;
        try {
            System.out.println("Big brother is listening...");
            ServerSocket serverSocket = new ServerSocket(6868);
            serverSocket.setSoTimeout(500); //Timeout after 500ms
            while (true) {
                Socket socket = serverSocket.accept();
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                line = reader.readLine();
                System.out.println("Received: " + line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

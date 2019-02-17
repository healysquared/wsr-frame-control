
import java.net.*;
import java.io.*;
import java.util.stream.Collectors;

public class TCPDataReceiver implements Runnable {

    @Override
    public void run() {
        String line;
        ServerSocket serverSocket = null;
        try {

            serverSocket = new ServerSocket(6868);
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Big brother is listening...");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                socket.setSoTimeout(1000);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                line = reader.readLine();
                System.out.println("Received: " + line);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}


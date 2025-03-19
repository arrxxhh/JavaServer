import java.io.*;
import java.net.*;

public class Client {
    public static void main(String[] args) {
        for (int i = 1; i <= 5; i++) { // Simulate 5 clients
            int clientId = i;
            new Thread(() -> makeRequest(clientId)).start();
        }
    }

    private static void makeRequest(int clientId) {
        try (Socket socket = new Socket("localhost", 8080);
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()))) {

            // Send HTTP GET request
            out.println("GET /index.html HTTP/1.1");
            out.println("Host: localhost");
            out.println("Connection: close");
            out.println(); // Empty line to indicate end of request

            // Read response
            String responseLine;
            System.out.println("Client " + clientId + " received:");
            while ((responseLine = in.readLine()) != null) {
                System.out.println(responseLine);
            }
            System.out.println("--------------------------------------");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

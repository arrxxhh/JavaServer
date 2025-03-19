import java.io.*;
import java.net.*;

public class SimpleHTTPServer {
    public static void main(String[] args) {
        int port = 8080; // Port number

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running on http://localhost:" + port);

            while (true) { // Keep listening for incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle the client request
                handleClient(clientSocket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)
        ) {
            // Read HTTP request (only the first line)
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);

            // Send HTTP response
            String httpResponse = "HTTP/1.1 200 OK\r\n" +
                                  "Content-Type: text/plain\r\n" +
                                  "\r\n" +
                                  "Hello, this is a simple HTTP server!";
            out.println(httpResponse);

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

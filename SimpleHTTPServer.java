import java.io.*;
import java.net.*;
import java.util.Date;
import java.text.SimpleDateFormat;

public class SimpleHTTPServer {
    private static final int PORT = 8080;
    private static final String WEB_ROOT = "www";
    private static final String LOG_FILE = "server.log";

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("Server is running on http://localhost:" + PORT);

            while (true) { // Keep listening for incoming connections
                Socket clientSocket = serverSocket.accept();
                System.out.println("New client connected: " + clientSocket.getInetAddress());

                // Handle the client request
                new Thread(() -> handleClient(clientSocket)).start();

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void handleClient(Socket clientSocket) {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                OutputStream out = clientSocket.getOutputStream();) {
            // Read HTTP request (only the first line)
            String requestLine = in.readLine();
            System.out.println("Received request: " + requestLine);
            if (requestLine == null || requestLine.isEmpty())
                return;

            // Parse HTTP request
            String[] tokens = requestLine.split(" ");
            if (tokens.length < 2)
                return;
            String requestedFile = tokens[1];

            if (requestedFile.equals("/")) {
                requestedFile = "/index.html";
            }
            if (requestedFile.startsWith("/hello")) {
                String name = requestedFile.contains("?name=") ? requestedFile.split("=")[1] : "Guest";
                String dynamicResponse = "<html><body><h1>Hello, " + name + "!</h1></body></html>";
                String header = "HTTP/1.1 200 OK\r\n" +
                        "Content-Type: text/html\r\n" +
                        "Content-Length: " + dynamicResponse.length() + "\r\n\r\n";
                out.write(header.getBytes());
                out.write(dynamicResponse.getBytes());
                out.flush();
            }
            // Serve static files
            File file = new File(WEB_ROOT + requestedFile);
            if (file.exists() && !file.isDirectory()) {
                sendResponse(out, "HTTP/1.1 200 OK", file);
            } else {
                sendNotFound(out);
            }
            logRequest(clientSocket.getInetAddress().toString(), requestedFile);

            // // Send HTTP response
            // String httpResponse = "HTTP/1.1 200 OK\r\n" +
            // "Content-Type: text/plain\r\n" +
            // "\r\n" +
            // "Hello, this is a simple HTTP server!";
            // out.println(httpResponse);

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

    private static void sendResponse(OutputStream out, String status, File file) throws IOException {
        byte[] fileContent = readFile(file);
        String contentType = getMimeType(file.getName());
        String header = status + "\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + fileContent.length + "\r\n" +
                "\r\n";
        out.write(header.getBytes());
        out.write(fileContent);
        out.flush();
    }

    private static void sendNotFound(OutputStream out) throws IOException {
        String response = "HTTP/1.1 404 Not Found\r\n" +
                "Content-Type: text/html\r\n" +
                "\r\n" +
                "<h1>404 - File Not Found</h1>";
        out.write(response.getBytes());
        out.flush();
    }

    private static byte[] readFile(File file) throws IOException {
        FileInputStream fis = new FileInputStream(file);
        byte[] data = new byte[(int) file.length()];
        fis.read(data);
        fis.close();
        return data;
    }

    private static void logRequest(String clientIP, String requestedFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("server.log", true))) {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            writer.write(timestamp + " - " + clientIP + " requested " + requestedFile + "\n");
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String getMimeType(String fileName) {
        if (fileName.endsWith(".html"))
            return "text/html";
        if (fileName.endsWith(".css"))
            return "text/css";
        if (fileName.endsWith(".js"))
            return "application/javascript";
        if (fileName.endsWith(".png"))
            return "image/png";
        if (fileName.endsWith(".jpg") || fileName.endsWith(".jpeg"))
            return "image/jpeg";
        if (fileName.endsWith(".gif"))
            return "image/gif";
        return "application/octet-stream"; // Default binary type
    }

}

# Simple HTTP Server in Java 🚀

A lightweight, multi-threaded HTTP server built using Java. Supports static file serving, logging, and basic dynamic content.

## 📌 Features
- 🖥️ Serves HTML, CSS, JS, and images
- 🔄 Multi-threaded request handling
- 📝 Logs all requests to `server.log`
- 🌐 Supports dynamic content (`/hello?name=John`)

## 🛠 Setup & Run

1. Clone the repository:
   ```sh
   git clone https://github.com/YOUR_USERNAME/SimpleHTTPServer.git
   cd SimpleHTTPServer

2. Compile the Java program:
    ```sh
    javac SimpleHTTPServer.java

3. Run the server:
    ```sh
    java SimpleHTTPServer

4. Open in browser:
    ```sh
    http://localhost:8080

## 📝 Logging
    Each request is logged in server.log:
    2025-03-20 12:00:34 - /index.html
    2025-03-20 12:01:10 - /style.css
    2025-03-20 12:02:21 - /script.js
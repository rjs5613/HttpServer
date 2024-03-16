package com.github.rjs5613;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            ServerConfig config = new ServerConfig(8080);
            AsyncHttpServer httpServer = new AsyncHttpServer(config, new RequestHandlerRegistry());
            httpServer.register("/", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is good", HttpStatus.SUCCESS));
            httpServer.register("/better", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is better", HttpStatus.SUCCESS));
            httpServer.register("/best", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is the best", HttpStatus.SUCCESS));
            httpServer.register("/error", HttpMethod.GET, (HttpRequest request) -> {
                throw new ForbiddenError("Request is forbidden");
            });
            httpServer.start();

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
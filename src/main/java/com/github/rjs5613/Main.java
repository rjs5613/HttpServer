package com.github.rjs5613;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        try {
            AsyncHttpServer asyncHttpServer = new AsyncHttpServer(null);
            asyncHttpServer.register("/", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is good", HttpStatus.SUCCESS));
            asyncHttpServer.register("/better", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is better", HttpStatus.SUCCESS));
            asyncHttpServer.register("/best", HttpMethod.GET, (HttpRequest request) -> new ResponseEntity<>("This is the best", HttpStatus.SUCCESS));
            asyncHttpServer.start();

        } catch (InterruptedException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
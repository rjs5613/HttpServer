package com.github.rjs5613;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

public class ResponseEntity<T> {

    private final T body;
    private final HttpStatus status;

    private final Set<HttpHeader> headers;

    public ResponseEntity(T body, HttpStatus status) {
        this.body = body;
        this.status = status;
        headers = new HashSet<>();
        addDefaultHeaders();
    }

    private void addDefaultHeaders() {
        headers.add(HttpHeader.of("Content-Type", "text/plain"));
        headers.add(HttpHeader.of("Date", new Date().toString()));
        headers.add(HttpHeader.of("Server", "AsyncHttpServer:1.0"));
        try {
            headers.add(HttpHeader.of("host", InetAddress.getLocalHost().toString()));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    public T body() {
        return body;
    }

    public HttpStatus status() {
        return status;
    }

    public String asString() {
        headers.add(HttpHeader.of("Content-Length", Integer.toString(body.toString().length())));
        StringBuilder finalResponse =
                new StringBuilder("\n")
                        .append("HTTP/1.1 ")
                        .append(status.getString())
                        .append("\n");
        headers.forEach(
                (header) -> finalResponse.append(header.name()).append(": ").append(String.join("", header.values())).append("\n"));
        finalResponse.append("\r\n");
        finalResponse.append(body);
        return finalResponse.toString();
    }

}

package com.github.rjs5613;

import java.nio.ByteBuffer;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class HttpRequest {

    private final HttpMethod httpMethod;

    private final Set<HttpHeaders> headers;
    private final String path;
    private String body;

    public HttpRequest(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.headers = new HashSet<>();
    }

    public HttpRequest(ByteBuffer buffer) {

        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String requestText = new String(data);
        String[] lines = requestText.split("\r\n");
        String line1 = lines[0];
        String[] split = line1.split(" ");
        headers = new HashSet<>();
        this.httpMethod = HttpMethod.valueOf(split[0].toUpperCase());
        this.path = split[1];
        for (int i = 1; i < lines.length; i++) {
            if (lines[i].trim().isEmpty()) {
                this.body = lines[i + 1];
                break;
            }
            headers.add(HttpHeaders.from(lines[i]));
        }
    }

    public HttpMethod getHttpMethod() {
        return httpMethod;
    }

    public String getPath() {
        return path;
    }

    public String getBody() {
        return body;
    }

    @Override
    public String toString() {
        return "HttpRequest{" +
                "httpMethod=" + httpMethod +
                ", headers=" + headers +
                ", path='" + path + '\'' +
                ", body='" + body + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HttpRequest that = (HttpRequest) o;
        return httpMethod == that.httpMethod && Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        return Objects.hash(httpMethod, path);
    }
}

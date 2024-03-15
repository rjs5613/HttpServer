package com.github.rjs5613;

import java.nio.ByteBuffer;
import java.util.*;

public class HttpRequest {

    private final HttpMethod httpMethod;
    private final Map<HttpHeader, List<String>> headers;
    private final String path;
    private final Map<String, String> queryMap;
    private String body;

    public HttpRequest(HttpMethod httpMethod, String path) {
        this.httpMethod = httpMethod;
        this.path = path;
        this.headers = new HashMap<>();
        this.queryMap = new HashMap<>();
    }

    public HttpRequest(ByteBuffer buffer) {
        buffer.flip();
        byte[] data = new byte[buffer.limit()];
        buffer.get(data);
        String requestText = new String(data);
        String[] lines = requestText.split("\r\n");

        String[] split = lines[0].split(" ");
        this.httpMethod = HttpMethod.valueOf(split[0].toUpperCase());
        String pathString = split[1];
        String[] pathSplit = pathString.split("\\?");
        this.path = pathSplit[0];
        this.queryMap = new HashMap<>();
        if (pathSplit.length > 1) {
            updateQueryMap(pathSplit[1]);
        }

        this.headers = new HashMap<>();

        parseHeadersAndBody(Arrays.copyOfRange(lines, 1, lines.length));
    }

    private void parseHeadersAndBody(String[] lines) {
        boolean foundEmptyLine = false;
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                foundEmptyLine = true;
                continue;
            }

            if (foundEmptyLine) {
                this.body = line;
                break;
            }

            HttpHeader header = HttpHeader.from(line);
            this.headers.computeIfAbsent(header, k -> new ArrayList<>()).addAll(header.values());
        }
    }

    private void updateQueryMap(String queryString) {
        Arrays.stream(queryString.split("&"))
                .map(entry -> entry.trim().split("="))
                .forEach(split -> this.queryMap.put(split[0], split[1]));
    }

    public HttpMethod httpMethods() {
        return httpMethod;
    }

    public String path() {
        return path;
    }

    public String body() {
        return body;
    }

    public Set<HttpHeader> headers() {
        return Collections.unmodifiableSet(headers.keySet());
    }

    public Map<String, String> queryMap() {
        return Collections.unmodifiableMap(queryMap);
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



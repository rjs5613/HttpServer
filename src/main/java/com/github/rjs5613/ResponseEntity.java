package com.github.rjs5613;

import java.util.HashSet;
import java.util.Set;

public class ResponseEntity<T> {

    private final T body;
    private final HttpStatus status;

    public ResponseEntity(T body, HttpStatus status) {
        this.body = body;
        this.status = status;
    }

    public T body() {
        return body;
    }

    public HttpStatus status() {
        return status;
    }
}

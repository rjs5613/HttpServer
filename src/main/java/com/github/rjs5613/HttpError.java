package com.github.rjs5613;

public abstract class HttpError extends RuntimeException {

    public HttpError(String message) {
        super(message);
    }

    public abstract HttpStatus status();

    public String message() {
        return getMessage();
    }

}

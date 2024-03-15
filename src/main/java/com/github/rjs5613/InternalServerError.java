package com.github.rjs5613;

public class InternalServerError extends HttpError {
    public InternalServerError(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.INTERNAL_SERVER_ERROR;
    }
}

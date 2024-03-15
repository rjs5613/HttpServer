package com.github.rjs5613;

public class NotFoundError extends HttpError {
    public NotFoundError(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}

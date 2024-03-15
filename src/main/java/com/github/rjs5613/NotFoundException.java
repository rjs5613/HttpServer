package com.github.rjs5613;

public class NotFoundException extends HttpError {
    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.NOT_FOUND;
    }
}

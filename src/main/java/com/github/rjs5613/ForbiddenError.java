package com.github.rjs5613;

public class ForbiddenError extends HttpError {
    public ForbiddenError(String message) {
        super(message);
    }

    @Override
    public HttpStatus status() {
        return HttpStatus.FORBIDDEN;
    }
}

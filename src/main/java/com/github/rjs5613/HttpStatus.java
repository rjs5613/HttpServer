package com.github.rjs5613;

public enum HttpStatus {
    SUCCESS(200, "OK"), NOT_FOUND(404, "Not Found"), INTERNAL_SERVER_ERROR(500, "Internal Server Error"), FORBIDDEN(403, "Forbidden");

    private final int status;
    private final String message;

    HttpStatus(int status, String message) {
        this.status = status;
        this.message = message;
    }

    public int status() {
        return status;
    }

    public String getString() {
        return status + " " + message;
    }
}

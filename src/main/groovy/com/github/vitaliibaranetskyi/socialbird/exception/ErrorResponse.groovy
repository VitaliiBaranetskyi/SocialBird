package com.github.vitaliibaranetskyi.socialbird.exception

import java.time.LocalDateTime

class ErrorResponse {
    private LocalDateTime timestamp
    private int status
    private String error
    private String message
    private String path

    ErrorResponse(LocalDateTime timestamp, int status, String error, String message, String path) {
        this.timestamp = timestamp
        this.status = status
        this.error = error
        this.message = message
        this.path = path
    }

    ErrorResponse() {
    }

    LocalDateTime getTimestamp() {
        return timestamp
    }

    void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp
    }

    int getStatus() {
        return status
    }

    void setStatus(int status) {
        this.status = status
    }

    String getError() {
        return error
    }

    void setError(String error) {
        this.error = error
    }

    String getMessage() {
        return message
    }

    void setMessage(String message) {
        this.message = message
    }

    String getPath() {
        return path
    }

    void setPath(String path) {
        this.path = path
    }
}

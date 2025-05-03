package br.com.alura.AluraFake.exepctions;

import org.springframework.http.HttpStatus;

public class TaskFullException extends RuntimeException {

    private final HttpStatus status;

    private TaskFullException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static TaskFullException badRequest(String message) {
        return new TaskFullException(message, HttpStatus.BAD_REQUEST);
    }

    public static TaskFullException notFound(String message) {
        return new TaskFullException(message, HttpStatus.NOT_FOUND);
    }
    public HttpStatus getStatus() {
        return status;
    }
}

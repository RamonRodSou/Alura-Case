package br.com.alura.AluraFake.exepctions;

import org.springframework.http.HttpStatus;

public class TaskException extends RuntimeException {

    private final HttpStatus status;

    private TaskException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static TaskException badRequest(String message) {
        return new TaskException(message, HttpStatus.BAD_REQUEST);
    }

    public static TaskException notFound(String message) {
        return new TaskException(message, HttpStatus.NOT_FOUND);
    }
    public HttpStatus getStatus() {
        return status;
    }
}

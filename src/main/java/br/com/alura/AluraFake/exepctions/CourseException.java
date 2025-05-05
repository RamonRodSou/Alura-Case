package br.com.alura.AluraFake.exepctions;

import org.springframework.http.HttpStatus;

public class CourseException extends RuntimeException {

    private final HttpStatus status;

    public CourseException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static CourseException bedRequest(String message) {
        return new CourseException(message, HttpStatus.BAD_REQUEST);
    }

    public static CourseException notFound(String message) {
        return new CourseException(message, HttpStatus.NOT_FOUND);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

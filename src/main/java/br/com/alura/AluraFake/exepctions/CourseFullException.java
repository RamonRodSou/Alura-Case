package br.com.alura.AluraFake.exepctions;

import org.springframework.http.HttpStatus;

public class CourseFullException extends RuntimeException {

    private final HttpStatus status;

    public CourseFullException(String message, HttpStatus status) {
        super(message);
        this.status = status;
    }

    public static CourseFullException bedRequest(String message) {
        return new CourseFullException(message, HttpStatus.BAD_REQUEST);
    }

    public static CourseFullException notFound(String message) {
        return new CourseFullException(message, HttpStatus.NOT_FOUND);
    }

    public HttpStatus getStatus() {
        return status;
    }
}

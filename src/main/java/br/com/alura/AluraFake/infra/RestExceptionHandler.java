package br.com.alura.AluraFake.infra;

import br.com.alura.AluraFake.exepctions.CourseFullException;
import br.com.alura.AluraFake.exepctions.TaskFullException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.logging.Logger;

@RestControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {
    private static final Logger log = Logger.getLogger(RestExceptionHandler.class.getName());
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_YELLOW = "\u001B[33m";

    @ExceptionHandler(TaskFullException.class)
    private ResponseEntity<RestErrorMessage> taskHandlerException(TaskFullException exception) {
        log.warning(
                ANSI_RED
                    + "A possible error might be occurring inside the TaskService: "
                    + ANSI_YELLOW + exception.getMessage()
                    + ANSI_RESET
        );
        RestErrorMessage errorMessage = new RestErrorMessage(
            exception.getStatus(),
            exception.getMessage()
    );
        return ResponseEntity
                .status(exception.getStatus())
                .body(errorMessage);
    }

    @ExceptionHandler(CourseFullException.class)
    public ResponseEntity<RestErrorMessage> courseHandleException(CourseFullException exception) {
        log.warning(
                ANSI_RED
                    + "A possible error might be occurring inside the CourseService or in areas where the course data is required:  "
                    + ANSI_YELLOW + exception.getMessage()
                    + ANSI_RESET
        );
        RestErrorMessage errorMessage = new RestErrorMessage(
                exception.getStatus(),
                exception.getMessage()
        );
        return ResponseEntity
                .status(exception.getStatus())
                .body(errorMessage);
    }


}

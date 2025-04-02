package it.florenceConsulting.exception.handler;

import it.florenceConsulting.exception.BadRequestException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j(topic = "CUSTOM_EXCEPTION_HANDLER")
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public final ResponseEntity<ErrorResponse> handleBadRequestException(BadRequestException exception) {
        return handleAndGetResponseEntity(ExceptionMapperHandlers.BAD_REQUEST_EXCEPTION_HANDLER, exception);
    }

    private static ResponseEntity<ErrorResponse> handleAndGetResponseEntity(
            ExceptionMapperHandler exceptionMapperHandler, Exception exception) {
        ErrorResponse errorResponse = exceptionMapperHandler.handleExceptionAndGetErrorResponse(exception);
        return  ResponseEntity.status(errorResponse.getCode()).body(errorResponse);
    }
}

package it.florenceConsulting.exception.handler;

public interface ExceptionMapperHandler {

    ErrorResponse handleExceptionAndGetErrorResponse(Exception exception);
}

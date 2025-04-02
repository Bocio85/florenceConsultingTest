package it.florenceConsulting.exception.handler;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j(topic = "EXCEPTION_MAPPER_HANDLERS")
@NoArgsConstructor
public class ExceptionMapperHandlers {

    public static final ExceptionMapperHandler BAD_REQUEST_EXCEPTION_HANDLER = exception -> {
        log.error(exception.getMessage());
        return new ErrorResponse(
                HttpStatus.BAD_REQUEST.value(),
                HttpStatus.BAD_REQUEST.name(),
                exception.getMessage()
        );
    };
}

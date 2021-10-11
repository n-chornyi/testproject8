package org.example.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.wrap;
import static org.apache.commons.lang3.StringUtils.SPACE;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetails handleNotFoundValue(APIException exception) {
        return ExceptionDetails.builder()
                .message(exception.getMessage())
                .cause(ExceptionCause.UNCAUGHT_EXCEPTION)
                .build();
    }

    @ExceptionHandler(ErrorAuth.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ExceptionDetails handleErrorAuth(APIException exception) {
        return ExceptionDetails.builder()
                .message(exception.getMessage())
                .cause(ExceptionCause.ERROR_AUTH)
                .build();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public FieldExceptionDetails handleIncorrectValue(BindException exception) {

        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();

        return FieldExceptionDetails.builder()
                .message(fieldErrors
                        .stream()
                        .map(fieldError ->
                                wrap(fieldError.getField(), "'") +
                                        SPACE +
                                        fieldError.getDefaultMessage())
                        .collect(Collectors.joining(", ")))
                .field(fieldErrors
                        .stream()
                        .map(FieldError::getField)
                        .toArray(String[]::new))
                .cause(ExceptionCause.INCORRECT_VALUE)
                .build();
    }

    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ExceptionDetails handleUncaughtException(Exception exception) {
        return ExceptionDetails.builder()
                .message(exception.getMessage())
                .cause(ExceptionCause.UNCAUGHT_EXCEPTION)
                .build();
    }
}

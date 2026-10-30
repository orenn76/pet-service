package com.ninyo.pet.controller;

import com.ninyo.pet.exception.PetException;
import com.ninyo.pet.exception.PetNotFoundException;
import com.ninyo.pet.model.ErrorMessage;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Slf4j
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    /* Default handler for any uncaught exception */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorMessage> handleAnyException(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(PetException.class)
    public ResponseEntity<ErrorMessage> handlePetException(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorMessage> handleIllegalArgument(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PetNotFoundException.class)
    public ResponseEntity<ErrorMessage> handlePetNotFoundException(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ResponseEntity<ErrorMessage> handleOptimisticLockingFailure(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorMessage> handleAccessDenied(Exception ex, HttpServletRequest req) {
        return buildResponse(ex, req, HttpStatus.FORBIDDEN);
    }

    @Override
    protected ResponseEntity<Object> handleExceptionInternal(
            Exception ex,
            @Nullable Object body,
            HttpHeaders headers,
            HttpStatusCode statusCode,
            WebRequest request) {

        HttpServletRequest req = ((ServletWebRequest) request).getRequest();
        return new ResponseEntity<>(
                buildErrorMessage(ex, req, statusCode.value()),
                headers,
                statusCode
        );
    }

    private ResponseEntity<ErrorMessage> buildResponse(Exception ex, HttpServletRequest req, HttpStatus status) {
        return new ResponseEntity<>(
                buildErrorMessage(ex, req, status.value()),
                status
        );
    }

    private ErrorMessage buildErrorMessage(Exception ex, HttpServletRequest req, int status) {
        String uri = req.getRequestURI();
        if (req.getQueryString() != null) {
            uri += '?' + req.getQueryString();
        }

        log.error("Request URI: {}, method: {}, status: {}", uri, req.getMethod(), status, ex);

        return ErrorMessage.builder()
                .timestamp(generateTimestamp())
                .status(status)
                .error(HttpStatus.valueOf(status).getReasonPhrase())
                .exception(ex.getClass().getName())
                .message(ex.getMessage())
                .path(uri)
                .build();
    }

    private static String generateTimestamp() {
        return new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
                .format(new Date());
    }
}
package thapo.pocspring.infrastructure.rest;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import thapo.pocspring.infrastructure.error.AppException;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@RestControllerAdvice
public class GlobalControllerExceptionHandler {
    @ExceptionHandler(AppException.class)
    public ResponseEntity<ErrorResponseDto> handleException(final AppException e, final HttpServletRequest request) {
        final ErrorResponseDto errorResponseDto = new ErrorResponseDto(OffsetDateTime.now(ZoneOffset.UTC), e.getHttpStatus().value(), request.getContextPath() + request.getServletPath(), e.getErrors());
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(errorResponseDto);
    }
}

package thapo.pocspring.infrastructure.error;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class AppException extends RuntimeException {
    final List<String> errors;

    final HttpStatus httpStatus;

    public AppException(final Exception e) {
        super(e);
        this.errors = null;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public AppException(final List<String> errors) {
        this(errors, HttpStatus.UNPROCESSABLE_ENTITY);
    }

    public AppException(final List<String> errors, final HttpStatus httpStatus) {
        this.errors = errors;
        this.httpStatus = httpStatus;
    }


}

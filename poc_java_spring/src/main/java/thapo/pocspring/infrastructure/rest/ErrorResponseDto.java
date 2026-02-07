package thapo.pocspring.infrastructure.rest;

import java.time.OffsetDateTime;
import java.util.List;

public record ErrorResponseDto(OffsetDateTime timestamp, int status, String path, List<String> errors) {
}

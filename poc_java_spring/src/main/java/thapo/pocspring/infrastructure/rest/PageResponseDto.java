package thapo.pocspring.infrastructure.rest;

public record PageResponseDto(int pageNumber, int pageSize, int resultCount, long totalCount) {
}

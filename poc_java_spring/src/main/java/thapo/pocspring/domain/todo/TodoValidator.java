package thapo.pocspring.domain.todo;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TodoValidator {

    public Long validateId(final Long id, final List<String> errors) {
        if (id == null) {
            errors.add("id cannot be empty");
        }
        return id;
    }

    public String validateTitle(final String title, final List<String> errors) {
        final String titleStripped = title != null ? title.strip() : null;
        if (titleStripped == null || titleStripped.isEmpty()) {
            errors.add("title cannot be empty");
        }
        if (titleStripped != null) {
            if (titleStripped.length() > 200) {
                errors.add(String.format("title=%s has greater length than 200", titleStripped));
            }
        }
        return titleStripped;
    }

    public String validateDescription(final String description, final List<String> errors) {
        final String descriptionStripped = description != null ? description.strip() : null;
        if (descriptionStripped != null) {
            if (descriptionStripped.length() > 2000) {
                errors.add(String.format("description=%s has greater length than 2000", descriptionStripped));
            }
        }
        return descriptionStripped;
    }

    public OffsetDateTime validateDueDate(final OffsetDateTime dueDate, final List<String> errors) {
        if (dueDate != null) {
            if (dueDate.isAfter(OffsetDateTime.of(LocalDateTime.of(2300, 12, 31, 23, 59, 59), ZoneOffset.UTC))
                    || dueDate.isBefore(OffsetDateTime.of(LocalDateTime.of(2025, 1, 1, 0, 0, 0), ZoneOffset.UTC))) {
                errors.add(String.format("dueDate=%s must be between 2025-01-01T00:00:00 and 2300-12-31T23:59:59", dueDate));
            }
        }
        return dueDate;
    }
}

package thapo.pocspring.domain.todo;

import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class TodoDto {
    private Long id;
    private String title;
    private String description;
    private OffsetDateTime dueDate;

    public static TodoDto fromTodo(final Todo todo) {
        final TodoDto todoDto = new TodoDto();
        todoDto.setId(todo.getId());
        todoDto.setTitle(todo.getTitle());
        todoDto.setDescription(todo.getDescription());
        todoDto.setDueDate(todo.getDueDate());
        return todoDto;
    }
}

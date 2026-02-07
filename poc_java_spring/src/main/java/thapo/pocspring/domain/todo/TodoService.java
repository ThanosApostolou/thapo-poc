package thapo.pocspring.domain.todo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thapo.pocspring.domain.user.UserDetails;

import java.time.OffsetDateTime;
import java.util.List;

@Service
public class TodoService {
    private final TodoValidator todoValidator;
    private final TodoRepository todoRepository;

    @Autowired
    public TodoService(final TodoValidator todoValidator,
                       TodoRepository todoRepository) {
        this.todoValidator = todoValidator;
        this.todoRepository = todoRepository;
    }


    public Todo createNew(final UserDetails userDetails, final TodoDto todoDto, final List<String> errors) {
        if (todoDto == null) {
            errors.add("todo cannot be null");
            return null;
        }
        final String title = todoValidator.validateTitle(todoDto.getTitle(), errors);
        final String description = todoValidator.validateDescription(todoDto.getDescription(), errors);
        final OffsetDateTime dueDate = todoValidator.validateDueDate(todoDto.getDueDate(), errors);
        if (!errors.isEmpty()) {
            return null;
        }

        final Todo todo = new Todo();
        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(dueDate);
        todo.setUser(userDetails.user());
        return todo;
    }


    public Todo updateTodo(final UserDetails userDetails, final TodoDto todoDto, final List<String> errors) {
        if (todoDto == null) {
            errors.add("todo cannot be null");
            return null;
        }
        final Long id = todoDto.getId();
        if (id == null) {
            errors.add("id cannot be null");
            return null;
        }
        final String title = todoValidator.validateTitle(todoDto.getTitle(), errors);
        final String description = todoValidator.validateDescription(todoDto.getDescription(), errors);
        final OffsetDateTime dueDate = todoValidator.validateDueDate(todoDto.getDueDate(), errors);
        final Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            errors.add(String.format("could not find todo with id=%s", id));
        }
        if (todo != null && !todo.getUser().getId().equals(userDetails.user().getId())) {
            errors.add(String.format("todo with id=%s does not belong to user with id=%s", id, userDetails.user().getId()));
        }
        if (!errors.isEmpty()) {
            return null;
        }

        todo.setTitle(title);
        todo.setDescription(description);
        todo.setDueDate(dueDate);
        return todo;
    }


    public void delete(final UserDetails userDetails, final long id, final List<String> errors) {
        if (id < 0) {
            errors.add(String.format("id=%s should be greater than 0", id));
            return;
        }
        final Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            errors.add(String.format("could not find todo with id=%s", id));
        }
        if (todo != null && !todo.getUser().getId().equals(userDetails.user().getId())) {
            errors.add(String.format("todo with id=%s does not belong to user with id=%s", id, userDetails.user().getId()));
        }
        if (!errors.isEmpty()) {
            return;
        }
        todoRepository.deleteById(id);
    }
}

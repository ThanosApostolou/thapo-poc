package thapo.pocspring.web.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import thapo.pocspring.domain.todo.Todo;
import thapo.pocspring.domain.todo.TodoDto;
import thapo.pocspring.domain.todo.TodoRepository;
import thapo.pocspring.infrastructure.error.AppException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchTodoAction {
    private final TodoRepository todoRepository;

    public record FetchTodoResDto(TodoDto todo) {
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public FetchTodoResDto fetchTodo(final long id) {
        final List<String> errors = new ArrayList<>();
        if (id < 0) {
            errors.add(String.format(String.format("id=%s should be greater than 0", id)));
            throw new AppException(errors);
        }
        final Todo todo = todoRepository.findById(id).orElse(null);
        if (todo == null) {
            errors.add(String.format(String.format("could not find todo with id=%s", id)));
            throw new AppException(errors);
        }
        return new FetchTodoResDto(TodoDto.fromTodo(todo));
    }
}

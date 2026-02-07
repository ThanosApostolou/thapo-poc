package thapo.pocspring.web.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import thapo.pocspring.domain.todo.Todo;
import thapo.pocspring.domain.todo.TodoDto;
import thapo.pocspring.domain.todo.TodoRepository;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchTodosAction {
    private final TodoRepository todoRepository;

    public record FetchTodosResDto(List<TodoDto> todos) {
    }

    @Transactional(isolation = Isolation.READ_COMMITTED, readOnly = true)
    public FetchTodosResDto fetchTodos() {
        final Page<Todo> todoPage = todoRepository.findAll(PageRequest.of(0, 100, Sort.by(Sort.Order.asc("dueDate"))));

        final Iterable<Todo> todos = todoPage;
        final List<TodoDto> todoDtos = StreamSupport.stream(todos.spliterator(), false)
                .map(TodoDto::fromTodo)
                .toList();
        return new FetchTodosResDto(todoDtos);
    }
}

package thapo.pocspring.web.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import thapo.pocspring.domain.todo.Todo;
import thapo.pocspring.domain.todo.TodoDto;
import thapo.pocspring.domain.todo.TodoRepository;
import thapo.pocspring.domain.todo.TodoService;
import thapo.pocspring.domain.user.UserDetails;
import thapo.pocspring.domain.user.UserService;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;
import thapo.pocspring.infrastructure.error.AppException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class UpdateTodoAction {

    private final TodoRepository todoRepository;
    private final TodoService todoService;
    private final UserService userService;


    public record UpdateTodoResDto(TodoDto todo) {
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public UpdateTodoResDto updateTodo(final CustomOAuth2AuthenticatedPrincipalI principal, final TodoDto todoDto) {
        final List<String> errors = new ArrayList<>();
        final UserDetails userDetails = userService.findOrCreateUserBySub(principal);
        final Todo todo = todoService.updateTodo(userDetails, todoDto, errors);
        if (!errors.isEmpty() || todo == null) {
            throw new AppException(errors);
        }
        todoRepository.save(todo);
        final TodoDto updatedTodo = TodoDto.fromTodo(todo);
        return new UpdateTodoResDto(updatedTodo);
    }

}

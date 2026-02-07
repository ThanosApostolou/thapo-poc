package thapo.pocspring.web.api.todo;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
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
public class DeleteTodoAction {

    private final TodoRepository todoRepository;
    private final TodoService todoService;
    private final UserService userService;


    public record DeleteTodoResDto(boolean success) {
    }

    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public DeleteTodoResDto deleteTodo(final CustomOAuth2AuthenticatedPrincipalI principal, final long id) {
        final List<String> errors = new ArrayList<>();
        final UserDetails userDetails = userService.findOrCreateUserBySub(principal);
        todoService.delete(userDetails, id, errors);
        if (!errors.isEmpty()) {
            throw new AppException(errors);
        }
        return new DeleteTodoResDto(true);
    }

}

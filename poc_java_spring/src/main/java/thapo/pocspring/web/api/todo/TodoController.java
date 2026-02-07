package thapo.pocspring.web.api.todo;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import thapo.pocspring.domain.todo.TodoDto;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;
import thapo.pocspring.web.api.ApiController;

@RestController
@RequestMapping(TodoController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class TodoController {
    public static final String PATH = ApiController.PATH + "/todo";

    private final FetchTodosAction fetchTodosAction;
    private final FetchTodoAction fetchTodoAction;
    private final CreateTodoAction createTodoAction;
    private final DeleteTodoAction deleteTodoAction;
    private final UpdateTodoAction updateTodoAction;

    @GetMapping(path = "/fetch_todos", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FetchTodosAction.FetchTodosResDto> fetchTodos() {
        return ResponseEntity.ok()
                .body(fetchTodosAction.fetchTodos());
    }

    @GetMapping(path = "/fetch_todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FetchTodoAction.FetchTodoResDto> fetchTodo(@RequestParam long id) {
        return ResponseEntity.ok()
                .body(fetchTodoAction.fetchTodo(id));
    }


    @PostMapping(path = "/create_todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateTodoAction.CreateTodoResDto> createTodo(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipalI principal, @RequestBody final TodoDto todoDto) {
        return ResponseEntity.ok()
                .body(createTodoAction.createTodo(principal, todoDto));
    }


    @DeleteMapping(path = "/delete_todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<DeleteTodoAction.DeleteTodoResDto> deleteTodo(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipalI principal, @RequestParam final long id) {
        return ResponseEntity.ok()
                .body(deleteTodoAction.deleteTodo(principal, id));
    }

    @PutMapping(path = "/update_todo", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateTodoAction.UpdateTodoResDto> updateTodo(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipalI principal, @RequestBody final TodoDto todoDto) {
        return ResponseEntity.ok()
                .body(updateTodoAction.updateTodo(principal, todoDto));
    }
}

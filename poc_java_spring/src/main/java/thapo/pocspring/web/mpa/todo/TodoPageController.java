package thapo.pocspring.web.mpa.todo;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import thapo.pocspring.domain.todo.TodoDto;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;
import thapo.pocspring.infrastructure.auth.Roles;
import thapo.pocspring.web.api.todo.CreateTodoAction;
import thapo.pocspring.web.api.todo.DeleteTodoAction;
import thapo.pocspring.web.api.todo.FetchTodosAction;
import thapo.pocspring.web.mpa.MpaController;

@Controller
@RequestMapping(TodoPageController.PATH)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Slf4j
public class TodoPageController {
    public static final String PATH = MpaController.PATH + "/todo";

    private final FetchTodosAction fetchTodosAction;
    private final DeleteTodoAction deleteTodoAction;
    private final CreateTodoAction createTodoAction;


    @GetMapping("")
    @PreAuthorize("hasRole('" + Roles.SIMPLE + "')")
    public String todoPage(@AuthenticationPrincipal final OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal, Model model) {
        model.addAttribute("name", oAuth2AuthenticatedPrincipal != null ? oAuth2AuthenticatedPrincipal.getName() : null);
        final FetchTodosAction.FetchTodosResDto fetchTodosResDto = fetchTodosAction.fetchTodos();
        model.addAttribute("todos", fetchTodosResDto.todos());
        return "todo/index";
    }

    @GetMapping("/create_todo")
    @PreAuthorize("hasRole('" + Roles.SIMPLE + "')")
    public String createTodoPage(@AuthenticationPrincipal final OAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal, Model model) {
        model.addAttribute("name", oAuth2AuthenticatedPrincipal != null ? oAuth2AuthenticatedPrincipal.getName() : null);
        return "todo/create_todo";
    }

    @PostMapping(path = "/create_todo")
    @PreAuthorize("hasRole('" + Roles.SIMPLE + "')")
    public ResponseEntity<CreateTodoAction.CreateTodoResDto> createTodo(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipalI principal, @RequestBody final TodoDto todoDto) {
        final CreateTodoAction.CreateTodoResDto createTodoResDto = createTodoAction.createTodo(principal, todoDto);
        return ResponseEntity.ok()
                .body(createTodoAction.createTodo(principal, todoDto));
    }

    @PostMapping(path = "/delete_todo")
    @PreAuthorize("hasRole('" + Roles.SIMPLE + "')")
    public String deleteTodo(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipalI principal, @RequestParam final long id) {
        final DeleteTodoAction.DeleteTodoResDto deleteTodoResDto = deleteTodoAction.deleteTodo(principal, id);
        return "todo/index";
    }

}

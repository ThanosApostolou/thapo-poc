package thapo.pocspring.application.graphql.api;

import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import thapo.pocspring.application.graphql.generated.types.Book;
import thapo.pocspring.infrastructure.auth.Roles;

import java.util.List;

@Controller
public class ApiGraphqlController {

    private static final List<Book> BOOKS = List.of(
            new Book("1", "The Great Gatsby", "F. Scott Fitzgerald"),
            new Book("2", "To Kill a Mockingbird", "Harper Lee"),
            new Book("3", "1984", "George Orwell")
    );

    @QueryMapping
    @PreAuthorize("isAuthenticated()")
    public List<Book> books() {
        return BOOKS;
    }

    @QueryMapping
    @PreAuthorize("hasRole('" + Roles.SIMPLE + "a')")
    public Book bookById(@Argument String id) {
        return BOOKS.stream()
                .filter(book -> book.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

}


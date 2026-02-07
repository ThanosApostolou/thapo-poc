package thapo.pocspring.domain.todo;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import thapo.pocspring.domain.user.User;

import java.time.OffsetDateTime;

@Entity(name = "todo")
@Getter
@Setter
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;
    @Column(name = "title", length = 100, nullable = false)
    private String title;
    @Column(name = "description", length = 2000, nullable = true)
    private String description;
    @Column(name = "due_date", nullable = true)
    private OffsetDateTime dueDate;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_fk", nullable = false, referencedColumnName = "id")
    private User user;
}

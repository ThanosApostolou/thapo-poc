package thapo.pocspring.domain.todo;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

public interface TodoRepository extends TodoCustomRepository, CrudRepository<Todo, Long>, PagingAndSortingRepository<Todo, Long> {
}

interface TodoCustomRepository {

}

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class TodoRepositoryImpl implements TodoCustomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;

//    public
}

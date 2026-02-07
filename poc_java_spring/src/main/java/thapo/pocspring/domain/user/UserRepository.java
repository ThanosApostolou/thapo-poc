package thapo.pocspring.domain.user;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Optional;

public interface UserRepository extends UserCustomRepository, CrudRepository<User, Long>, PagingAndSortingRepository<User, Long> {
}

interface UserCustomRepository {
    Optional<User> findBySubEM(final String sub);

    Optional<User> findBySubJdbc(final String sub);

}

@Repository
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
class UserRepositoryImpl implements UserCustomRepository {

    private final JdbcTemplate jdbcTemplate;
    private final EntityManager entityManager;


    @Override
    public Optional<User> findBySubEM(final String sub) {
        final TypedQuery<User> query = entityManager.createQuery("""
                SELECT u \
                FROM users u \
                WHERE u.sub = :sub \
                """, User.class);
        query.setParameter("sub", sub);
        final User user = query.getSingleResultOrNull();
        return user == null ? Optional.empty() : Optional.of(user);
    }


    @Override
    public Optional<User> findBySubJdbc(final String sub) {
        final User user = DataAccessUtils.singleResult(jdbcTemplate.queryForList("""
                SELECT u.* \
                FROM users u \
                WHERE u.sub = ? \
                """, User.class, sub));
        return user == null ? Optional.empty() : Optional.of(user);
    }

}

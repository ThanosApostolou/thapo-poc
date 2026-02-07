package thapo.pocspring.domain.user;

import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public @NonNull UserDetails findOrCreateUserBySub(final @NonNull CustomOAuth2AuthenticatedPrincipalI principal) {
        final String sub = principal.getName();
        final Optional<User> userOpt = userRepository.findBySubEM(sub);
        if (userOpt.isPresent()) {
            return new UserDetails(userOpt.get(), principal);
        } else {
            final User newUser = new User();
            newUser.setSub(sub);
            newUser.setEmail(principal.getEmail());
            final LocalDateTime nowUtc = LocalDateTime.ofInstant(Instant.now(), ZoneOffset.UTC);
            newUser.setCreatedAt(nowUtc);
            newUser.setUpdatedAt(nowUtc);
            final User user = userRepository.save(newUser);
            return new UserDetails(user, principal);
        }
    }
}

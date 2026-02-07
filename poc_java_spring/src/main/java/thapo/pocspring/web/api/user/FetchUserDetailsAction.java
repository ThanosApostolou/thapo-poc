package thapo.pocspring.web.api.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thapo.pocspring.domain.user.UserDetails;
import thapo.pocspring.domain.user.UserService;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipal;

import java.time.LocalDateTime;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchUserDetailsAction {
    private final UserService userService;

    public record FetchUserDetailsDto(String username, String email, Set<String> roles, LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {
    }

    public FetchUserDetailsDto fetchUserDetails(final CustomOAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
        log.info("oAuth2AuthenticatedPrincipal={}", oAuth2AuthenticatedPrincipal);
        final UserDetails userDetails = userService.findOrCreateUserBySub(oAuth2AuthenticatedPrincipal);
        return new FetchUserDetailsDto(oAuth2AuthenticatedPrincipal.getUsername(), oAuth2AuthenticatedPrincipal.getEmail(), oAuth2AuthenticatedPrincipal.getRoles(), userDetails.user().getCreatedAt(), userDetails.user().getUpdatedAt());
    }
}

package thapo.pocspring.web.api.user;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thapo.pocspring.domain.user.UserDetails;
import thapo.pocspring.domain.user.UserService;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class FetchUserDetailsAction {
    private final UserService userService;

    public FetchUserDetailsDto fetchUserDetails(final CustomOAuth2AuthenticatedPrincipalI oAuth2AuthenticatedPrincipal) {
        log.info("oAuth2AuthenticatedPrincipal={}", oAuth2AuthenticatedPrincipal);
        final UserDetails userDetails = userService.findOrCreateUserBySub(oAuth2AuthenticatedPrincipal);
        return new FetchUserDetailsDto(oAuth2AuthenticatedPrincipal.getName(), oAuth2AuthenticatedPrincipal.getEmail(), oAuth2AuthenticatedPrincipal.getAuthorities().stream().map(Object::toString).collect(Collectors.toSet()), userDetails.user().getCreatedAt(), userDetails.user().getUpdatedAt());
    }

    public record FetchUserDetailsDto(String username, String email, Set<String> roles, LocalDateTime createdAt,
                                      LocalDateTime updatedAt) {
    }
}

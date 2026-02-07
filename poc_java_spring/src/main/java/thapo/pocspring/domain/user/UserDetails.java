package thapo.pocspring.domain.user;

import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipalI;

public record UserDetails(User user, CustomOAuth2AuthenticatedPrincipalI principal) {
}

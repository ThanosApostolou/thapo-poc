package thapo.pocspring.infrastructure.auth;

import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;

public interface CustomOAuth2AuthenticatedPrincipalI extends OAuth2AuthenticatedPrincipal {
    String getSub();

    String getEmail();
}

package thapo.pocspring.infrastructure.auth;

import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.oauth2.server.resource.introspection.SpringOpaqueTokenIntrospector;

public class CustomOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final SpringOpaqueTokenIntrospector springOpaqueTokenIntrospector;

    public CustomOpaqueTokenIntrospector(String introspectionUri, String clientId, String clientSecret) {
        springOpaqueTokenIntrospector = SpringOpaqueTokenIntrospector
                .withIntrospectionUri(introspectionUri)
                .clientId(clientId).clientSecret(clientSecret).build();
    }

    @Override
    public CustomOAuth2AuthenticatedPrincipal introspect(final String token) {
        final OAuth2IntrospectionAuthenticatedPrincipal oAuth2AuthenticatedPrincipal = (OAuth2IntrospectionAuthenticatedPrincipal) springOpaqueTokenIntrospector.introspect(token);
        return new CustomOAuth2AuthenticatedPrincipal(oAuth2AuthenticatedPrincipal);
    }
}

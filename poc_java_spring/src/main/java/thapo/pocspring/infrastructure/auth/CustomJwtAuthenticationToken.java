package thapo.pocspring.infrastructure.auth;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

public class CustomJwtAuthenticationToken extends JwtAuthenticationToken {
    public CustomJwtAuthenticationToken(CustomJwt customJwt, Collection<? extends GrantedAuthority> authorities) {
        super(customJwt, authorities);
    }
}

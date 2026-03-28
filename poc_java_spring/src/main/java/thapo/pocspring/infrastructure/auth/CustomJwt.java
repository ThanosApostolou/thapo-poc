package thapo.pocspring.infrastructure.auth;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;

@EqualsAndHashCode(callSuper = true)
public class CustomJwt extends Jwt implements CustomOAuth2AuthenticatedPrincipalI {
    @Getter
    private Collection<? extends GrantedAuthority> authorities;

    public CustomJwt(String tokenValue, Instant issuedAt, Instant expiresAt, Map<String, Object> headers, Map<String, Object> claims, Collection<? extends GrantedAuthority> authorities) {
        super(tokenValue, issuedAt, expiresAt, headers, claims);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return getClaims();
    }

    @Override
    public String getName() {
        return getClaimAsString("name");
    }

    @Override
    public String getSub() {
        return getSubject();
    }

    @Override
    public String getEmail() {
        return getClaimAsString("email");
    }
}

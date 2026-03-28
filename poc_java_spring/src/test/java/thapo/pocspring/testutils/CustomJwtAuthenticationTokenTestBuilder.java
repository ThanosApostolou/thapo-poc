package thapo.pocspring.testutils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import thapo.pocspring.infrastructure.auth.CustomJwt;
import thapo.pocspring.infrastructure.auth.CustomJwtAuthenticationToken;

import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class CustomJwtAuthenticationTokenTestBuilder {
    private String tokenValue;
    private Instant issuedAt;
    private Instant expiresAt;
    private Map<String, Object> headers;
    private Map<String, Object> claims;
    private Collection<? extends GrantedAuthority> authorities;

    public CustomJwtAuthenticationTokenTestBuilder() {
        tokenValue = "test-token";
        issuedAt = Instant.now();
        expiresAt = Instant.now().plusSeconds(3600);
        headers = new HashMap<>(Map.of("alg", "RS256"));
        claims = new HashMap<>(Map.of(
                "sub", "test-user",
                "iss", "https://issuer.example.com",
                "iat", issuedAt.getEpochSecond(),
                "exp", expiresAt.getEpochSecond()
        ));
        authorities = new HashSet<>();
    }

    public CustomJwtAuthenticationToken build() {
        CustomJwt customJwt = new CustomJwt(tokenValue, issuedAt, expiresAt, headers, claims, authorities);
        return new CustomJwtAuthenticationToken(customJwt, authorities);
    }
}

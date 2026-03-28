package thapo.pocspring.testutils;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@Accessors(chain = true)
public class JwtTestBuilder {
    private String tokenValue;
    private Instant issuedAt;
    private Instant expiresAt;
    private Map<String, Object> headers;
    private Map<String, Object> claims;

    public JwtTestBuilder() {
        this.tokenValue = "test-token";
        this.issuedAt = Instant.now();
        this.expiresAt = Instant.now().plusSeconds(3600);
        this.headers = new HashMap<>(Map.of("alg", "RS256"));
        this.claims = new HashMap<>(Map.of(
                "sub", "test-user",
                "iss", "https://issuer.example.com",
                "iat", issuedAt.getEpochSecond(),
                "exp", expiresAt.getEpochSecond()
        ));
    }

    public Jwt build() {
        return new Jwt(tokenValue, issuedAt, expiresAt, headers, claims);
    }
}

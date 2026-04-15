package thapo.pocspring.config.security;

import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import thapo.pocspring.infrastructure.auth.CustomJwt;
import thapo.pocspring.infrastructure.auth.CustomJwtAuthenticationToken;

import java.util.*;
import java.util.stream.Collectors;

@Component
@Slf4j
public class CustomJwtAuthenticationConverter implements Converter<Jwt, CustomJwtAuthenticationToken> {
    private final Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public CustomJwtAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = findAuthorities(jwt);
        final CustomJwt customJwt = new CustomJwt(jwt.getTokenValue(), jwt.getIssuedAt(), jwt.getExpiresAt(), jwt.getHeaders(), jwt.getClaims(), authorities);
        return new CustomJwtAuthenticationToken(customJwt, authorities);
    }

    private Collection<GrantedAuthority> findAuthorities(Jwt jwt) {
        Collection<GrantedAuthority> authorities = new HashSet<>(this.jwtGrantedAuthoritiesConverter.convert(jwt));

        final Set<String> rolesSet = new HashSet<>();
        final Map<String, Object> realmAccessMap = jwt.getClaimAsMap("realm_access");
        if (realmAccessMap != null) {
            final Object rolesObj = realmAccessMap.get("roles");
            if (rolesObj != null) {
                try {
                    final List<String> roles = (List<String>) rolesObj;
                    rolesSet.addAll(roles);
                } catch (Exception e) {
                    log.warn(" could not parse roles", e);
                }
            }
        }
        Collection<GrantedAuthority> extraAuthorities = rolesSet.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toUnmodifiableSet());
        authorities.addAll(extraAuthorities);
        return Collections.unmodifiableCollection(authorities);
    }

}

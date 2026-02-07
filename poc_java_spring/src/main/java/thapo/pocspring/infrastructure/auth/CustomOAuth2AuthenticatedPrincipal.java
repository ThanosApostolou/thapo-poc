package thapo.pocspring.infrastructure.auth;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.core.OAuth2TokenIntrospectionClaimAccessor;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class CustomOAuth2AuthenticatedPrincipal implements OAuth2TokenIntrospectionClaimAccessor, CustomOAuth2AuthenticatedPrincipalI {
    private final OAuth2AuthenticatedPrincipal delegate;
    private final Set<GrantedAuthority> authorities;

    @Getter
    private final Set<String> roles;

    public CustomOAuth2AuthenticatedPrincipal(final OAuth2AuthenticatedPrincipal delegate) {
        this.delegate = delegate;

        final Set<String> rolesSet = new HashSet<>();
        final Map<String, Object> realmAccessMap = getClaimAsMap("realm_access");
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
        this.roles = Collections.unmodifiableSet(rolesSet);

        this.authorities = rolesSet.stream().map(r -> new SimpleGrantedAuthority("ROLE_" + r)).collect(Collectors.toUnmodifiableSet());
    }

    @Override
    public <A> A getAttribute(String name) {
        return CustomOAuth2AuthenticatedPrincipalI.super.getAttribute(name);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return delegate.getName();
    }


    @Override
    public Map<String, Object> getClaims() {
        return getAttributes();
    }

    @Override
    public String getSub() {
        return getClaimAsString("sub");
    }

    public Boolean getEmailVerified() {
        return getClaimAsBoolean("email_verified");
    }

    public String getFullName() {
        return getClaimAsString("name");
    }

    public String getPreferredUsername() {
        return getClaimAsString("preferred_username");
    }

    public String getGivenName() {
        return getClaimAsString("given_name");
    }

    public String getFamilyName() {
        return getClaimAsString("family_name");
    }

    @Override
    public String getEmail() {
        return getClaimAsString("email");
    }

    public List<String> getScopes() {
        return getClaimAsStringList("scope");
    }
}

package thapo.pocspring.infrastructure.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import java.util.*;

@Slf4j
public class CustomOidcUser implements OidcUser, CustomOAuth2AuthenticatedPrincipalI {
    private final OidcUser delegate;

    public CustomOidcUser(final OidcUser delegate) {
        this.delegate = delegate;
        final Set<String> rolesSet = new HashSet<>();
        final Map<String, Object> realmAccessMap = delegate.getClaimAsMap("realm_access");
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
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return delegate.getAuthorities();
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
    public OidcUserInfo getUserInfo() {
        return delegate.getUserInfo();
    }

    @Override
    public OidcIdToken getIdToken() {
        return delegate.getIdToken();
    }

    @Override
    public String getSub() {
        return getClaimAsString("sub");
    }

    @Override
    public String getEmail() {
        return getClaimAsString("email");
    }

    public List<String> getScopes() {
        return getClaimAsStringList("scope");
    }
}

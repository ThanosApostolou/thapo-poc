package thapo.pocspring.infrastructure.auth;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserSource;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
@Service
public class CustomOidcUserService extends OidcUserService {

    private Map<String, Object> getAccessTokenClaims(OAuth2AccessToken token) {
        try {
            JWT jwt = JWTParser.parse(token.getTokenValue());
            return jwt.getJWTClaimsSet().getClaims();
        } catch (Exception e) {
            throw new IllegalStateException("Cannot parse access token", e);
        }
    }

    private CustomOidcUser oidcUserConverter(final OidcUserSource oidcUserSource) {
        final OidcUserRequest oidcUserRequest = oidcUserSource.getUserRequest();
        final OidcUserInfo oidcUserInfo = oidcUserSource.getUserInfo();
        Set<GrantedAuthority> authorities = new LinkedHashSet<>();
        ClientRegistration.ProviderDetails providerDetails = oidcUserRequest.getClientRegistration().getProviderDetails();
        String userNameAttributeName = providerDetails.getUserInfoEndpoint().getUserNameAttributeName();
        if (StringUtils.hasText(userNameAttributeName)) {
            authorities.add(new OidcUserAuthority(oidcUserRequest.getIdToken(), oidcUserInfo, userNameAttributeName));
        } else {
            authorities.add(new OidcUserAuthority(oidcUserRequest.getIdToken(), oidcUserInfo));
        }
        final OAuth2AccessToken token = oidcUserRequest.getAccessToken();
        final Map<String, Object> accessTokenClaims = getAccessTokenClaims(token);
        if (accessTokenClaims.get("realm_access") != null) {
            try {
                final Map<String, Object> realmAccessMap = (Map<String, Object>) accessTokenClaims.get("realm_access");
                if (realmAccessMap.get("roles") != null) {
                    for (String role : (List<String>) realmAccessMap.get("roles")) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                }
            } catch (Exception e) {
                log.error("error getting authorities", e);
            }
        }
        final DefaultOidcUser defaultOidcUser;
        if (StringUtils.hasText(userNameAttributeName)) {
            defaultOidcUser = new DefaultOidcUser(authorities, oidcUserRequest.getIdToken(), oidcUserInfo, userNameAttributeName);
        } else {
            defaultOidcUser = new DefaultOidcUser(authorities, oidcUserRequest.getIdToken(), oidcUserInfo);
        }
        return new CustomOidcUser(defaultOidcUser);
    }

    @Override
    public CustomOidcUser loadUser(final OidcUserRequest userRequest) {
        log.info("CustomOidcUserService.loadUser: userRequest={}", userRequest);
        setOidcUserConverter(this::oidcUserConverter);

        // Load the user from the provider
        return (CustomOidcUser) super.loadUser(userRequest);
    }
}

package thapo.pocspring.infrastructure.auth;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizationRequestResolver;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestCustomizers;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestRedirectFilter;
import org.springframework.security.oauth2.client.web.OAuth2AuthorizationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@Slf4j
public class WebSecurityConfig {
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.introspection-uri}")
    private String introspectionUri;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-secret}")
    private String clientSecret;


    @Bean
    @Order(0)
    public OAuth2AuthorizationRequestResolver pkceResolver(ClientRegistrationRepository clientRegistrationRepository) {
        DefaultOAuth2AuthorizationRequestResolver resolver = new DefaultOAuth2AuthorizationRequestResolver(clientRegistrationRepository, OAuth2AuthorizationRequestRedirectFilter.DEFAULT_AUTHORIZATION_REQUEST_BASE_URI);
        // Responsible for enabling PKCE, to generate code verifier, code challenge
        resolver.setAuthorizationRequestCustomizer(OAuth2AuthorizationRequestCustomizers.withPkce());

        return resolver;
    }

    @Bean
    @Order(1)
    public SecurityFilterChain filterChainMpa(final HttpSecurity http, final ClientRegistrationRepository clientRegistrationRepository,
                                              final OAuth2AuthorizationRequestResolver resolver, final CustomOidcUserService customOidcUserService) throws Exception {
        // Mutli page application auth
        http
                .securityMatcher("/", "/mpa/**", "/login/**", "/logout/**", "/oauth2/**")
                .cors(Customizer.withDefaults()) // use WebMVC cors configuration
                .csrf(Customizer.withDefaults()) // use default session based csrf
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/", "/mpa", "/mpa/home", "/login/oauth2/code/**", "/login").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/mpa/**").authenticated();
                    authorizationManagerRequestMatcherRegistry.anyRequest().denyAll();
                })
                .logout(httpSecurityLogoutConfigurer ->
                        httpSecurityLogoutConfigurer.logoutSuccessHandler(oidcLogoutSuccessHandler(clientRegistrationRepository)))
                .sessionManagement(securitySessionManagementConfigurer ->
                        securitySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .oauth2Login(oAuth2LoginConfigurer ->
                        oAuth2LoginConfigurer
                                .userInfoEndpoint(userInfoEndpointConfig ->
                                        userInfoEndpointConfig
                                                .oidcUserService(customOidcUserService))
                                .authorizationEndpoint(authorizationEndpointConfig ->
                                        authorizationEndpointConfig.authorizationRequestResolver(resolver)))
                .oauth2Client(Customizer.withDefaults())
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.accessDeniedPage("/mpa/403"));
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain filterChainRest(final HttpSecurity http) throws Exception {
        // rest auth
        http
                .cors(Customizer.withDefaults()) // use WebMVC cors configuration
                .csrf(CsrfConfigurer::disable) // disable csrf
                .authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> {
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/actuator/**", "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/public_api/**", "/api/stream/**", "/public/**").permitAll();
                    authorizationManagerRequestMatcherRegistry.requestMatchers("/api/**", "/ws/**").authenticated();
                    authorizationManagerRequestMatcherRegistry.anyRequest().denyAll();
                })
                .sessionManagement(securitySessionManagementConfigurer ->
                        securitySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .oauth2ResourceServer(httpSecurityOAuth2ResourceServerConfigurer ->
                        httpSecurityOAuth2ResourceServerConfigurer
                                .opaqueToken(opaqueTokenConfigurer ->
                                        opaqueTokenConfigurer
                                                .introspector(new CustomOpaqueTokenIntrospector(introspectionUri, clientId, clientSecret))));
        return http.build();
    }

    private OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler(ClientRegistrationRepository clientRegistrationRepository) {
        OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler =
                new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);

        // Sets the location that the End-User's User Agent will be redirected to
        // after the logout has been performed at the Provider
        oidcLogoutSuccessHandler.setPostLogoutRedirectUri("{baseUrl}");

        return oidcLogoutSuccessHandler;
    }


}

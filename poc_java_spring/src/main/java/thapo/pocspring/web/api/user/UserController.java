package thapo.pocspring.web.api.user;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import thapo.pocspring.infrastructure.auth.CustomOAuth2AuthenticatedPrincipal;
import thapo.pocspring.infrastructure.auth.Roles;
import thapo.pocspring.web.api.ApiController;


@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
@RequestMapping(UserController.PATH)
public class UserController {
    public static final String PATH = ApiController.PATH + "/user";

    private final FetchUserDetailsAction fetchUserDetailsAction;

    @PreAuthorize("hasRole('" + Roles.SIMPLE + "')")
    @RequestMapping(method = RequestMethod.GET, path = "/fetch_user_details", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<FetchUserDetailsAction.FetchUserDetailsDto> fetchUserDetails(@AuthenticationPrincipal CustomOAuth2AuthenticatedPrincipal oAuth2AuthenticatedPrincipal) {
        return ResponseEntity.ok()
                .body(fetchUserDetailsAction.fetchUserDetails(oAuth2AuthenticatedPrincipal));
    }
}

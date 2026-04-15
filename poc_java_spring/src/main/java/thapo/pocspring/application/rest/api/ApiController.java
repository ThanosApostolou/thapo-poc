package thapo.pocspring.application.rest.api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(ApiController.PATH)
@Slf4j
public class ApiController {
    public static final String PATH = "/rest/api";
    public static final String PATH_GET_TEST = "/getTest";

    @GetMapping(path = PATH_GET_TEST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTest() {
        final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        log.info("Authentication: {}", auth);

        return ResponseEntity.ok()
                .body("some test message from protected api");
    }


}

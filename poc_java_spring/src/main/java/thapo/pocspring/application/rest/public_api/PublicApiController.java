package thapo.pocspring.application.rest.public_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(PublicApiController.PATH)
public class PublicApiController {
    public static final String PATH = "/rest/public_api";
    public static final String PATH_GET_TEST = "/getTest";

    @GetMapping(path = PATH_GET_TEST, produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok()
                .body("some test message");
    }
}

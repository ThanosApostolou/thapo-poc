package thapo.pocspring.web.public_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(PublicApiController.PATH)
public class PublicApiController {
    public static final String PATH = "/public_api";

    @RequestMapping(method = RequestMethod.GET, path = "/getTest", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok()
                .body("some test message");
    }
}

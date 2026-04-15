package thapo.pocspring.application.rest.public_api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.client.RestTestClient;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
class PublicApiControllerIT {

    @Autowired
    private RestTestClient restTestClient;

    @Test
    void getTest_returnsExpectedMessage() {
        restTestClient
                .get().uri(PublicApiController.PATH + "/getTest")
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType("text/plain;charset=UTF-8")
                .expectBody(String.class).isEqualTo("some test message");
    }

}

package thapo.pocspring.web.api;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.resttestclient.autoconfigure.AutoConfigureRestTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.client.RestTestClient;
import thapo.pocspring.testutils.JwtTestBuilder;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureRestTestClient
@Slf4j
class ApiControllerIT {
    private final String PATH = ApiController.PATH + "/getTest";

    @Autowired
    private RestTestClient restTestClient;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void getTest_notAuthorized() {
        restTestClient.get().uri(PATH).exchange()
                .expectStatus().isUnauthorized()
                .expectHeader().doesNotExist(HttpHeaders.CONTENT_TYPE)
                .expectBody().isEmpty();
    }

    @Test
    void getTest_returnsExpectedMessage() {
        when(jwtDecoder.decode(anyString())).thenReturn(new JwtTestBuilder().build());
        restTestClient
                .get().uri(PATH)
                .headers(h -> h.setBearerAuth("test-token"))  // any value — introspector is mocked
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                .expectBody(String.class).isEqualTo("some test message from protected api");
    }

}

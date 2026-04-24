package thapo.pocspring.application.rest.public_api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import thapo.pocspring.infrastructure.auth.CustomOidcUserService;
import thapo.pocspring.testutils.CommonControllerTestConfiguration;

@WebMvcTest(PublicApiController.class)
@Import(CommonControllerTestConfiguration.class)
class PublicApiControllerTest {
    @Autowired
    private MockMvcTester mockMvcTester;
    @MockitoBean
    private CustomOidcUserService customOidcUserService;

    @Nested
    class GetTest {
        @Test
        void ok_returnsExpectedMessage() {
            mockMvcTester.get().uri(PublicApiController.PATH + "/getTest")
                    .assertThat()
                    .hasStatus(HttpStatus.OK)
                    .hasContentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    .bodyText().isEqualTo("some test message");
        }
    }
}
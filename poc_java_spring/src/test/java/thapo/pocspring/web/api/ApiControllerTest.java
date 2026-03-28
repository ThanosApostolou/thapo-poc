package thapo.pocspring.web.api;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;
import thapo.pocspring.infrastructure.auth.CustomOidcUserService;
import thapo.pocspring.testutils.CommonControllerTestConfiguration;
import thapo.pocspring.testutils.CustomJwtAuthenticationTokenTestBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.authentication;

@WebMvcTest(ApiController.class)
@Import(CommonControllerTestConfiguration.class)
class ApiControllerTest {
    private final String PATH = "/api/getTest";

    @Autowired
    private MockMvcTester mockMvcTester;
    @MockitoBean
    private CustomOidcUserService customOidcUserService;

    @Nested
    class GetTest {
        @Test
        void unauthorized_noAuthentication() {
            var result = mockMvcTester.get().uri("/api/getTest").exchange();
            assertThat(result)
                    .hasStatus(HttpStatus.UNAUTHORIZED)
                    .doesNotContainHeader(HttpHeaders.CONTENT_TYPE)
                    .body().isEmpty();
        }

        @Test
        void ok_returnsExpectedMessage() {
            var result = mockMvcTester.get().uri("/api/getTest")
                    .with(authentication(new CustomJwtAuthenticationTokenTestBuilder().build())).exchange();
            assertThat(result)
                    .hasStatus(HttpStatus.OK)
                    .hasContentTypeCompatibleWith(MediaType.TEXT_PLAIN)
                    .bodyText().isEqualTo("some test message from protected api");
        }
    }
}


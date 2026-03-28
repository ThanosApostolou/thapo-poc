package thapo.pocspring.web.public_api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

@WebMvcTest(PublicApiController.class)
class PublicApiControllerTest {
    @Autowired
    private MockMvcTester mockMvcTester;

//    @Test
//    void getTest_returnsExpectedMessage() {
//        mockMvcTester.get().uri(PublicApiController.PATH + "/getTest")
//                .with(authentication(new OAuth2AuthenticationToken(
//                        new CustomOAuth2AuthenticatedPrincipal(new OAuth2IntrospectionAuthenticatedPrincipal()), List.of(), "null"
//                )))
//                .assertThat()
//                .hasStatus(HttpStatus.OK)
//                .hasContentType(MediaType.TEXT_PLAIN)
//                .bodyText().isEqualTo("some test message");
//    }
}
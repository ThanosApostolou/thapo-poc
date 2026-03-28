package thapo.pocspring.testutils;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;
import thapo.pocspring.infrastructure.auth.WebSecurityConfig;

@TestConfiguration
@Import(WebSecurityConfig.class)
public class CommonControllerTestConfiguration {
}

package thapo.pocspring.application.graphql.public_api;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class PublicApiGraphqlController {

    @QueryMapping
    public String hello(@Argument String name) {
        return "Hello, " + (name != null ? name : "World") + "!";
    }

    @QueryMapping
    public String getTest() {
        return "some test message";
    }
}

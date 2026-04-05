package thapo.pocjakarta.application.rest.public_api;

import jakarta.ws.rs.core.Application;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.test.JerseyTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Unit test for {@link PublicController} using Jersey Test Framework.
 * Similar to Spring Boot's {@code @WebMvcTest} — starts a lightweight
 * in-memory HTTP server with only the controller under test registered.
 */
class PublicControllerTest extends JerseyTest {

    @Override
    protected Application configure() {
        // Register only the controller under test (like @WebMvcTest scoping)
        return new ResourceConfig(PublicController.class);
    }

    @BeforeEach
    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @AfterEach
    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    @Test
    void getTest_shouldReturnOkWithMessage() {
        Response response = target(PublicController.PATH)
                .path("getTest")
                .request(MediaType.TEXT_PLAIN)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals("some test message", response.readEntity(String.class));
    }

    @Test
    void getTest_shouldReturnTextPlainContentType() {
        Response response = target(PublicController.PATH)
                .path("getTest")
                .request(MediaType.TEXT_PLAIN)
                .get();

        assertEquals(
                MediaType.TEXT_PLAIN,
                response.getMediaType().toString()
        );
    }

    @Test
    void invalidPath_shouldReturn404() {
        Response response = target(PublicController.PATH)
                .path("nonExistent")
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
}


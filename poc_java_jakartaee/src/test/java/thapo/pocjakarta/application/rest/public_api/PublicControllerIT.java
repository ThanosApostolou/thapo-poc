package thapo.pocjakarta.application.rest.public_api;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit5.ArquillianExtension;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import thapo.pocjakarta.application.rest.RestApplication;

import java.net.URI;
import java.net.URL;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Integration test for {@link PublicController} using <b>Arquillian + Payara Managed</b>.
 * This is the Jakarta EE equivalent of Spring Boot's {@code @SpringBootTest}:
 *
 * <ul>
 *   <li>Starts a <b>real Payara 7</b> domain</li>
 *   <li>Deploys the full application (all controllers, CDI beans, EJBs, datasources)</li>
 *   <li>Runs tests via real HTTP requests against the deployed app</li>
 *   <li>Shuts down the domain after tests complete</li>
 * </ul>
 *
 * <p>Run with: {@code mvn verify} (Failsafe plugin picks up *IT.java files).
 */
@ExtendWith(ArquillianExtension.class)
@RunAsClient
class PublicControllerIT {

    /**
     * Builds the deployable WAR archive — like Spring's component scanning,
     * but explicit. ShrinkWrap packages all application classes into a
     * micro-deployment that gets deployed to the real Payara server.
     */
    @Deployment(testable = false)
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                // Include all application packages
                .addPackages(true, RestApplication.class.getPackage())
                // Enable CDI (like beans.xml discovery)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    /**
     * Arquillian injects the base URL of the deployed application.
     * e.g. http://localhost:8080/test/
     */
    @ArquillianResource
    private URL deploymentUrl;

    private Client client;

    @BeforeEach
    void setUp() {
        client = ClientBuilder.newClient();
    }

    @AfterEach
    void tearDown() {
        if (client != null) {
            client.close();
        }
    }

    // ---- PublicController endpoints ----

    @Test
    void getTest_shouldReturnOkWithExpectedBody() throws Exception {
        Response response = client.target(resolveUri("public_api/getTest"))
                .request(MediaType.TEXT_PLAIN)
                .get();

        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.TEXT_PLAIN, response.getMediaType().toString());
        assertEquals("some test message", response.readEntity(String.class));
    }

    @Test
    void getTest_shouldReturnTextPlainContentType() throws Exception {
        Response response = client.target(resolveUri("public_api/getTest"))
                .request(MediaType.TEXT_PLAIN)
                .get();

        assertNotNull(response.getMediaType());
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());
        assertEquals(MediaType.TEXT_PLAIN, response.getMediaType().toString());
    }

    // ---- Cross-controller routing (full Payara context) ----

    @Test
    void fileController_fetchImage_shouldBeRoutable() throws Exception {
        // FileController is deployed in the same WAR — route must resolve.
        // File won't exist on disk, so expect 500 (not 404).
        Response response = client.target(resolveUri("public_api/file/fetch_image"))
                .request("image/jpeg")
                .get();

        assertNotNull(response);
        assertTrue(
                response.getStatus() != Response.Status.NOT_FOUND.getStatusCode(),
                "FileController route should be registered in full Payara context"
        );
    }

    // ---- Negative cases ----

    @Test
    void unknownPath_shouldReturn404() throws Exception {
        Response response = client.target(resolveUri("does_not_exist"))
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    @Test
    void publicApi_unknownSubPath_shouldReturn404() throws Exception {
        Response response = client.target(resolveUri("public_api/nonExistent"))
                .request()
                .get();

        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }

    // ---- Helper ----

    private URI resolveUri(String path) throws Exception {
        return deploymentUrl.toURI().resolve(path);
    }
}

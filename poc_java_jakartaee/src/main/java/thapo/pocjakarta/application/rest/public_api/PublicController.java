package thapo.pocjakarta.application.rest.public_api;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path(PublicController.PATH)
public class PublicController {

    public static final String PATH = "public_api";

    @GET
    @Path("getTest")
    @Produces(MediaType.TEXT_PLAIN)
    public Response getTest() {
        return Response.ok("some test message").build();
    }
}

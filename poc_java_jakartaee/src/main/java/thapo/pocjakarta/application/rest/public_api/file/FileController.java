package thapo.pocjakarta.application.rest.public_api.file;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;
import thapo.pocjakarta.application.rest.public_api.PublicController;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Logger;

@Path(FileController.PATH)
public class FileController {

    private static final Logger log = Logger.getLogger(FileController.class.getName());

    public static final String PATH = PublicController.PATH + "/file";

    @GET
    @Path("/fetch_image")
    @Produces("image/jpeg")
    public Response fetchImage() {
        log.info("START fetchImage");
        final File file = new File("../files/public/sample-image.jpg");
        final StreamingOutput stream = buildStreamingOutput(file);
        final Response response = Response.ok(stream, "image/jpeg").build();
        log.info("END fetchImage");
        return response;
    }

    @GET
    @Path("/fetch_audio")
    @Produces("audio/mpeg")
    public Response fetchAudio() {
        log.info("START fetchAudio");
        final File file = new File("../files/public/sample-audio.mp3");
        final StreamingOutput stream = buildStreamingOutput(file);
        final Response response = Response.ok(stream, "audio/mpeg").build();
        log.info("END fetchAudio");
        return response;
    }

    @GET
    @Path("/fetch_video")
    @Produces("video/webm")
    public Response fetchVideo() {
        log.info("START fetchVideo");
        final File file = new File("../files/public/sample-video.webm");
        final StreamingOutput stream = buildStreamingOutput(file);
        final Response response = Response.ok(stream, "video/webm").build();
        log.info("END fetchVideo");
        return response;
    }

    private StreamingOutput buildStreamingOutput(final File file) {
        return outputStream -> {
            try (final FileInputStream fis = new FileInputStream(file)) {
                final byte[] buffer = new byte[8192];
                int bytesRead;
                while ((bytesRead = fis.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            } catch (IOException e) {
                throw new IOException("Failed to stream file: " + file.getPath(), e);
            }
        };
    }
}

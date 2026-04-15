package thapo.pocspring.application.rest.public_api.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thapo.pocspring.application.rest.public_api.PublicApiController;

@RestController
@RequestMapping(FileController.PATH)
@Slf4j
public class FileController {
    public static final String PATH = PublicApiController.PATH + "/file";
    public static final String PATH_FETCH_IMAGE = "/fetch_image";
    public static final String PATH_FETCH_AUDIO = "/fetch_audio";
    public static final String PATH_FETCH_VIDEO = "/fetch_video";

    @GetMapping(path = PATH_FETCH_IMAGE)
    public ResponseEntity<Resource> fetchImage() {
        log.info("START fetchImage");
        final FileSystemResource resource = new FileSystemResource("../files/public/sample-image.jpg");
        final ResponseEntity<Resource> response = ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
        log.info("END fetchImage");
        return response;
    }

    @GetMapping(path = PATH_FETCH_AUDIO)
    public ResponseEntity<Resource> fetchAudio() {
        log.info("START fetchAudio");
        final FileSystemResource resource = new FileSystemResource("../files/public/sample-audio.mp3");
        final ResponseEntity<Resource> response = ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(resource);
        log.info("END fetchAudio");
        return response;
    }

    @GetMapping(path = PATH_FETCH_VIDEO)
    public ResponseEntity<Resource> fetchVideo() {
        log.info("START fetchVideo");
        final FileSystemResource resource = new FileSystemResource("../files/public/sample-video.webm");
        final ResponseEntity<Resource> response = ResponseEntity.ok()
                .contentType(MediaType.valueOf("video/webm"))
                .body(resource);
        log.info("END fetchVideo");
        return response;
    }
}

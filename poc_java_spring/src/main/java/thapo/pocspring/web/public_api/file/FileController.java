package thapo.pocspring.web.public_api.file;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import thapo.pocspring.web.public_api.PublicApiController;

@RestController
@RequestMapping(FileController.PATH)
@Slf4j
public class FileController {
    public static final String PATH = PublicApiController.PATH + "/file";

    @GetMapping(path = "/fetch_image")
    public ResponseEntity<Resource> fetchImage() {
        log.info("START fetchImage");
        final FileSystemResource resource = new FileSystemResource("../files/public/sample-image.jpg");
        final ResponseEntity<Resource> response = ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(resource);
        log.info("END fetchImage");
        return response;
    }

    @GetMapping(path = "/fetch_audio")
    public ResponseEntity<Resource> fetchAudio() {
        log.info("START fetchAudio");
        final FileSystemResource resource = new FileSystemResource("../files/public/sample-audio.mp3");
        final ResponseEntity<Resource> response = ResponseEntity.ok()
                .contentType(MediaType.valueOf("audio/mpeg"))
                .body(resource);
        log.info("END fetchAudio");
        return response;
    }

    @GetMapping(path = "/fetch_video")
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

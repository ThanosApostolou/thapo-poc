package thapo.pocspring.web.public_api.stream;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyEmitter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import thapo.pocspring.web.public_api.PublicApiController;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping(StreamController.PATH)
@Slf4j
public class StreamController {
    public static final String PATH = PublicApiController.PATH + "/stream";

    @GetMapping(path = "/fetch_stream", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseBodyEmitter fetchStream() {
        final ResponseBodyEmitter emitter = new ResponseBodyEmitter();
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    emitter.send("Chunk " + i + "\n");
                    Thread.sleep(1000); // Simulate delay
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        return emitter;
    }

    @GetMapping(path = "/fetch_sse", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter fetchSse() {
        log.info("START fetchSse");
        SseEmitter emitter = new SseEmitter();
        CompletableFuture.runAsync(() -> {
            try {
                for (int i = 1; i <= 3; i++) {
                    log.info("emitter.send({})", "Chunk " + i);
                    emitter.send("Chunk " + i);
                    Thread.sleep(1000); // Simulate delay
                }
                emitter.complete();
            } catch (Exception e) {
                emitter.completeWithError(e);
            }
        });
        log.info("END fetchSse");
        return emitter;
    }
}

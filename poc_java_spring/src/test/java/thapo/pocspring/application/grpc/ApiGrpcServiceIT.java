package thapo.pocspring.application.grpc;

import com.google.protobuf.Empty;
import io.grpc.ManagedChannel;
import io.grpc.stub.StreamObserver;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.grpc.test.autoconfigure.AutoConfigureInProcessTransport;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.grpc.client.GrpcChannelFactory;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import thapo.pocspring.application.proto.generated.AddBookRequest;
import thapo.pocspring.application.proto.generated.ApiServiceGrpc;
import thapo.pocspring.application.proto.generated.GetBookByIdRequest;
import thapo.pocspring.application.proto.generated.HelloRequest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static java.time.Instant.now;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureInProcessTransport
class ApiGrpcServiceIT {
    @Autowired
    private GrpcChannelFactory channelFactory;
    private ManagedChannel channel;
    private ApiServiceGrpc.ApiServiceBlockingStub blockingStub;
    private ApiServiceGrpc.ApiServiceStub asyncStub;

    @BeforeEach
    void setUp() {
        channel = channelFactory.createChannel("test");
        blockingStub = ApiServiceGrpc.newBlockingStub(channel);
        asyncStub = ApiServiceGrpc.newStub(channel);
    }

    @AfterEach
    void tearDown() throws InterruptedException {
        if (channel != null) {
            channel.shutdownNow();
            channel.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    void hello_returnsExpectedMessage() {
        var response = blockingStub.hello(HelloRequest.newBuilder().build());

        assertThat(response.getMessage()).isEqualTo("Hello, World!");
    }

    @Test
    void getTest_returnsExpectedMessage() {
        var response = blockingStub.getTest(Empty.getDefaultInstance());

        assertThat(response.getMessage()).isEqualTo("some test message");
    }

    @Test
    void addBook_thenListAndFetchById_returnsCreatedBook() {
        int initialCount = blockingStub.listBooks(Empty.getDefaultInstance()).getBooksCount();

        var created = blockingStub.addBook(AddBookRequest.newBuilder()
                .setTitle("Clean Code")
                .setAuthor("Robert C. Martin")
                .build());

        assertThat(created.getId()).isNotBlank();
        assertThat(created.getTitle()).isEqualTo("Clean Code");
        assertThat(created.getAuthor()).isEqualTo("Robert C. Martin");

        var fetched = blockingStub.getBookById(GetBookByIdRequest.newBuilder()
                .setId(created.getId())
                .build());

        assertThat(fetched.hasBook()).isTrue();
        assertThat(fetched.getBook().getId()).isEqualTo(created.getId());
        assertThat(blockingStub.listBooks(Empty.getDefaultInstance()).getBooksCount()).isEqualTo(initialCount + 1);
    }

    @Test
    void bookAdded_streamsNewlyCreatedBook() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicReference<thapo.pocspring.application.proto.generated.Book> receivedBook = new AtomicReference<>();
        AtomicReference<Throwable> error = new AtomicReference<>();

        asyncStub.bookAdded(Empty.getDefaultInstance(), new StreamObserver<>() {
            @Override
            public void onNext(thapo.pocspring.application.proto.generated.Book value) {
                receivedBook.set(value);
                latch.countDown();
            }

            @Override
            public void onError(Throwable throwable) {
                error.set(throwable);
                latch.countDown();
            }

            @Override
            public void onCompleted() {
            }
        });

        Thread.sleep(200);

        var created = blockingStub.addBook(AddBookRequest.newBuilder()
                .setTitle("Domain-Driven Design")
                .setAuthor("Eric Evans")
                .build());

        assertThat(latch.await(5, TimeUnit.SECONDS)).isTrue();
        assertThat(error.get()).isNull();
        assertThat(receivedBook.get()).isNotNull();
        assertThat(receivedBook.get().getId()).isEqualTo(created.getId());
        assertThat(receivedBook.get().getTitle()).isEqualTo("Domain-Driven Design");
    }

    @TestConfiguration
    static class TestCorsConfiguration {
        @Bean
        CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.addAllowedOriginPattern("*");
            configuration.addAllowedMethod("*");
            configuration.addAllowedHeader("*");

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }

        @Bean
        JwtDecoder jwtDecoder() {
            return token -> Jwt.withTokenValue(token)
                    .header("alg", "none")
                    .claim("sub", "grpc-test-user")
                    .issuedAt(now())
                    .expiresAt(now().plusSeconds(3600))
                    .build();
        }
    }
}


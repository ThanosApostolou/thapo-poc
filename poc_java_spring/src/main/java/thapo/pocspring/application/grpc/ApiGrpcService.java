package thapo.pocspring.application.grpc;

import com.google.protobuf.Empty;
import io.grpc.Status;
import io.grpc.stub.ServerCallStreamObserver;
import io.grpc.stub.StreamObserver;
import org.springframework.stereotype.Service;
import thapo.pocspring.application.proto.generated.*;

@Service
public class ApiGrpcService extends ApiServiceGrpc.ApiServiceImplBase {

    private final PublicMessageService publicMessageService;
    private final BookCatalogService bookCatalogService;

    public ApiGrpcService(PublicMessageService publicMessageService, BookCatalogService bookCatalogService) {
        this.publicMessageService = publicMessageService;
        this.bookCatalogService = bookCatalogService;
    }

    @Override
    public void hello(HelloRequest request, StreamObserver<HelloResponse> responseObserver) {
        final String name = request.hasName() ? request.getName().getValue() : null;
        final String message = "Hello, " + (name != null ? name : "World") + "!";
        HelloResponse response = HelloResponse.newBuilder()
                .setMessage(message)
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getTest(Empty request, StreamObserver<GetTestResponse> responseObserver) {
        GetTestResponse response = GetTestResponse.newBuilder()
                .setMessage(publicMessageService.getTest())
                .build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void listBooks(Empty request, StreamObserver<ListBooksResponse> responseObserver) {
        ListBooksResponse.Builder response = ListBooksResponse.newBuilder();
        bookCatalogService.findAll().stream()
                .map(this::toProto)
                .forEach(response::addBooks);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void getBookById(GetBookByIdRequest request, StreamObserver<GetBookByIdResponse> responseObserver) {
        if (request.getId().isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("id must not be blank")
                    .asRuntimeException());
            return;
        }

        GetBookByIdResponse.Builder response = GetBookByIdResponse.newBuilder();
        bookCatalogService.findById(request.getId())
                .map(this::toProto)
                .ifPresent(response::setBook);
        responseObserver.onNext(response.build());
        responseObserver.onCompleted();
    }

    @Override
    public void addBook(AddBookRequest request, StreamObserver<Book> responseObserver) {
        if (request.getTitle().isBlank() || request.getAuthor().isBlank()) {
            responseObserver.onError(Status.INVALID_ARGUMENT
                    .withDescription("title and author must not be blank")
                    .asRuntimeException());
            return;
        }

        Book response = toProto(bookCatalogService.addBook(request.getTitle(), request.getAuthor()));
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void bookAdded(Empty request, StreamObserver<Book> responseObserver) {
        if (!(responseObserver instanceof ServerCallStreamObserver<Book> serverObserver)) {
            responseObserver.onError(Status.INTERNAL
                    .withDescription("Streaming observer does not support cancellation callbacks")
                    .asRuntimeException());
            return;
        }

        Runnable unsubscribe = bookCatalogService.subscribeToBookAdded(book -> {
            if (!serverObserver.isCancelled()) {
                serverObserver.onNext(toProto(book));
            }
        });

        serverObserver.setOnCancelHandler(unsubscribe);
        serverObserver.setOnCloseHandler(unsubscribe);
    }

    private Book toProto(BookRecord book) {
        return Book.newBuilder()
                .setId(book.id())
                .setTitle(book.title())
                .setAuthor(book.author())
                .build();
    }
}


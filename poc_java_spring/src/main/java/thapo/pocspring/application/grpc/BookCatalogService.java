package thapo.pocspring.application.grpc;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Consumer;

@Service
public class BookCatalogService {

    private final AtomicLong idSequence = new AtomicLong(3);
    private final CopyOnWriteArrayList<BookRecord> books = new CopyOnWriteArrayList<>(List.of(
            new BookRecord("1", "The Great Gatsby", "F. Scott Fitzgerald"),
            new BookRecord("2", "To Kill a Mockingbird", "Harper Lee"),
            new BookRecord("3", "1984", "George Orwell")
    ));
    private final CopyOnWriteArrayList<Consumer<BookRecord>> bookAddedSubscribers = new CopyOnWriteArrayList<>();

    public List<BookRecord> findAll() {
        return List.copyOf(books);
    }

    public Optional<BookRecord> findById(String id) {
        return books.stream()
                .filter(book -> book.id().equals(id))
                .findFirst();
    }

    public BookRecord addBook(String title, String author) {
        BookRecord book = new BookRecord(String.valueOf(idSequence.incrementAndGet()), title, author);
        books.add(book);
        notifyBookAdded(book);
        return book;
    }

    public Runnable subscribeToBookAdded(Consumer<BookRecord> consumer) {
        bookAddedSubscribers.add(consumer);
        return () -> bookAddedSubscribers.remove(consumer);
    }

    private void notifyBookAdded(BookRecord book) {
        bookAddedSubscribers.forEach(subscriber -> subscriber.accept(book));
    }
}


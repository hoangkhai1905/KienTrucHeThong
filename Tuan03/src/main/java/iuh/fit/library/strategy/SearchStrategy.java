package iuh.fit.library.strategy;

import iuh.fit.library.models.Book;
import java.util.List;

public interface SearchStrategy {
    void search(List<Book> books, String keyword);
}


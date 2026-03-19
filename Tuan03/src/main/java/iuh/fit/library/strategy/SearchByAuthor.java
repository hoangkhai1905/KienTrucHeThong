package iuh.fit.library.strategy;

import iuh.fit.library.models.Book;
import java.util.List;

public class SearchByAuthor implements SearchStrategy {
    @Override
    public void search(List<Book> books, String keyword) {
        System.out.println("Searching by Author: " + keyword);
        // Assuming metadata simulation as Book doesn't strictly have author field in this simplified version
        System.out.println(" - (Feature simulated) No books found for author: " + keyword);
    }
}


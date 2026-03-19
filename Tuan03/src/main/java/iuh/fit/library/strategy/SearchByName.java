package iuh.fit.library.strategy;

import iuh.fit.library.models.Book;
import java.util.List;

public class SearchByName implements SearchStrategy {
    @Override
    public void search(List<Book> books, String keyword) {
        System.out.println("Searching by Name: " + keyword);
        boolean found = false;
        for (Book book : books) {
            if (book.getTitle().toLowerCase().contains(keyword.toLowerCase())) {
                System.out.println(" - Found: " + book.getTitle() + " [" + book.getType() + "]");
                found = true;
            }
        }
        if (!found) System.out.println(" - No books found.");
    }
}


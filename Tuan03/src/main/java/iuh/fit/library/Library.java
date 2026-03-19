package iuh.fit.library;

import iuh.fit.library.models.Book;
import iuh.fit.library.observer.LibraryObserver;
import iuh.fit.library.strategy.SearchStrategy;
import iuh.fit.library.strategy.SearchByName;

import java.util.ArrayList;
import java.util.List;

public class Library {

    private static Library instance;
    private List<Book> books;
    private List<LibraryObserver> observers;
    private SearchStrategy searchStrategy;

    private Library() {
        books = new ArrayList<>();
        observers = new ArrayList<>();
        searchStrategy = new SearchByName(); // Default strategy
    }

    public static synchronized Library getInstance() {
        if (instance == null) {
            instance = new Library();
        }
        return instance;
    }

    public void addObserver(LibraryObserver observer) {
        observers.add(observer);
    }

    public void addBook(Book book) {
        books.add(book);
        notifyObservers("New book added: " + book.getTitle());
    }

    private void notifyObservers(String message) {
        for (LibraryObserver observer : observers) {
            observer.update(message);
        }
    }

    public void setSearchStrategy(SearchStrategy strategy) {
        this.searchStrategy = strategy;
    }

    public void searchBooks(String keyword) {
        if (searchStrategy != null) {
            searchStrategy.search(books, keyword);
        } else {
            System.out.println("No search strategy defined.");
        }
    }
}


package iuh.fit.library.decorator;

import iuh.fit.library.models.Book;

public class BaseLoan implements Loan {
    private Book book;

    public BaseLoan(Book book) {
        this.book = book;
    }

    @Override
    public String getDetails() {
        return "Loan: " + book.getTitle();
    }

    @Override
    public double calculateFee() {
        return 10.0; // Base fee
    }
}


package iuh.fit.library;

import iuh.fit.library.models.Book;
import iuh.fit.library.models.BookFactory;
import iuh.fit.library.models.PaperBook;
import iuh.fit.library.observer.Librarian;
import iuh.fit.library.observer.Member;
import iuh.fit.library.strategy.SearchByName;
import iuh.fit.library.strategy.SearchByCategory;
import iuh.fit.library.strategy.SearchByAuthor;
import iuh.fit.library.decorator.Loan;
import iuh.fit.library.decorator.BaseLoan;
import iuh.fit.library.decorator.ExtendedDurationDecorator;
import iuh.fit.library.decorator.SpecialEditionDecorator;

import java.util.Scanner;

public class LibraryApp {

    public static void main(String[] args) {
        Library library = Library.getInstance();
        Scanner scanner = new Scanner(System.in);

        // Pre-register some observers
        library.addObserver(new Librarian("Sarah"));
        library.addObserver(new Member("John"));

        while (true) {
            System.out.println("\n=== LIBRARY MANAGEMENT SYSTEM ===");
            System.out.println("1. Add Book");
            System.out.println("2. Search Book");
            System.out.println("3. Borrow Book");
            System.out.println("4. Exit");
            System.out.print("Select option: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter book type (PAPER/EBOOK/AUDIO): ");
                    String type = scanner.nextLine();
                    System.out.print("Enter title: ");
                    String title = scanner.nextLine();
                    Book book = BookFactory.createBook(type, title);
                    if (book != null) {
                        library.addBook(book);
                    } else {
                        System.out.println("Invalid book type.");
                    }
                    break;
                case "2":
                    System.out.println("Select search strategy:");
                    System.out.println("a. By Name");
                    System.out.println("b. By Category");
                    String strat = scanner.nextLine();
                    if (strat.equalsIgnoreCase("a")) library.setSearchStrategy(new SearchByName());
                    else if (strat.equalsIgnoreCase("b")) library.setSearchStrategy(new SearchByCategory());
                    
                    System.out.print("Enter keyword: ");
                    String keyword = scanner.nextLine();
                    library.searchBooks(keyword);
                    break;
                case "3":
                    System.out.print("Enter book name to borrow: ");
                    String borrowTitle = scanner.nextLine();
                    // Simulating book retrieval for borrowing
                    Book borrowBook = new PaperBook(borrowTitle); 
                    
                    Loan loan = new BaseLoan(borrowBook);
                    System.out.println("Standard Loan Fee: $" + loan.calculateFee());
                    
                    System.out.print("Add Extended Duration? (y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("y")) {
                        loan = new ExtendedDurationDecorator(loan);
                    }
                    
                    System.out.print("Add Special Edition Handling? (y/n): ");
                    if (scanner.nextLine().equalsIgnoreCase("y")) {
                        loan = new SpecialEditionDecorator(loan);
                    }
                    
                    System.out.println("\n--- Loan Summary ---");
                    System.out.println("Details: " + loan.getDetails());
                    System.out.println("Total Fee: $" + loan.calculateFee());
                    break;
                case "4":
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid option.");
            }
        }
    }
}


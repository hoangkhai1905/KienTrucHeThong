package iuh.fit.design_patterns;

import iuh.fit.design_patterns.composite.File;
import iuh.fit.design_patterns.composite.Folder;
import iuh.fit.design_patterns.observer.Investor;
import iuh.fit.design_patterns.observer.Stock;
import iuh.fit.design_patterns.observer.Task;
import iuh.fit.design_patterns.observer.TeamMember; // New import
import iuh.fit.design_patterns.adapter.JsonProvider;
import iuh.fit.design_patterns.adapter.WebClient;
import iuh.fit.design_patterns.adapter.XmlLibrary;
import iuh.fit.design_patterns.adapter.XmlSystem;
import iuh.fit.design_patterns.adapter.XmlToJsonAdapter;
import iuh.fit.design_patterns.adapter.JsonWebService;
import iuh.fit.library.LibraryApp;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n--- Design Patterns Exercises ---");
            System.out.println("1. Composite Pattern (File System)");
            System.out.println("2. Observer Pattern (Stock Market)");
            System.out.println("3. Adapter Pattern (XML to JSON)");
            System.out.println("4. Library Management System");
            System.out.println("0. Exit");
            System.out.print("Choose an option: ");

            int choice = -1;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input.");
                continue;
            }

            switch (choice) {
                case 1:
                    runCompositeDemo();
                    break;
                case 2:
                    runObserverDemo();
                    break;
                case 3:
                    runAdapterDemo();
                    break;
                case 4:
                    LibraryApp.main(null);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void runCompositeDemo() {
        System.out.println("\n--- Composite Pattern Demo ---");
        Folder root = new Folder("Root");
        Folder documents = new Folder("Documents");
        Folder pictures = new Folder("Pictures");

        File resume = new File("Resume.docx", 100);
        File budget = new File("Budget.xlsx", 50);
        File photo1 = new File("Vacation.png", 2000);
        File photo2 = new File("Avatar.jpg", 500);

        root.add(documents);
        root.add(pictures);

        documents.add(resume);
        documents.add(budget);

        pictures.add(photo1);
        pictures.add(photo2);

        System.out.println("File System Structure:");
        root.displayInfo();
        System.out.println("Total Size: " + root.getSize() + "KB");
    }

    private static void runObserverDemo() {
        System.out.println("\n--- Observer Pattern Demo ---");
        
        // Stock Example
        System.out.println("Part 1: Stock Market");
        Stock techStock = new Stock("TECH", 100.0);
        Investor investor1 = new Investor("Alice");
        Investor investor2 = new Investor("Bob");

        techStock.attach(investor1);
        techStock.attach(investor2);

        System.out.println("Setting stock price to 105.0...");
        techStock.setPrice(105.0);

        System.out.println("Bob starts ignoring the stock.");
        techStock.detach(investor2);

        System.out.println("Setting stock price to 110.0...");
        techStock.setPrice(110.0);
        
        // Task Example (New)
        System.out.println("\nPart 2: Task Status");
        Task projectTask = new Task("API Development", "In Progress");
        TeamMember dev1 = new TeamMember("John");
        TeamMember manager = new TeamMember("Sarah");
        
        projectTask.attach(dev1);
        projectTask.attach(manager);
        
        System.out.println("Updating task status to 'Completed'...");
        projectTask.setStatus("Completed");
    }

    private static void runAdapterDemo() {
        System.out.println("\n--- Adapter Pattern Demo ---");
        
        // 1. We have an existing legacy system (Adaptee)
        XmlSystem legacyXmlSystem = new XmlSystem();
        
        // 2. We have an adapter that makes the legacy system look like a JsonWebService
        JsonWebService adapter = new XmlToJsonAdapter(legacyXmlSystem);
        
        // 3. The client expects to talk to a JsonWebService
        WebClient client = new WebClient(adapter);
        
        // 4. Client sends JSON data
        String jsonRequest = "{ \"username\": \"john_doe\", \"action\": \"login\" }";
        client.sendRequest(jsonRequest);
    }
}
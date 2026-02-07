package iuh.fit;

import iuh.fit.singleton.DatabaseConnection;
import iuh.fit.factory.BankFactory;
import iuh.fit.factory.Bank;
import iuh.fit.state.*;
import iuh.fit.strategy.*;
import iuh.fit.decorator.*;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\nSelect an exercise to run:");
            System.out.println("1. Singleton Pattern");
            System.out.println("2. Factory Pattern");
            System.out.println("3. State Pattern (Order System)");
            System.out.println("4. Strategy Pattern (Tax Calculation)");
            System.out.println("5. Decorator Pattern (Payment System)");
            System.out.println("0. Exit");
            System.out.print("Enter choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    testSingleton();
                    break;
                case 2:
                    testFactory();
                    break;
                case 3:
                    testState();
                    break;
                case 4:
                    testStrategy();
                    break;
                case 5:
                    testDecorator();
                    break;
                case 0:
                    System.out.println("Exiting...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private static void testSingleton() {
        System.out.println("--- Singleton Pattern ---");

        System.out.println("1. Getting first instance (db1)...");
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        db1.connect();
        db1.query("SELECT * FROM users");

        System.out.println("\n2. Getting second instance (db2)...");
        DatabaseConnection db2 = DatabaseConnection.getInstance();
        db2.query("SELECT * FROM products");

        System.out.println("\n3. Comparing instances...");
        System.out.println("Are db1 and db2 the same object? " + (db1 == db2));

        System.out.println("\n4. Testing shared state...");
        System.out.println("Disconnecting db1...");
        db1.disconnect();
        System.out.println("Trying to query with db2...");
        db2.query("SELECT * FROM orders"); // Should fail or show disconnected because db1 disconnected the shared instance

        System.out.println("\n5. Testing Thread Safety (Simulated):");
        Runnable task = () -> {
            DatabaseConnection db = DatabaseConnection.getInstance();
            System.out.println("Thread " + Thread.currentThread().getId() + " obtained instance: " + db.hashCode());
        };

        Thread t1 = new Thread(task);
        Thread t2 = new Thread(task);
        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void testFactory() {
        System.out.println("--- Factory Pattern ---");
        Bank bank1 = BankFactory.getBank("VIETCOMBANK");
        if (bank1 != null) System.out.println("Bank 1: " + bank1.getBankName());

        Bank bank2 = BankFactory.getBank("TPBANK");
        if (bank2 != null) System.out.println("Bank 2: " + bank2.getBankName());
    }

    private static void testState() {
        System.out.println("--- State Pattern ---");
        Order order = new Order();
        System.out.println("Current State: New");
        order.process();

        System.out.println("Changing to Processing...");
        order.setState(new ProcessingState());
        order.process();

        System.out.println("Changing to Delivered...");
        order.setState(new DeliveredState());
        order.process();

        System.out.println("Changing to Cancelled...");
        order.setState(new CancelledState());
        order.process();
    }

    private static void testStrategy() {
        System.out.println("--- Strategy Pattern ---");
        TaxCalculator calculator = new TaxCalculator(new ConsumptionTax());
        double price = 100.0;
        System.out.println("Price: " + price);

        System.out.println("Tax (Consumption 5%): " + calculator.calculate(price));

        calculator.setTaxStrategy(new VATTax());
        System.out.println("Tax (VAT 10%): " + calculator.calculate(price));

        calculator.setTaxStrategy(new LuxuryTax());
        System.out.println("Tax (Luxury 20%): " + calculator.calculate(price));
    }

    private static void testDecorator() {
        System.out.println("--- Decorator Pattern ---");
        Payment creditCard = new CreditCardPayment();
        System.out.println("Normal Credit Card:");
        creditCard.pay(100);

        System.out.println("\nCredit Card with Processing Fee:");
        Payment creditCardWithFee = new ProcessingFeeDecorator(new CreditCardPayment());
        creditCardWithFee.pay(100);

        System.out.println("\nPayPal with Discount:");
        Payment paypalWithDiscount = new DiscountCodeDecorator(new PayPalPayment());
        paypalWithDiscount.pay(100);

        System.out.println("\nCredit Card with Fee AND Discount:");
        Payment complexPayment = new DiscountCodeDecorator(new ProcessingFeeDecorator(new CreditCardPayment()));
        complexPayment.pay(100);
    }
}
package iuh.fit.library.observer;

public class Librarian implements LibraryObserver {
    private String name;

    public Librarian(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[LIBRARIAN] " + name + " received: " + message);
    }
}


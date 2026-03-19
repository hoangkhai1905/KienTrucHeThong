package iuh.fit.library.observer;

public class Member implements LibraryObserver {
    private String name;

    public Member(String name) {
        this.name = name;
    }

    @Override
    public void update(String message) {
        System.out.println("[MEMBER] " + name + " received: " + message);
    }
}


package iuh.fit.design_patterns.observer;

import java.util.ArrayList;
import java.util.List;

public class Task implements Subject {
    private String taskName;
    private String status;
    private List<Observer> observers = new ArrayList<>();

    public Task(String taskName, String status) {
        this.taskName = taskName;
        this.status = status;
    }

    @Override
    public void attach(Observer observer) {
        observers.add(observer);
    }

    @Override
    public void detach(Observer observer) {
        observers.remove(observer);
    }

    public void setStatus(String status) {
        this.status = status;
        notifyObservers();
    }

    @Override
    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update("Task " + taskName + " is now " + status);
        }
    }
}


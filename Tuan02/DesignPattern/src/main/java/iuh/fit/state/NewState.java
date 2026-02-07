package iuh.fit.state;

public class NewState implements OrderState {
    @Override
    public void handleRequest() {
        System.out.println("Processing New Order: Checking order information...");
    }
}

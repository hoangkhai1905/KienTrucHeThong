package iuh.fit.state;

public class DeliveredState implements OrderState {
    @Override
    public void handleRequest() {
        System.out.println("Order Delivered: Updating status to delivered.");
    }
}

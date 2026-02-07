package iuh.fit.state;

public class CancelledState implements OrderState {
    @Override
    public void handleRequest() {
        System.out.println("Order Cancelled: Cancelling order and refunding money.");
    }
}

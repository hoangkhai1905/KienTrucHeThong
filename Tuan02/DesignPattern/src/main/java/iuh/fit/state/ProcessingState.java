package iuh.fit.state;

public class ProcessingState implements OrderState {
    @Override
    public void handleRequest() {
        System.out.println("Processing Order: Packing and shipping...");
    }
}

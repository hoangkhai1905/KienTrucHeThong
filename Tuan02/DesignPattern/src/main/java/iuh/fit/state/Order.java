package iuh.fit.state;

public class Order {
    private OrderState state;

    public Order() {
        this.state = new NewState(); // Initial state
    }

    public void setState(OrderState state) {
        this.state = state;
    }

    public void process() {
        state.handleRequest();
    }
}

package iuh.fit.decorator;

public class DiscountCodeDecorator extends PaymentDecorator {
    public DiscountCodeDecorator(Payment decoratedPayment) {
        super(decoratedPayment);
    }

    @Override
    public void pay(double amount) {
        double discount = amount * 0.10; // 10% discount
        System.out.println("Applying discount: " + discount);
        super.pay(amount - discount);
    }
}

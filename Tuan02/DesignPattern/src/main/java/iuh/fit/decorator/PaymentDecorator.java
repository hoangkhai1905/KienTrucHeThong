package iuh.fit.decorator;

public abstract class PaymentDecorator implements Payment {
    protected Payment decoratedPayment;

    public PaymentDecorator(Payment decoratedPayment) {
        this.decoratedPayment = decoratedPayment;
    }

    public void pay(double amount) {
        decoratedPayment.pay(amount);
    }
}

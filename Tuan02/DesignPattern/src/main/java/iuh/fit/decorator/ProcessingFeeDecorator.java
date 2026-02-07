package iuh.fit.decorator;

public class ProcessingFeeDecorator extends PaymentDecorator {
    public ProcessingFeeDecorator(Payment decoratedPayment) {
        super(decoratedPayment);
    }

    @Override
    public void pay(double amount) {
        double fee = amount * 0.02; // 2% processing fee
        System.out.println("Adding processing fee: " + fee);
        super.pay(amount + fee);
    }
}

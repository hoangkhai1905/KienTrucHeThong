package iuh.fit.library.decorator;

public class SpecialEditionDecorator extends LoanDecorator {
    public SpecialEditionDecorator(Loan loan) {
        super(loan);
    }

    @Override
    public String getDetails() {
        return super.getDetails() + " + Special Edition Handling";
    }

    @Override
    public double calculateFee() {
        return super.calculateFee() + 5.0;
    }
}


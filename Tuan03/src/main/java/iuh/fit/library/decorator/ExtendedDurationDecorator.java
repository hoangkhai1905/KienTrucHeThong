package iuh.fit.library.decorator;

public class ExtendedDurationDecorator extends LoanDecorator {
    public ExtendedDurationDecorator(Loan loan) {
        super(loan);
    }

    @Override
    public String getDetails() {
        return super.getDetails() + " + Extended Duration (7 days)";
    }

    @Override
    public double calculateFee() {
        return super.calculateFee() + 3.0;
    }
}


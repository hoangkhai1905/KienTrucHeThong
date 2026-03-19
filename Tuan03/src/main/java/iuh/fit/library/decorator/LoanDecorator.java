package iuh.fit.library.decorator;

public abstract class LoanDecorator implements Loan {
    protected Loan decoratedLoan;

    public LoanDecorator(Loan loan) {
        this.decoratedLoan = loan;
    }

    @Override
    public String getDetails() {
        return decoratedLoan.getDetails();
    }

    @Override
    public double calculateFee() {
        return decoratedLoan.calculateFee();
    }
}


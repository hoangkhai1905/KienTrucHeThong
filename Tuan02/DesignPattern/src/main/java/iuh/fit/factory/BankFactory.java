package iuh.fit.factory;

public class BankFactory {
    public static Bank getBank(String bankType) {
        if (bankType == null) {
            return null;
        }
        if (bankType.equalsIgnoreCase("VIETCOMBANK")) {
            return new VietcomBank();
        } else if (bankType.equalsIgnoreCase("TPBANK")) {
            return new TPBank();
        }
        return null;
    }
}

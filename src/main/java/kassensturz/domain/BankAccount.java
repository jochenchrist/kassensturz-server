package kassensturz.domain;

import java.math.BigDecimal;

public class BankAccount {
    public Bank bank;
    public String iban;
    public String type;
    public BigDecimal amount;

    public BankAccount(Bank bank, String iban) {
        this.bank = bank;
        this.iban = iban;
    }
}

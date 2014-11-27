package kassensturz.domain;

import org.springframework.hateoas.ResourceSupport;

import java.util.Arrays;
import java.util.List;

public class User extends ResourceSupport {
    public String username;
    public List<BankAccount> bankAccounts;

    public User() {
    }

    public User(String username, Bank bank, String iban) {
        this.username = username;
        this.bankAccounts = Arrays.asList(new BankAccount(bank, iban));
    }

}

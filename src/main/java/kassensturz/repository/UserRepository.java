package kassensturz.repository;

import kassensturz.domain.Bank;
import kassensturz.domain.BankAccount;
import kassensturz.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Simple in memory repository
 */
@Component
public class UserRepository {

    public List<User> users = new ArrayList<>();

    /**
     * Initialize data
     */
    public UserRepository() {
        users.add(jochen());
        users.add(new User("peter", Bank.HIMBEERBANK, "DE99123456780000000004"));
    }


    public User getUser(String username) {
        Assert.notNull(username);

        for(User user : users ) {
            if(username.equals(user.username)) {
                User newUser = new User();
                newUser.username = user.username;
                newUser.bankAccounts = user.bankAccounts;
                return newUser;
            }
        }

        return null;
    }

    private static User jochen() {
        User user = new User();
        user.username = "jochen";
        user.bankAccounts = Arrays.asList(new BankAccount(Bank.CLOUDBANK, "DE99123456780000000001"),
                                          new BankAccount(Bank.CLOUDBANK, "DE99123456780000000002"),
                                          new BankAccount(Bank.CLOUDBANK, "DE99123456780000000003")
                );
        return user;
    }

}

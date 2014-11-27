package kassensturz.repository;

import kassensturz.domain.Bank;
import kassensturz.domain.User;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.ArrayList;
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
        users.add(new User("jochen", Bank.CLOUDBANK, "DE99123456780000000001"));
        users.add(new User("peter", Bank.HIMBEERBANK, "DE99777777770000000001"));
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

}

package kassensturz.controller;

import kassensturz.domain.BankAccount;
import kassensturz.domain.User;
import kassensturz.event.AccountBalance;
import kassensturz.exception.NotFoundException;
import kassensturz.integration.CommandRetrieveAccountBalance;
import kassensturz.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    UserRepository userRepository;


    /**
     * @return A list of all available users, with link
     */
    @RequestMapping(method = GET)
    @ResponseStatus(OK)
    public List<User> getUsers() {
        final List<User> users = userRepository.users;
        users.forEach(user -> user.add(ControllerLinkBuilder.linkTo(methodOn(UserController.class).getUser(user.username)).withSelfRel()));

        return users;
    }

    /**
     * @return The user profile with all bank accounts an the balance, retrieved from the banks.
     */
    @RequestMapping(method = GET, value = "/{username}")
    @ResponseStatus(OK)
    public User getUser(@PathVariable String username) {

        final User user = userRepository.getUser(username);

        if(user == null) {
            throw new NotFoundException();
        }

        for(BankAccount bankAccount : user.bankAccounts) {
            final AccountBalance accountBalance = new CommandRetrieveAccountBalance(bankAccount.bank, bankAccount.iban).execute();
            if(accountBalance != null) {
                bankAccount.type = accountBalance.type;
                bankAccount.amount = accountBalance.amount;
            }
        }

        user.add(ControllerLinkBuilder.linkTo(methodOn(UserController.class).getUser(user.username)).withSelfRel());

        return user;

    }



}

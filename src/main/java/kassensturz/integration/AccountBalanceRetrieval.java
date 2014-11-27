package kassensturz.integration;

import kassensturz.domain.Bank;
import kassensturz.event.AccountBalance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class AccountBalanceRetrieval {

    private static final Logger logger = LoggerFactory.getLogger(AccountBalanceRetrieval.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    Environment environment;

    /**
     * Retrieves the current balance from the bank
     */
    public AccountBalance getBalance(Bank bank, String iban) {
        final String bankUrl = environment.getProperty("bank." + bank.name().toLowerCase());
        final URI uri = UriComponentsBuilder.fromHttpUrl(bankUrl).buildAndExpand(iban).toUri();

        logger.info("GET {}", uri);

        return restTemplate.getForObject(uri, AccountBalance.class);
    }

}

package kassensturz.integration;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import kassensturz.domain.Bank;
import kassensturz.event.AccountBalance;
import kassensturz.factory.Config;
import kassensturz.factory.RestTemplateFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

/**
 * AccountBalanceRetrieval with Hystrix Support
 */
public class CommandRetrieveAccountBalance extends HystrixCommand<AccountBalance> {

    private static final Logger logger = LoggerFactory.getLogger(CommandRetrieveAccountBalance.class);

    private RestTemplate restTemplate;

    private Bank bank;
    private String iban;

    private URI bankUri;

    /**
     * Constructor.
     *
     * Does not support @Autowired
     */
    public CommandRetrieveAccountBalance(Bank bank, String iban) {
        super(HystrixCommandGroupKey.Factory.asKey(bank.name()));
        this.restTemplate = RestTemplateFactory.getRestTemplate();
        this.bank = bank;
        this.iban = iban;

        final String bankUrl = Config.getProperty("bank." + bank.name().toLowerCase());
        bankUri = UriComponentsBuilder.fromHttpUrl(bankUrl).buildAndExpand(iban).toUri();
    }

    /**
     * Retrieves the current balance from the bank
     */
    @Override
    protected AccountBalance run() throws Exception {
        logger.info("START GET {}", bankUri);
        final long start = System.currentTimeMillis();

        final AccountBalance accountBalance = restTemplate.getForObject(bankUri, AccountBalance.class);

        final long end = System.currentTimeMillis();
        logger.info("END   GET {} Duration: {}", bankUri, end-start);

        return accountBalance;
    }


}

package kassensturz.integration;

import com.netflix.hystrix.HystrixCommand;
import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.HystrixCommandKey;
import com.netflix.hystrix.HystrixThreadPoolKey;
import kassensturz.domain.Bank;
import kassensturz.event.AccountBalance;
import kassensturz.factory.Config;
import kassensturz.factory.RestTemplateFactory;
import kassensturz.repository.AccountBalanceCache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.Clock;
import java.time.Instant;

/**
 * AccountBalanceRetrieval as a Command with Hystrix Support
 */
public class CommandRetrieveAccountBalance extends HystrixCommand<AccountBalance> {

    private static final Logger logger = LoggerFactory.getLogger(CommandRetrieveAccountBalance.class);

    private Bank bank;
    private String iban;

    private RestTemplate restTemplate;
    private URI backendUrl;

    /**
     * Constructor.
     */
    public CommandRetrieveAccountBalance(Bank bank, String iban) {
        super(Setter.withGroupKey(HystrixCommandGroupKey.Factory.asKey("RetrieveAccountBalanceGroup_" + bank))
        .andCommandKey(HystrixCommandKey.Factory.asKey("RetrieveAccountBalance_" + bank))
        .andThreadPoolKey(HystrixThreadPoolKey.Factory.asKey(bank.name())));

        this.bank = bank;
        this.iban = iban;

        this.restTemplate = RestTemplateFactory.getRestTemplate();
        this.backendUrl = buildEndpointUrl();

    }

    /**
     * Retrieves the current balance from the bank.
     */
    @Override
    protected AccountBalance run() throws Exception {
        logger.info("START GET {}", backendUrl);
        final long start = System.currentTimeMillis();

        final AccountBalance accountBalance = restTemplate.getForObject(backendUrl, AccountBalance.class);
        accountBalance.timestamp = Instant.now().toString();

        final long end = System.currentTimeMillis();
        logger.info("END   GET {} Duration: {}", backendUrl, end-start);

        return accountBalance;
    }

    /**
     * In case of an error, try to return cached data from a former request
     */
    @Override
    protected AccountBalance getFallback() {
        logger.info("Read data from cache for iban {}", iban);
        return AccountBalanceCache.getCachedAccountBalance(bank, iban);
    }

    private URI buildEndpointUrl() {
        final String bankUrl = Config.getProperty("bank." + bank.name().toLowerCase());
        return UriComponentsBuilder.fromHttpUrl(bankUrl).buildAndExpand(iban).toUri();
    }
}

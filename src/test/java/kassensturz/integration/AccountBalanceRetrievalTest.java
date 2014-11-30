package kassensturz.integration;

import kassensturz.domain.Bank;
import kassensturz.event.AccountBalance;
import org.junit.Test;
import rx.Observable;
import rx.Observer;

import java.math.BigDecimal;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.Assert.*;

public class AccountBalanceRetrievalTest {

    @Test
    public void testSynchronous() {

        AccountBalance accountBalance1 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000001").execute();
        AccountBalance accountBalance2 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000002").execute();

        assertEquals(new BigDecimal("332.23"), accountBalance1.amount);
        assertEquals(new BigDecimal("-32.76"), accountBalance2.amount);

    }

    @Test
    public void testAsynchronous() throws ExecutionException, InterruptedException {

        Future<AccountBalance> fAccountBalance1 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000003").queue();
        Future<AccountBalance> fAccountBalance2 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000004").queue();

        assertEquals(new BigDecimal("5000"), fAccountBalance1.get().amount);
        assertEquals(new BigDecimal("7232.23"), fAccountBalance2.get().amount);
    }


    @Test
    public void testReactive() throws InterruptedException {

        Semaphore semaphore = new Semaphore(2);

        semaphore.acquire(2);

        AtomicReference<AccountBalance> arBalance1 = new AtomicReference<>();
        AtomicReference<AccountBalance> arBalance2 = new AtomicReference<>();

        Observable<AccountBalance> oAccountBalance1 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000003").observe();
        Observable<AccountBalance> oAccountBalance2 = new CommandRetrieveAccountBalance(Bank.CLOUDBANK, "DE99123456780000000004").observe();

        // verbose with anonymous class
        oAccountBalance1.subscribe(new Observer<AccountBalance>() {

            @Override
            public void onNext(AccountBalance accountBalance) {
                System.out.println(accountBalance.amount);
                arBalance1.set(accountBalance);
                semaphore.release();
            }

            @Override
            public void onCompleted() {
                // not required
            }

            @Override
            public void onError(Throwable e) {
                System.out.println("error " + e);
            }

        });


        // short with lambda
        oAccountBalance2.subscribe((accountBalance) -> {
            System.out.println(accountBalance.amount);
            arBalance2.set(accountBalance);
            semaphore.release();
        });

        // wait until released
        semaphore.acquire(2);

        // do the assertions
        assertEquals(new BigDecimal("5000"), arBalance1.get().amount);
        assertEquals(new BigDecimal("7232.23"), arBalance2.get().amount);


    }

}
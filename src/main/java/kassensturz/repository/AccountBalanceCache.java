package kassensturz.repository;

import kassensturz.domain.Bank;
import kassensturz.event.AccountBalance;

import java.math.BigDecimal;

public class AccountBalanceCache {

    public static AccountBalance getCachedAccountBalance(Bank bank, String iban) {

        if(bank == Bank.CLOUDBANK && iban.equals("DE99123456780000000001")) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.amount = new BigDecimal("372.99");
            accountBalance.type = "Giro";
            accountBalance.timestamp = "2014-10-03T10:15:30.00Z";
            return accountBalance;
        } else if(bank == Bank.CLOUDBANK && iban.equals("DE99123456780000000002")) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.amount = new BigDecimal("-40.99");
            accountBalance.type = "Giro";
            accountBalance.timestamp =  "2014-10-03T10:15:30.00Z";
            return accountBalance;
        } else if(bank == Bank.CLOUDBANK && iban.equals("DE99123456780000000003")) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.amount = new BigDecimal("4999");
            accountBalance.type = "Sparbuch";
            accountBalance.timestamp = "2014-10-03T10:15:30.00Z";
            return accountBalance;
        } else if(bank == Bank.CLOUDBANK && iban.equals("DE99123456780000000004")) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.amount = new BigDecimal("7232.23");
            accountBalance.type = "Tagesgeld";
            accountBalance.timestamp = "2014-10-03T10:15:30.00Z";
            return accountBalance;
        } else if(bank == Bank.HIMBEERBANK && iban.equals("DE99123456780000000001")) {
            AccountBalance accountBalance = new AccountBalance();
            accountBalance.amount = new BigDecimal("371.99");
            accountBalance.type = "Giro";
            accountBalance.timestamp = "2014-10-03T10:15:30.00Z";
            return accountBalance;
        }

        return null;
    }

}

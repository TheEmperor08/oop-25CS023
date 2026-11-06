package service;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InsufficientFundsException;
import exception.InvalidAmountException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Unit tests for the MiniBank service layer (Practical 14). */
class BankServiceTest {

    @TempDir
    Path dataDir;                 // a fresh, empty folder for every test

    private BankService service;
    private Account savings;

    @BeforeEach
    void setUp() throws BankException {
        service = new BankService(dataDir);
        savings = service.openAccount("SAVINGS", "Riya", 5000);
    }

    @Test
    @DisplayName("A deposit increases the balance")
    void depositIncreasesBalance() throws BankException {
        service.deposit(savings.getAccountNumber(), 2000);
        assertEquals(7000, savings.getBalance());
    }

    @Test
    @DisplayName("A withdrawal over the limit throws and leaves the balance unchanged")
    void withdrawalOverLimitThrows() {
        // savings keep a minimum balance of 1000, so at most 4000 can be withdrawn
        InsufficientFundsException e = assertThrows(InsufficientFundsException.class,
                () -> service.withdraw(savings.getAccountNumber(), 4500));
        assertEquals(500, e.getShortfall());
        assertEquals(5000, savings.getBalance());
    }

    @Test
    @DisplayName("Zero, negative and non-numeric amounts are rejected")
    void invalidAmountsAreRejected() {
        String number = savings.getAccountNumber();
        assertThrows(InvalidAmountException.class, () -> service.deposit(number, 0));
        assertThrows(InvalidAmountException.class, () -> service.withdraw(number, -5));
        assertThrows(InvalidAmountException.class, () -> BankService.parseAmount("12a"));
        assertEquals(5000, savings.getBalance());
    }

    @Test
    @DisplayName("An unknown account number throws AccountNotFoundException")
    void unknownAccountThrows() {
        assertThrows(AccountNotFoundException.class, () -> service.deposit("AC9999", 100));
    }

    @Test
    @DisplayName("A transfer moves money; a failed transfer changes nothing")
    void transferMovesMoney() throws BankException {
        Account current = service.openAccount("CURRENT", "Aman", 2000);
        service.transfer(savings.getAccountNumber(), current.getAccountNumber(), 1500);
        assertEquals(3500, savings.getBalance());
        assertEquals(3500, current.getBalance());
        assertThrows(InsufficientFundsException.class,
                () -> service.transfer(savings.getAccountNumber(),
                        current.getAccountNumber(), 9000));
        assertEquals(3500, savings.getBalance());
        assertEquals(3500, current.getBalance());
    }

    @Test
    @DisplayName("Many concurrent deposits keep the balance correct")
    void concurrentDepositsKeepBalanceCorrect() throws Exception {
        ExecutorService pool = Executors.newFixedThreadPool(10);
        for (int t = 0; t < 10; t++) {
            pool.execute(() -> {
                for (int i = 0; i < 1000; i++) {
                    try {
                        savings.deposit(1);
                    } catch (BankException e) {
                        throw new IllegalStateException(e);
                    }
                }
            });
        }
        pool.shutdown();
        assertTrue(pool.awaitTermination(30, TimeUnit.SECONDS));
        assertEquals(5000 + 10_000, savings.getBalance());
    }

    @Test
    @DisplayName("A concurrent batch on the thread pool gives the exact expected balance")
    void batchProcessingIsThreadSafe() throws Exception {
        List<String> batch = new ArrayList<>();
        for (int i = 0; i < 300; i++) {
            batch.add("DEPOSIT " + savings.getAccountNumber() + " 10");
        }
        batch.add("DEPOSIT AC9999 10");                    // unknown account -> fails
        batch.add("HELLO");                                // malformed line -> fails
        int[] result = service.processBatch(batch);
        assertArrayEquals(new int[] {300, 2}, result);
        assertEquals(5000 + 3000, savings.getBalance());
    }

    @Test
    @DisplayName("State saved to a file loads back unchanged")
    void saveAndLoadKeepsDataUnchanged() throws Exception {
        Account fd = service.openAccount("FD", "Neha", 50000);
        service.deposit(savings.getAccountNumber(), 250);
        assertEquals(2, service.save());

        BankService restarted = new BankService(dataDir);   // like a new program run
        assertEquals(2, restarted.load());
        Account reloaded = restarted.find(savings.getAccountNumber());
        assertEquals(savings, reloaded);
        assertEquals(5250, reloaded.getBalance());
        assertEquals(50000, restarted.find(fd.getAccountNumber()).getBalance());
        assertEquals(service.listAccounts().toString(), restarted.listAccounts().toString());
    }

    @Test
    @DisplayName("Search by owner name ignores case")
    void searchByOwner() throws BankException {
        service.openAccount("CURRENT", "Aman Patel", 3000);
        assertEquals(1, service.searchByOwner("PATEL").size());
        assertEquals(0, service.searchByOwner("Zed").size());
    }
}

package service;

import static util.Validator.isValidAmount;

import exception.AccountNotFoundException;
import exception.BankException;
import exception.InvalidAmountException;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import model.Account;
import model.Command;
import model.CurrentAccount;
import model.FixedDepositAccount;
import model.SavingsAccount;
import util.AnnotationValidator;
import util.CommandParser;
import util.ReportGenerator;
import util.Repository;
import util.StatePersister;
import util.StatementFormatter;
import util.TransactionLog;

/**
 * Service layer of MiniBank (Practical 14). Wires together the generic
 * Repository (P13), the validators (P4, P7), the exceptions (P8), the thread-pool
 * TransactionProcessor over a ConcurrentHashMap (P10, P12) and the persistence,
 * log and report classes (P11). The menu (user interface) only talks to this class.
 */
public class BankService {

    /** Minimum balance of every savings account. */
    public static final long SAVINGS_MIN_BALANCE = 1000;
    /** Overdraft limit of every current account. */
    public static final long CURRENT_OVERDRAFT = 10000;

    private final Repository<Account> repository = new Repository<>();
    private final Path accountsFile;
    private final Path logsDir;
    private final Path logFile;
    private final Path reportFile;

    /** All data lives below dataDir: accounts.dat, logs/ and reports/. */
    public BankService(Path dataDir) {
        this.accountsFile = dataDir.resolve("accounts.dat");
        this.logsDir = dataDir.resolve("logs");
        this.logFile = logsDir.resolve("transactions.log");
        this.reportFile = dataDir.resolve("reports").resolve("eod-report.txt");
    }

    // ------------------------------------------------------------ persistence

    /** Loads the accounts saved by an earlier run; returns how many were loaded. */
    public int load() throws IOException {
        if (!Files.exists(accountsFile)) {
            return 0;
        }
        Account[] saved = StatePersister.load(accountsFile);
        repository.saveAll(saved);
        return saved.length;
    }

    /** Saves every account; returns how many were saved. */
    public int save() throws IOException {
        List<Account> all = listAccounts();
        StatePersister.save(all.toArray(new Account[0]), accountsFile);
        return all.size();
    }

    // ------------------------------------------------------------ operations

    /**
     * Opens an account of the given type ("SAVINGS", "CURRENT" or "FD").
     * The new object is checked with the annotation validator before it is stored.
     */
    public Account openAccount(String type, String ownerName, long openingBalance)
            throws BankException {
        if (ownerName == null || ownerName.isBlank()) {
            throw new BankException("Owner name must not be empty");
        }
        if (openingBalance <= 0) {
            throw new InvalidAmountException(openingBalance);
        }
        String owner = ownerName.trim();
        Account account = switch (type.trim().toUpperCase()) {
            case "SAVINGS" -> {
                if (openingBalance < SAVINGS_MIN_BALANCE) {
                    throw new BankException("A savings account needs an opening balance of at "
                            + "least " + SAVINGS_MIN_BALANCE);
                }
                yield new SavingsAccount(owner, openingBalance, SAVINGS_MIN_BALANCE);
            }
            case "CURRENT" -> new CurrentAccount(owner, openingBalance, CURRENT_OVERDRAFT);
            case "FD" -> new FixedDepositAccount(owner, openingBalance);
            default -> throw new BankException("Unknown account type: " + type);
        };
        String[] errors = AnnotationValidator.validateWithFieldNames(account);
        if (errors.length > 0) {
            throw new BankException("Invalid account data: " + String.join(", ", errors));
        }
        repository.save(account);
        log("OPEN " + account.getAccountNumber() + " " + openingBalance);
        return account;
    }

    public void deposit(String accountNumber, long amount) throws BankException {
        find(accountNumber).deposit(amount);
        log("DEPOSIT " + accountNumber + " " + amount);
    }

    public void withdraw(String accountNumber, long amount) throws BankException {
        find(accountNumber).withdraw(amount);
        log("WITHDRAW " + accountNumber + " " + amount);
    }

    public void transfer(String fromNumber, String toNumber, long amount) throws BankException {
        Account from = find(fromNumber);
        Account to = find(toNumber);
        if (from == to) {
            throw new BankException("Cannot transfer to the same account");
        }
        from.transfer(to, amount);
        log("TRANSFER " + fromNumber + " " + toNumber + " " + amount);
    }

    /** The formatted statement plus the last 10 logged transactions of the account. */
    public String statement(String accountNumber) throws BankException {
        Account account = find(accountNumber);
        StringBuilder sb = new StringBuilder(StatementFormatter.buildStatement(account));
        sb.append(System.lineSeparator()).append("Recent transactions:");
        List<String> lines = transactionsOf(accountNumber);
        if (lines.isEmpty()) {
            sb.append(System.lineSeparator()).append("  (none)");
        }
        for (String line : lines.subList(Math.max(0, lines.size() - 10), lines.size())) {
            sb.append(System.lineSeparator()).append("  ").append(line);
        }
        return sb.toString();
    }

    /** All accounts in natural (account-number) order. */
    public List<Account> listAccounts() {
        List<Account> all = repository.findAll();
        Collections.sort(all);
        return all;
    }

    /** Supplementary: accounts whose owner name contains the text (ignoring case). */
    public List<Account> searchByOwner(String text) {
        String needle = text.trim().toLowerCase();
        List<Account> result = new ArrayList<>();
        for (Account account : listAccounts()) {
            if (account.getOwnerName().toLowerCase().contains(needle)) {
                result.add(account);
            }
        }
        return result;
    }

    /**
     * Processes a batch of command lines ("DEPOSIT AC0001 500") concurrently on the
     * TransactionProcessor's thread pool, over its thread-safe ConcurrentHashMap.
     * @return {succeeded, failed}
     */
    public int[] processBatch(List<String> commandLines) throws InterruptedException {
        TransactionProcessor processor = new TransactionProcessor();
        for (Account account : repository.findAll()) {
            processor.register(account);
        }
        AtomicInteger ok = new AtomicInteger();
        AtomicInteger failed = new AtomicInteger();
        for (String line : commandLines) {
            processor.submit(() -> {
                try {
                    Command command = CommandParser.parse(line);
                    Account account = processor.getAccount(command.accountNumber());
                    switch (command.type()) {
                        case DEPOSIT -> account.deposit(command.amount());
                        case WITHDRAW -> account.withdraw(command.amount());
                        case TRANSFER ->
                            throw new BankException("TRANSFER is not allowed in a batch");
                    }
                    log(command.type() + " " + command.accountNumber() + " " + command.amount());
                    ok.incrementAndGet();
                } catch (BankException | IllegalArgumentException e) {
                    failed.incrementAndGet();
                }
            });
        }
        processor.stop();
        return new int[] {ok.get(), failed.get()};
    }

    /** Reads a batch file (one command per line). */
    public List<String> readBatchFile(Path file) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file, StandardCharsets.UTF_8)) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    lines.add(line.trim());
                }
            }
        }
        return lines;
    }

    /** Builds, writes and returns the end-of-day report from the transaction logs. */
    public String report() throws IOException {
        Files.createDirectories(logsDir);
        return ReportGenerator.generate(logsDir, reportFile);
    }

    // ------------------------------------------------------------ helpers

    /** Converts typed text to an amount, validated with the regex validator. */
    public static long parseAmount(String text) throws InvalidAmountException {
        String trimmed = text == null ? "" : text.trim();
        if (!isValidAmount(trimmed)) {
            throw new InvalidAmountException(trimmed);
        }
        return Long.parseLong(trimmed);
    }

    public Account find(String accountNumber) throws AccountNotFoundException {
        String key = accountNumber == null ? "" : accountNumber.trim().toUpperCase();
        Account account = repository.findById(key);
        if (account == null) {
            throw new AccountNotFoundException(key);
        }
        return account;
    }

    public int count() {
        return repository.count();
    }

    /** Appends one line to the transaction log (synchronized: called from pool threads). */
    private synchronized void log(String line) throws BankException {
        try {
            TransactionLog.append(logFile, line);
        } catch (IOException e) {
            throw new BankException("Could not write the transaction log: " + e.getMessage());
        }
    }

    /** Lines of the transaction log that mention the account. */
    private List<String> transactionsOf(String accountNumber) throws BankException {
        List<String> result = new ArrayList<>();
        if (!Files.exists(logFile)) {
            return result;
        }
        try {
            for (String line : Files.readAllLines(logFile, StandardCharsets.UTF_8)) {
                if (List.of(line.split("\\s+")).contains(accountNumber)) {
                    result.add(line);
                }
            }
        } catch (IOException e) {
            throw new BankException("Could not read the transaction log: " + e.getMessage());
        }
        return result;
    }
}

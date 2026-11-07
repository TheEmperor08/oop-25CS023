package service;

import exception.AccountNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import model.Account;

/**
 * Stores accounts in a HashMap keyed by account number (fast look-up) and keeps a
 * transaction history in an ArrayList.
 */
public class AccountStore {

    private final Map<String, Account> accounts = new HashMap<>();
    private final List<String> history = new ArrayList<>();

    public void add(Account account) {
        accounts.put(account.getAccountNumber(), account);
        history.add("ADD " + account.getAccountNumber() + " " + account.getOwnerName());
    }

    /** Looks an account up by its number. */
    public Account get(String accountNumber) throws AccountNotFoundException {
        Account account = accounts.get(accountNumber);
        if (account == null) {
            throw new AccountNotFoundException(accountNumber);
        }
        return account;
    }

    /** All accounts (in no particular order - HashMap does not keep one). */
    public List<Account> getAll() {
        return new ArrayList<>(accounts.values());
    }

    /** Records one line of transaction history. */
    public void record(String entry) {
        history.add(entry);
    }

    /** The history in the order it happened (read-only view). */
    public List<String> getHistory() {
        return Collections.unmodifiableList(history);
    }

    /** All accounts sorted by balance, highest first, using a Comparator. */
    public ArrayList<Account> sortedByBalance() {
        ArrayList<Account> list = new ArrayList<>(accounts.values());
        list.sort(Comparator.comparingLong(Account::getBalance).reversed());
        return list;
    }

    /** Supplementary: accounts whose balance is above the given value. */
    public List<Account> balanceAbove(long value) {
        List<Account> result = new ArrayList<>();
        for (Account account : accounts.values()) {
            if (account.getBalance() > value) {
                result.add(account);
            }
        }
        Collections.sort(result);             // natural order (Comparable): by number
        return result;
    }

    /** Supplementary: a TreeMap copy, which keeps the keys in sorted order. */
    public TreeMap<String, Account> sortedByNumber() {
        return new TreeMap<>(accounts);
    }

    public int size() {
        return accounts.size();
    }
}

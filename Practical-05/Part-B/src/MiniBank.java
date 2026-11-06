import java.time.LocalDate;
import java.util.Scanner;

/**
 * MiniBank - console banking application.
 * Practical 01: menu shell.
 * Practical 02: Customer and Account classes (encapsulation).
 * Practical 03: toString(), equals()/hashCode(), nested Address, clone(), instanceof.
 * Practical 04: Validator (regex), Command record, CommandParser, StatementFormatter.
 * Practical 05: abstract Account with SavingsAccount, CurrentAccount, FixedDepositAccount.
 */
public class MiniBank {

    public static void main(String[] args) {
        BankInfo bank = new BankInfo("MiniBank", "Changa Branch");
        System.out.println("=== " + bank.name() + " - " + bank.branch() + " ===");
        System.out.println(bank);

        runPracticalDemo();
        runMenu(bank);
    }

    /** Practical 05 demo: account types in one Account[] array (run-time polymorphism). */
    private static void runPracticalDemo() {
        System.out.println();
        System.out.println("--- Practical 05: Account hierarchy and polymorphism ---");
        Account[] accounts = {
            new SavingsAccount("Riya", 5000, 1000),
            new CurrentAccount("Aman", 2000, 5000),
            new FixedDepositAccount("Neha", 50000)
        };
        for (Account account : accounts) {
            // Java picks the subclass version of interestRate() at run time
            System.out.println(account.getClass().getSimpleName() + " " + account.getAccountNumber()
                    + " interestRate() = " + account.interestRate()
                    + ", monthlyInterest() = " + String.format("%.2f", account.monthlyInterest()));
        }

        System.out.println("Savings withdraw(4500) -> " + accounts[0].withdraw(4500)
                + " (min balance 1000), balance " + accounts[0].getBalance());
        System.out.println("Savings withdraw(3000) -> " + accounts[0].withdraw(3000)
                + ", balance " + accounts[0].getBalance());
        System.out.println("Current withdraw(6000) -> " + accounts[1].withdraw(6000)
                + " (overdraft 5000), balance " + accounts[1].getBalance());
        System.out.println("Current withdraw(2000) -> " + accounts[1].withdraw(2000)
                + ", balance " + accounts[1].getBalance());
        System.out.println("FD withdraw(1000)      -> " + accounts[2].withdraw(1000)
                + " (locked), balance " + accounts[2].getBalance());
        Account maturedFd = new FixedDepositAccount("Old FD", 10000, LocalDate.now().minusDays(1));
        System.out.println("Matured FD withdraw(1000) -> " + maturedFd.withdraw(1000)
                + ", balance " + maturedFd.getBalance());

        for (Account account : accounts) {
            if (account instanceof SavingsAccount savings) {          // pattern instanceof
                System.out.println(savings.getAccountNumber() + " is a savings account with minimum "
                        + "balance " + savings.getMinBalance());
            } else if (account instanceof FixedDepositAccount fd) {
                System.out.println(fd.getAccountNumber() + " is a fixed deposit maturing on "
                        + fd.getMaturityDate());
            }
        }
        Account base = accounts[1];
        CurrentAccount current = (CurrentAccount) base;              // explicit down-cast
        System.out.println("Down-cast to CurrentAccount: overdraft limit "
                + current.getOverdraftLimit());
    }

    // ---- end of practical demo ----

    /**
     * Supplementary: moves an amount from one account to another using
     * withdraw and deposit. Nothing changes if the withdrawal fails.
     */
    static boolean transfer(Account from, Account to, long amount) {
        if (amount <= 0 || !from.withdraw(amount)) {
            return false;
        }
        to.deposit(amount);
        return true;
    }

    /** The Practical 01 menu loop: placeholders until EXIT is chosen. */
    private static void runMenu(BankInfo bank) {
        Scanner scanner = new Scanner(System.in);
        MenuOption option;
        do {
            printMenu();
            option = readOption(scanner);
            if (option == null) {          // input ended
                break;
            }
            String message = switch (option) {
                case OPEN_ACCOUNT -> "Open account - to be implemented in a later lab";
                case DEPOSIT -> "Deposit - to be implemented in a later lab";
                case WITHDRAW -> "Withdraw - to be implemented in a later lab";
                case TRANSFER -> "Transfer - to be implemented in a later lab";
                case WORKING_HOURS -> "Working hours: Mon-Fri 10:00-17:00, Sat 10:00-13:00";
                case EXIT -> "Thank you for banking with " + bank.name() + ". Goodbye!";
            };
            System.out.println(message);
        } while (option != MenuOption.EXIT);
        scanner.close();
    }

    /** Prints the numbered menu built from the enum constants. */
    private static void printMenu() {
        System.out.println();
        MenuOption[] options = MenuOption.values();
        for (int i = 0; i < options.length; i++) {
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.print("Enter choice: ");
    }

    /**
     * Reads a menu number and maps it to a MenuOption with a switch expression.
     * Re-prompts until a valid number is entered; returns null at end of input.
     */
    private static MenuOption readOption(Scanner scanner) {
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            int choice;
            try {
                choice = Integer.parseInt(line);
            } catch (NumberFormatException e) {
                choice = -1;
            }
            MenuOption option = switch (choice) {
                case 1 -> MenuOption.OPEN_ACCOUNT;
                case 2 -> MenuOption.DEPOSIT;
                case 3 -> MenuOption.WITHDRAW;
                case 4 -> MenuOption.TRANSFER;
                case 5 -> MenuOption.WORKING_HOURS;
                case 6 -> MenuOption.EXIT;
                default -> null;
            };
            if (option != null) {
                return option;
            }
            System.out.print("Invalid choice '" + line + "'. Enter a number from 1 to "
                    + MenuOption.values().length + ": ");
        }
        return null;
    }
}

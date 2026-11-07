package util;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import model.Account;

/**
 * Saves the whole Account[] to a file with object serialization and loads it back.
 */
public class StatePersister {

    private StatePersister() {
        // utility class: no objects
    }

    /** Writes the array (and every account in it) with ObjectOutputStream. */
    public static void save(Account[] accounts, Path file) throws IOException {
        if (file.getParent() != null) {
            Files.createDirectories(file.getParent());
        }
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(file))) {
            out.writeObject(accounts);
        }   // the stream is closed automatically, even if writeObject throws
    }

    /** Reads an array written by save() with ObjectInputStream. */
    public static Account[] load(Path file) throws IOException {
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(file))) {
            return (Account[]) in.readObject();
        } catch (ClassNotFoundException e) {
            throw new IOException("Saved data does not match the program's classes", e);
        }
    }
}

package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import model.annotation.Id;

/**
 * A generic, type-safe store for ANY entity type T. The key of each object is
 * found at run time by reflection: the value of its field marked with @Id.
 *
 * @param <T> the entity type, e.g. Account or Customer
 */
public class Repository<T> {

    private final Map<String, T> items = new HashMap<>();

    /** Stores (or replaces) the entity under its @Id value and returns it. */
    public T save(T entity) {
        items.put(idOf(entity), entity);
        return entity;
    }

    /** Saves several entities at once. @SafeVarargs: the T[] is only read. */
    @SafeVarargs
    public final void saveAll(T... entities) {
        for (T entity : entities) {
            save(entity);
        }
    }

    /** The entity with this id, or null when there is none. */
    public T findById(String id) {
        return items.get(id);
    }

    public ArrayList<T> findAll() {
        return new ArrayList<>(items.values());
    }

    /** Removes the entity with this id; true if something was removed. */
    public boolean delete(String id) {
        return items.remove(id) != null;
    }

    /** Supplementary: the number of stored items. */
    public int count() {
        return items.size();
    }

    /** A generic method with a bounded type parameter: works for any Number array. */
    public static <N extends Number> double average(N[] values) {
        if (values.length == 0) {
            return 0;
        }
        double sum = 0;
        for (N value : values) {
            sum += value.doubleValue();       // allowed because N extends Number
        }
        return sum / values.length;
    }

    /** Finds the field marked @Id (also in superclasses) and reads its value. */
    private String idOf(T entity) {
        for (Class<?> type = entity.getClass(); type != null; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                if (field.isAnnotationPresent(Id.class)) {
                    try {
                        field.setAccessible(true);
                        return String.valueOf(field.get(entity));
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException("Cannot read @Id field", e);
                    }
                }
            }
        }
        throw new IllegalArgumentException(entity.getClass().getSimpleName()
                + " has no field marked @Id");
    }
}

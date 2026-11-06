package util;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import model.annotation.MaxLength;
import model.annotation.Positive;

/**
 * Validates ANY object by reading its field annotations at run time with
 * reflection - the same idea validation frameworks use.
 */
public class AnnotationValidator {

    private AnnotationValidator() {
        // utility class: no objects
    }

    /**
     * Checks the @Positive and @MaxLength rules on every field of obj
     * (including fields inherited from superclasses).
     * @return the error messages; an empty array when the object is valid
     */
    public static String[] validate(Object obj) {
        return check(obj, false);
    }

    /** Supplementary: the same check, but every message starts with the field name. */
    public static String[] validateWithFieldNames(Object obj) {
        return check(obj, true);
    }

    private static String[] check(Object obj, boolean withFieldNames) {
        List<String> errors = new ArrayList<>();
        // getClass() is the runtime type (e.g. SavingsAccount); walk up to Account too
        for (Class<?> type = obj.getClass(); type != Object.class; type = type.getSuperclass()) {
            for (Field field : type.getDeclaredFields()) {
                String prefix = withFieldNames ? field.getName() + ": " : "";
                if (field.isAnnotationPresent(Positive.class)) {
                    Object value = read(field, obj);
                    if (!(value instanceof Number) || ((Number) value).doubleValue() <= 0) {
                        errors.add(prefix + field.getAnnotation(Positive.class).message());
                    }
                }
                if (field.isAnnotationPresent(MaxLength.class)) {
                    int max = field.getAnnotation(MaxLength.class).value();
                    Object value = read(field, obj);
                    if (value instanceof String && ((String) value).length() > max) {
                        errors.add(prefix + "length must be <= " + max);
                    }
                }
            }
        }
        return errors.toArray(new String[0]);
    }

    /** Reads a (possibly private) field value by reflection. */
    private static Object read(Field field, Object obj) {
        try {
            field.setAccessible(true);
            return field.get(obj);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot read field " + field.getName(), e);
        }
    }
}

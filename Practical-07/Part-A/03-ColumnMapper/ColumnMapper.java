import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical 07 - Part A - Program 3 (For Advanced Learners): Column mapper.
 * Populates an object's @Column fields from a header row and a data row by
 * matching column names (case-insensitive).
 *
 * Missing-column policy: the field keeps its default value (null / 0) and a
 * warning is recorded, so one absent column does not reject the whole row.
 * Extra columns with no matching field are ignored and also reported.
 */
public final class ColumnMapper {

    private ColumnMapper() {
    }

    public static <T> T map(Class<T> type, String header, String row, List<String> warnings)
            throws ReflectiveOperationException {
        String[] names = header.split(",");
        String[] values = row.split(",", -1);
        T object = type.getDeclaredConstructor().newInstance();
        List<String> used = new ArrayList<>();

        for (Field field : type.getDeclaredFields()) {
            Column column = field.getAnnotation(Column.class);
            if (column == null) {
                continue;
            }
            int index = indexOf(names, column.name());
            if (index < 0 || index >= values.length) {
                warnings.add("missing column '" + column.name() + "' -> "
                        + field.getName() + " left at default");
                continue;
            }
            used.add(names[index].trim());
            field.setAccessible(true);
            String raw = values[index].trim();
            try {
                field.set(object, convert(raw, field.getType()));
            } catch (NumberFormatException e) {
                warnings.add("bad value '" + raw + "' for column '" + column.name() + "'");
            }
        }
        for (String name : names) {
            if (!used.contains(name.trim())) {
                warnings.add("extra column '" + name.trim() + "' ignored");
            }
        }
        return object;
    }

    private static int indexOf(String[] names, String wanted) {
        for (int i = 0; i < names.length; i++) {
            if (names[i].trim().equalsIgnoreCase(wanted)) {
                return i;
            }
        }
        return -1;
    }

    private static Object convert(String raw, Class<?> target) {
        if (target == int.class) {
            return Integer.parseInt(raw);
        }
        if (target == double.class) {
            return Double.parseDouble(raw);
        }
        return raw;
    }
}

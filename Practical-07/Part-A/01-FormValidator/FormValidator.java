import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

/**
 * Practical 07 - Part A - Program 1: Form validator.
 * Reads each field's annotations by reflection and collects validation errors.
 */
public final class FormValidator {

    private FormValidator() {
    }

    public static List<String> validate(Object form) {
        List<String> errors = new ArrayList<>();
        for (Field field : form.getClass().getDeclaredFields()) {
            field.setAccessible(true);
            Object value;
            try {
                value = field.get(form);
            } catch (IllegalAccessException e) {
                errors.add(field.getName() + ": cannot be read");
                continue;
            }
            String text = (value == null) ? null : value.toString();

            if (field.isAnnotationPresent(NotBlank.class)
                    && (text == null || text.trim().isEmpty())) {
                errors.add(field.getName() + ": must not be blank");
            }
            MaxLength max = field.getAnnotation(MaxLength.class);
            if (max != null && text != null && text.length() > max.value()) {
                errors.add(field.getName() + ": length " + text.length()
                        + " exceeds max " + max.value());
            }
        }
        return errors;
    }
}

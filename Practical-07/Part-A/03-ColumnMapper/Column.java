import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Practical 07 - Part A - Program 3 (For Advanced Learners): Column mapper.
 * Binds a field to a named column of a CSV-style row.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Column {

    String name();
}

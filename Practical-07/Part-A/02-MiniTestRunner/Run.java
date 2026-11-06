import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Practical 07 - Part A - Program 2: Mini test runner.
 * Marks a no-arg method that the runner should invoke (like JUnit's @Test).
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Run {
}

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Comparator;

/**
 * Practical 07 - Part A - Program 2: Mini test runner.
 * Uses reflection to find and invoke only the @Run methods of a class and reports
 * how many ran - the idea behind how JUnit finds @Test methods.
 */
public class MiniTestRunner {

    public static void main(String[] args) throws ReflectiveOperationException {
        Class<?> type = CalculatorChecks.class;
        Object instance = type.getDeclaredConstructor().newInstance();

        Method[] methods = type.getDeclaredMethods();
        Arrays.sort(methods, Comparator.comparing(Method::getName));   // stable order
        int ran = 0;
        int passed = 0;
        System.out.println("Scanning " + type.getSimpleName() + " (" + methods.length
                + " declared methods)");
        for (Method method : methods) {
            if (!method.isAnnotationPresent(Run.class) || method.getParameterCount() != 0) {
                continue;
            }
            ran++;
            try {
                method.invoke(instance);
                passed++;
                System.out.println("  PASS " + method.getName());
            } catch (InvocationTargetException e) {
                System.out.println("  FAIL " + method.getName() + ": "
                        + e.getCause().getMessage());
            }
        }
        System.out.println("Ran " + ran + " @Run methods: " + passed + " passed, "
                + (ran - passed) + " failed");
    }
}

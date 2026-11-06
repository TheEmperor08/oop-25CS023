import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Practical 04 - Part A - Program 3 (For Advanced Learners): Template filler.
 * Replaces every {placeholder} with its value; unknown names become [?].
 */
public class TemplateFiller {

    private static final Pattern PLACEHOLDER = Pattern.compile("\\{(\\w+)\\}");

    public static String fill(String template, String[] names, String[] values) {
        Matcher matcher = PLACEHOLDER.matcher(template);
        StringBuilder result = new StringBuilder();
        int last = 0;
        while (matcher.find()) {
            result.append(template, last, matcher.start());
            String key = matcher.group(1);
            String replacement = "[?]";
            for (int i = 0; i < names.length; i++) {
                if (names[i].equals(key)) {
                    replacement = values[i];
                    break;
                }
            }
            result.append(replacement);
            last = matcher.end();
        }
        result.append(template.substring(last));
        return result.toString();
    }
}

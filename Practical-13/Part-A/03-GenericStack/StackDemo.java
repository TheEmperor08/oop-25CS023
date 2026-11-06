/**
 * Practical 13 - Part A - Program 3 (For Advanced Learners): Generic stack.
 * Reuses the same GenericStack class for Integer and for Character values.
 */
public class StackDemo {

    /** Uses a stack of Character to check whether brackets are balanced. */
    static boolean balanced(String text) {
        GenericStack<Character> stack = new GenericStack<>();
        for (char c : text.toCharArray()) {
            if (c == '(' || c == '[' || c == '{') {
                stack.push(c);
            } else if (c == ')' || c == ']' || c == '}') {
                if (stack.isEmpty()) {
                    return false;
                }
                char open = stack.pop();
                if ((c == ')' && open != '(') || (c == ']' && open != '[')
                        || (c == '}' && open != '{')) {
                    return false;
                }
            }
        }
        return stack.isEmpty();
    }

    public static void main(String[] args) {
        GenericStack<Integer> numbers = new GenericStack<>();
        for (int i = 1; i <= 5; i++) {
            numbers.push(i * 10);
        }
        System.out.println("Integer stack size " + numbers.size()
                + ", top = " + numbers.peek());
        System.out.print("Popping all:");
        while (!numbers.isEmpty()) {
            System.out.print(" " + numbers.pop());
        }
        System.out.println();

        try {
            numbers.pop();
        } catch (IllegalStateException e) {
            System.out.println("pop() on empty stack -> " + e.getMessage());
        }

        String[] samples = {"(a[b]{c})", "(a[b)c]", "((x)"};
        for (String s : samples) {
            System.out.println("Character stack: " + s + " balanced? " + balanced(s));
        }
    }
}

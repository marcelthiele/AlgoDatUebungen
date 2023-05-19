import java.util.Stack;

/**
 * A class for parsing arithmetic expressions
 */
public class Parser {

    /**
     * An exception that is thrown if the to-be-parsed expression is not
     * well-formed.
     */
    public static class ExpressionNotWellFormedException extends Exception {
        public ExpressionNotWellFormedException() {
        }
    }

    /**
     * Parses a given String, determines whether it is a well-formed expression,
     * and computes the expression.
     * 
     * @param expression
     *                   the expression that is to be evaluated
     * @return the result of the evaluation / computation
     * @throws ExpressionNotWellFormedException
     *                                          if the expression is not
     *                                          well-formed, an exception is thrown
     */
    public static int parse(String expression)
            throws ExpressionNotWellFormedException {
        return parseRecursive(expression);
    }

    private static int parseRecursive(String expression) throws Parser.ExpressionNotWellFormedException {
        char[] expressionAsArray = expression.toCharArray();
        // System.out.println("Expression: " + expression);

        // Base Case: Just one number
        if (expressionAsArray.length == 1) {
            if (isNumber(expressionAsArray[0]))
                return expressionAsArray[0] - '0';
        }

        // ERROR Cases
        if (expressionAsArray.length < 5 && expressionAsArray.length > 1) {
            // Is never allowed
            // Expression must either be:
            // 1. only one Number -> see Base Case
            // 2. at least 5 chars -> "(X+Y)"
            throw new ExpressionNotWellFormedException();
        }
        if (!isOpeningParenthese(expressionAsArray[0]) && !isNumber(expressionAsArray[0])) {
            // Case: eg. "+...", "a..."
            throw new ExpressionNotWellFormedException();
        }

        char symbol = ' ';
        int symbolIndex = 0;

        // FIND X
        Stack<Character> xStack = new Stack<Character>();
        int X = 0;
        for (int i = 1; i < expressionAsArray.length; i++) {
            if (isOpeningParenthese(expressionAsArray[i])) {
                xStack.push(')');
            }
            if (isClosingParenthese(expressionAsArray[i])) {
                if (xStack.isEmpty())
                    throw new ExpressionNotWellFormedException();
                xStack.pop();
            }
            if (isSymbol(expressionAsArray[i]) && xStack.isEmpty() && i > 1) {
                // Found X
                symbolIndex = i;
                symbol = expressionAsArray[symbolIndex];
                X = parseRecursive(expression.substring(1, i));
                break;
            }
        }

        // FIND Y
        // Y is string between the Symbol and the last closing Parenthese
        String YExpression = expression.substring(symbolIndex + 1, expressionAsArray.length - 1);
        int Y = parseRecursive(YExpression);

        // Return Result according to symbol
        switch (symbol) {
            case '+':
                return X + Y;
            case '*':
                return X * Y;
            case '-':
                return X - Y;
            case '/':
                return X / Y;
        }

        //Should never be reached, but if reached, throw exception
        throw new ExpressionNotWellFormedException();
    }

    private static boolean isSymbol(char c) {
        switch (c) {
            case '+':
                return true;
            case '*':
                return true;
            case '-':
                return true;
            case '/':
                return true;
            default:
                return false;
        }
    }

    private static boolean isNumber(char c) {
        switch (c) {
            case '1':
                return true;
            case '2':
                return true;
            case '3':
                return true;
            case '4':
                return true;
            case '5':
                return true;
            case '6':
                return true;
            case '7':
                return true;
            case '8':
                return true;
            case '9':
                return true;
            case '0':
                return true;

            default:
                return false;
        }
    }

    private static boolean isClosingParenthese(char c) {
        switch (c) {
            case ')':
                return true;
            case '}':
                return true;
            case ']':
                return true;

            default:
                return false;
        }
    }

    private static boolean isOpeningParenthese(char toCheck) {

        switch (toCheck) {
            case '(':
                return true;
            case '{':
                return true;
            case '[':
                return true;

            default:
                return false;
        }

    }

    /**
     * test cases
     */
    public static void main(String[] args) {
        {
            wellFormedCheck("((8+7)*2)", 30);
            wellFormedCheck("(4-(7-1))", -2);
            wellFormedCheck("8", 8);
            wellFormedCheck("((1+1)*(2*2))", 8);
            wellFormedCheck("((1+9)*(2-5))", -30);
            wellFormedCheck("((0-9)*((1+9)*(2-5)))", -270);

            notWellFormedCheck(")8+)1(())");
            notWellFormedCheck("(8+())");
            notWellFormedCheck("-1");
            notWellFormedCheck("(   5    -7)");
            notWellFormedCheck("108");
            notWellFormedCheck("(8)");
            notWellFormedCheck("(+8)");
        }
    }

    private static void checkAndPrint(String message, boolean correct) {
        System.out.println((correct ? "PASS:" : "FAIL:") + " " + message);
        assert (correct);
    }

    private static void notWellFormedCheck(String expression) {
        try {
            int returned = parse(expression);
            checkAndPrint("nicht wohlgeformter Ausdruck " + expression
                    + " ausgewertet zu " + returned, false);
        } catch (ExpressionNotWellFormedException e) {
            checkAndPrint("Ausdruck " + expression
                    + " als nicht wohlgeformt erkannt.", true);
        }
    }

    private static void wellFormedCheck(String expression, int expected) {
        try {
            int returned = parse(expression);
            checkAndPrint("Ausdruck " + expression + " ausgewertet zu " + returned
                    + " (erwartet: " + expected + ")", returned == expected);
        } catch (ExpressionNotWellFormedException e) {
            checkAndPrint("Ausdruck " + expression
                    + " fälschlicherweise als nicht wohlgeformt eingeschätzt.", false);
        }
    }
}
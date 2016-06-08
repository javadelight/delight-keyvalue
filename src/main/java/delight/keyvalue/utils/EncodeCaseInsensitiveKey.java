package delight.keyvalue.utils;

import delight.functional.Function;

public class EncodeCaseInsensitiveKey implements Function<String, String> {

    @Override
    public String apply(final String input) {

        String res = "";
        // System.out.println("INPUT " + input);
        for (int i = 0; i < input.length(); i++) {
            final char testChar = input.charAt(i);

            assert testChar != '^' : "Found illegal character for Case Sensitive Encoding: ^\n" + "   In Key: " + input;

            if (!Character.isUpperCase(testChar) || testChar == '_' || testChar == '/' || testChar == '-') {
                res += testChar;
            } else {
                res += "^" + testChar;
            }

        }

        return res;
    }

}

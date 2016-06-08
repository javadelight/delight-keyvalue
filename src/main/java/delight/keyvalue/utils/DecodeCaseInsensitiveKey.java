package delight.keyvalue.utils;

import delight.functional.Function;

public class DecodeCaseInsensitiveKey implements Function<String, String> {
    @Override
    public String apply(final String input) {
        String res = "";
        int i = 0;
        while ((i < input.length())) {
            {
                final char testChar = input.charAt(i);
                final Character _valueOf = Character.valueOf('^');
                final char _charValue = _valueOf.charValue();
                final boolean _tripleNotEquals = (testChar != _charValue);
                if (_tripleNotEquals) {
                    final String _res = res;
                    res = (_res + Character.valueOf(testChar));
                } else {
                    i++;
                    final String _res_1 = res;
                    final char _charAt = input.charAt(i);
                    final char _upperCase = Character.toUpperCase(_charAt);
                    res = (_res_1 + Character.valueOf(_upperCase));
                }
                i++;
            }
        }
        return res;
    }
}

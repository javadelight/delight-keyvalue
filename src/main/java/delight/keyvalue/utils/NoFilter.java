package delight.keyvalue.utils;

import delight.functional.Function;

public class NoFilter implements Function<String, String> {

    @Override
    public String apply(final String input) {

        return input;
    }

}

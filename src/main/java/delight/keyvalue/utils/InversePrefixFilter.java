package delight.keyvalue.utils;

import delight.functional.Function;

public class InversePrefixFilter implements Function<String, String> {
    private final int prefixLength;

    public InversePrefixFilter(final int prefixLength) {
        if (prefixLength < 0) {
            throw new IllegalArgumentException("Cannot create inverse prefix filter with length: " + prefixLength);
        }

        this.prefixLength = prefixLength;

    }

    @Override
    public String apply(final String input) {
        if (input.length() <= prefixLength) {
            return input;
        }
        return input.substring(prefixLength);
    }
}
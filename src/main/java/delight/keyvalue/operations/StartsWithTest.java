package delight.keyvalue.operations;

import delight.functional.Function;

public class StartsWithTest implements Function<String, Boolean> {

    private final String startsWith;

    public final String startsWith() {
        return startsWith;
    }

    @Override
    public Boolean apply(final String input) {

        return input.startsWith(startsWith);
    }

    public StartsWithTest(final String startsWith) {
        super();
        this.startsWith = startsWith;
    }

}

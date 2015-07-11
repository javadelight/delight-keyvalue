package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;

final class EmptyCallback implements SimpleCallback {
    @Override
    public void onFailure(final Throwable arg0) {

    }

    @Override
    public void onSuccess() {

    }
}
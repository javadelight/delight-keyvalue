package delight.keyvalue.internal.decorators;

import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.keyvalue.Store;
import delight.keyvalue.operations.StoreOperation;

public class ExceptionOnStartAndStopStore implements Store<String, Object> {

    private final String message;
    private final Store<String, Object> decorated;

    public ExceptionOnStartAndStopStore(final String message, final Store<String, Object> decorated) {
        super();
        this.message = message;
        this.decorated = decorated;
    }

    @Override
    public void put(final String key, final Object value, final SimpleCallback callback) {
        decorated.put(key, value, callback);
    }

    @Override
    public void putSync(final String key, final Object value) {
        decorated.putSync(key, value);
    }

    @Override
    public void get(final String key, final ValueCallback<Object> callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public Object getSync(final String key) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void remove(final String key, final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void removeSync(final String key) {
        // TODO Auto-generated method stub

    }

    @Override
    public void start(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void stop(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void commit(final SimpleCallback callback) {
        // TODO Auto-generated method stub

    }

    @Override
    public void performOperation(final StoreOperation<String, Object> operation, final ValueCallback<Object> callback) {
        // TODO Auto-generated method stub

    }

}

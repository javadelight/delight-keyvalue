package delight.keyvalue.tests;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.Value;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Closure;
import delight.functional.Success;
import delight.keyvalue.Store;
import delight.keyvalue.StoreEntry;
import delight.keyvalue.Stores;
import delight.keyvalue.operations.StoreOperation;
import delight.keyvalue.operations.StoreOperations;
import org.junit.Test;

@SuppressWarnings("all")
public class TestMultiSelect {
  @Test
  public void test() {
    final Store<String, String> store = Stores.<String, String>hashMap();
    final Operation<Success> _function = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.start(_asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function);
    final Operation<Success> _function_1 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.put("node/child1", "one", _asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_1);
    final Operation<Success> _function_2 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.put("node/child2", "two", _asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_2);
    final Operation<Success> _function_3 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.put("node/child3", "three", _asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_3);
    final Operation<Success> _function_4 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        final Value<Integer> count = new Value<Integer>(Integer.valueOf(0));
        final Closure<StoreEntry<String, String>> _function = new Closure<StoreEntry<String, String>>() {
          @Override
          public void apply(final StoreEntry<String, String> e) {
            Integer _get = count.get();
            int _plus = ((_get).intValue() + 1);
            count.set(Integer.valueOf(_plus));
            Integer _get_1 = count.get();
            boolean _equals = ((_get_1).intValue() == 3);
            if (_equals) {
              callback.onSuccess(Success.INSTANCE);
            }
          }
        };
        StoreOperation<String, String> _all = StoreOperations.<String, String>getAll("node/", _function);
        final Closure<Object> _function_1 = new Closure<Object>() {
          @Override
          public void apply(final Object it) {
          }
        };
        ValueCallback<Object> _embed = AsyncCommon.<Object>embed(callback, _function_1);
        store.performOperation(_all, _embed);
      }
    };
    Async.<Success>waitFor(_function_4);
    final Operation<Success> _function_5 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.stop(_asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_5);
  }
}

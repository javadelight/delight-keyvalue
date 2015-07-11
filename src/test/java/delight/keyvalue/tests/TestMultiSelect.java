package delight.keyvalue.tests;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Success;
import delight.keyvalue.Store;
import delight.keyvalue.Stores;
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
        store.stop(_asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_3);
  }
}

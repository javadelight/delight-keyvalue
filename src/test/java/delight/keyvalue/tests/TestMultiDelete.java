package delight.keyvalue.tests;

import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import delight.functional.Closure;
import delight.functional.Success;
import delight.keyvalue.Store;
import delight.keyvalue.Stores;
import delight.keyvalue.operations.StoreOperation;
import delight.keyvalue.operations.StoreOperations;
import org.junit.Test;

@SuppressWarnings("all")
public class TestMultiDelete {
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
    final Operation<Object> _function_4 = new Operation<Object>() {
      @Override
      public void apply(final ValueCallback<Object> callback) {
        StoreOperation<String, String> _count = StoreOperations.<String, String>count("");
        final Closure<Object> _function = new Closure<Object>() {
          @Override
          public void apply(final Object count) {
            if (((((Integer) count)).intValue() != 3)) {
              Exception _exception = new Exception("Not created correctly");
              callback.onFailure(_exception);
              return;
            }
            callback.onSuccess(Success.INSTANCE);
          }
        };
        ValueCallback<Object> _embed = AsyncCommon.<Object>embed(callback, _function);
        store.performOperation(_count, _embed);
      }
    };
    Async.<Object>waitFor(_function_4);
    final Operation<Object> _function_5 = new Operation<Object>() {
      @Override
      public void apply(final ValueCallback<Object> callback) {
        StoreOperation<String, String> _removeAll = StoreOperations.<String, String>removeAll("node/");
        final Closure<Object> _function = new Closure<Object>() {
          @Override
          public void apply(final Object it) {
            callback.onSuccess(Success.INSTANCE);
          }
        };
        ValueCallback<Object> _embed = AsyncCommon.<Object>embed(callback, _function);
        store.performOperation(_removeAll, _embed);
      }
    };
    Async.<Object>waitFor(_function_5);
    final Operation<Object> _function_6 = new Operation<Object>() {
      @Override
      public void apply(final ValueCallback<Object> callback) {
        StoreOperation<String, String> _count = StoreOperations.<String, String>count("");
        final Closure<Object> _function = new Closure<Object>() {
          @Override
          public void apply(final Object count) {
            if (((((Integer) count)).intValue() > 0)) {
              Exception _exception = new Exception("Not deleted correctly");
              callback.onFailure(_exception);
              return;
            }
            callback.onSuccess(Success.INSTANCE);
          }
        };
        ValueCallback<Object> _embed = AsyncCommon.<Object>embed(callback, _function);
        store.performOperation(_count, _embed);
      }
    };
    Async.<Object>waitFor(_function_6);
    final Operation<Success> _function_7 = new Operation<Success>() {
      @Override
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _asSimpleCallback = AsyncCommon.asSimpleCallback(callback);
        store.stop(_asSimpleCallback);
      }
    };
    Async.<Success>waitFor(_function_7);
  }
}

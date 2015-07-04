package de.mxro.async.map.tests;

import de.mxro.async.map.AsyncMap;
import de.mxro.async.map.AsyncMaps;
import de.mxro.concurrency.jre.JreConcurrency;
import de.mxro.fn.Success;
import delight.async.AsyncCommon;
import delight.async.Operation;
import delight.async.callbacks.SimpleCallback;
import delight.async.callbacks.ValueCallback;
import delight.async.jre.Async;
import org.junit.Test;

@SuppressWarnings("all")
public class TestThatAsynchronousPutMapCanBeStopped {
  @Test
  public void test() {
    JreConcurrency _jreConcurrency = new JreConcurrency();
    AsyncMap<String, String> _hashMap = AsyncMaps.<String, String>hashMap();
    final AsyncMap<String, String> map = AsyncMaps.<String, String>enforceAsynchronousPut(10, _jreConcurrency, _hashMap);
    final Operation<Success> _function = new Operation<Success>() {
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _wrap = AsyncCommon.wrap(callback);
        map.start(_wrap);
      }
    };
    Async.<Success>waitFor(_function);
    final Operation<Success> _function_1 = new Operation<Success>() {
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _wrap = AsyncCommon.wrap(callback);
        map.put("1", "one", _wrap);
      }
    };
    Async.<Success>waitFor(_function_1);
    final Operation<Success> _function_2 = new Operation<Success>() {
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _wrap = AsyncCommon.wrap(callback);
        map.put("2", "two", _wrap);
      }
    };
    Async.<Success>waitFor(_function_2);
    final Operation<Success> _function_3 = new Operation<Success>() {
      public void apply(final ValueCallback<Success> callback) {
        SimpleCallback _wrap = AsyncCommon.wrap(callback);
        map.stop(_wrap);
      }
    };
    Async.<Success>waitFor(_function_3);
  }
}

package delight.keyvalue.tests;

import delight.concurrency.jre.ConcurrencyJre;
import delight.keyvalue.Store;
import delight.keyvalue.Stores;
import org.junit.Assert;
import org.junit.Test;

@SuppressWarnings("all")
public class TestNonExistentKeysCache {
  @Test
  public void test() {
    final Store<String, Object> store = Stores.<Object>cacheNonExistingKeys(ConcurrencyJre.create(), Stores.<String, Object>hashMap());
    store.getSync("/a");
    store.getSync("/a");
    store.putSync("/a", "v1");
    Assert.assertEquals("v1", store.getSync("/a"));
    store.getSync("/a/b");
    store.getSync("/a/b");
    store.putSync("/a/b", "v2");
    store.putSync("/a/c", "v3");
    Assert.assertEquals("v2", store.getSync("/a/b"));
    store.removeSync("/a/b");
    Assert.assertEquals(null, store.getSync("/a/b"));
    Assert.assertEquals("v1", store.getSync("/a"));
    Assert.assertEquals("v3", store.getSync("/a/c"));
    store.removeSync("/a");
    Assert.assertEquals(null, store.getSync("/a"));
    Assert.assertEquals(null, store.getSync("/a/c"));
    store.getSync("b");
    store.putSync("b", "v4");
    Assert.assertEquals("v4", store.getSync("b"));
  }
  
  @Test
  public void testLongKeys() {
    final Store<String, String> store = Stores.<String>cacheNonExistingKeys(ConcurrencyJre.create(), Stores.<String, String>hashMap());
    store.getSync("test66/A1/B1/C1/D1/E1/F1");
    store.putSync("test66/A1/B1/C1/D1/E1/F1", "v1");
    store.getSync("test66/A1/B1/C1/D1/E1");
    store.putSync("test66/A1/B1/C1/D1/E1", "v2");
    Assert.assertEquals("v1", store.getSync("test66/A1/B1/C1/D1/E1/F1"));
    Assert.assertEquals("v2", store.getSync("test66/A1/B1/C1/D1/E1"));
  }
}

package delight.keyvalue.tests

import delight.concurrency.jre.ConcurrencyJre
import delight.keyvalue.Stores
import org.junit.Assert
import org.junit.Test

class TestNonExistentKeysCache {
	
	
	@Test
	def void test() {
		
		val store = Stores.cacheNonExistingKeys(ConcurrencyJre.create(), Stores.hashMap)
		
		
		store.getSync("/a");
		
		store.getSync("/a");
		
		store.putSync("/a", "v1")
		
		Assert.assertEquals("v1", store.getSync("/a"))
			
		store.getSync("/a/b")
		
		store.getSync("/a/b")
		
		store.putSync("/a/b", "v2")
		store.putSync("/a/c", "v3")
		
		Assert.assertEquals("v2", store.getSync("/a/b"))
		
		store.removeSync("/a/b")
		
		Assert.assertEquals(null, store.getSync("/a/b"))
		
		Assert.assertEquals("v1", store.getSync("/a"))
		Assert.assertEquals("v3", store.getSync("/a/c"))
		
		store.removeSync("/a")
		
		Assert.assertEquals(null, store.getSync("/a"))
		Assert.assertEquals(null, store.getSync("/a/c"))
		
		store.getSync("b")
		
		store.putSync("b", "v4")
		Assert.assertEquals("v4", store.getSync("b"))
				
	}

}

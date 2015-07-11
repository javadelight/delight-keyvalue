package delight.keyvalue.tests

import delight.async.AsyncCommon
import delight.async.jre.Async
import delight.keyvalue.Stores
import org.junit.Test

class TestMultiSelect {
	
	@Test
	def void test() {
		
		val store = Stores.hashMap
		
		Async.waitFor [ callback |
			store.start(AsyncCommon.asSimpleCallback(callback));
		]
	
		Async.waitFor [ callback |
			store.put("node/child1", "one", AsyncCommon.asSimpleCallback(callback));
		]

		Async.waitFor [ callback |
			store.put("node/child2", "two", AsyncCommon.asSimpleCallback(callback));
		]
		
	
		Async.waitFor [ callback |
			store.stop(AsyncCommon.asSimpleCallback(callback));
		]
		
	}
	
}
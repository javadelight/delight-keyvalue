package delight.keyvalue.tests

import delight.async.AsyncCommon
import delight.async.jre.Async
import delight.keyvalue.Stores
import org.junit.Test
import delight.keyvalue.operations.StoreOperations

class TestMultiSelect {
	
	@Test
	def void test() {
		
		val store = Stores.<String, String>hashMap
		
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
			
			store.performOperation(StoreOperations.getAll("node/", [ e |
				
			]))

		]
	
		Async.waitFor [ callback |
			store.stop(AsyncCommon.asSimpleCallback(callback));
		]
		
	}
	
}
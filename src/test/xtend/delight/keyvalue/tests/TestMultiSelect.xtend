package delight.keyvalue.tests

import delight.async.AsyncCommon
import delight.async.jre.Async
import delight.keyvalue.Stores
import org.junit.Test
import delight.keyvalue.operations.StoreOperations
import delight.functional.Success

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
				
				println(e.key())
			]), AsyncCommon.embed(callback, [
				callback.onSuccess(Success.INSTANCE)
			]));

		]
	
		Async.waitFor [ callback |
			store.stop(AsyncCommon.asSimpleCallback(callback));
		]
		
	}
	
}
package delight.keyvalue.tests

import delight.async.AsyncCommon
import delight.async.jre.Async
import delight.keyvalue.Stores
import org.junit.Test
import delight.keyvalue.operations.StoreOperations
import delight.functional.Success
import delight.async.Value

class TestMultiDelete {
	
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
			store.put("node/child3", "three", AsyncCommon.asSimpleCallback(callback));
		]
		
		
		Async.waitFor [ callback |
			
			
			store.performOperation(StoreOperations.removeAll("node/"), AsyncCommon.embed(callback, [
				callback.onSuccess(Success.INSTANCE)
			]));

		]
	
		Async.waitFor [ callback |
			store.stop(AsyncCommon.asSimpleCallback(callback));
		]
		
	}
	
}
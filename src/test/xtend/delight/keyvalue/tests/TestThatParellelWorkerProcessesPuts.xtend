package delight.keyvalue.tests

import delight.keyvalue.Stores
import delight.keyvalue.jre.AsyncMapsJre
import delight.async.AsyncCommon
import delight.async.jre.Async
import org.junit.Test

class TestThatParellelWorkerProcessesPuts {
	
	@Test
	def void test() {
		
		val map = AsyncMapsJre.divideWork(4, Stores.hashMap())
		
		
		Async.waitFor [ callback | 
			map.start(AsyncCommon.asSimpleCallback(callback))
		]
		
		
		Async.waitFor [ callback | 
			map.put("1", "one", AsyncCommon.asSimpleCallback(callback))
		]
		
		Async.waitFor [ callback | 
			map.put("1", "one", AsyncCommon.asSimpleCallback(callback))
		]
		
		Async.waitFor [ callback |
			map.stop(AsyncCommon.asSimpleCallback(callback))
		]
	
		
	}
	
	
}
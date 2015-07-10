package de.mxro.async.map.tests

import de.mxro.async.map.Stores
import de.mxro.async.map.jre.AsyncMapsJre
import delight.async.AsyncCommon
import delight.async.jre.Async
import org.junit.Test
import delight.async.callbacks.SimpleCallback

class TestThatParellelWorkerProcessesPuts {
	
	@Test
	def void test() {
		
		val map = AsyncMapsJre.divideWork(4, Stores.hashMap())
		
		
		Async.waitFor [ callback | 
			map.start(AsyncCommon.wrap(callback))
		]
		
		
		Async.waitFor [ callback | 
			map.put("1", "one", AsyncCommon.wrap(callback))
		]
		
		Async.waitFor [ callback | 
			map.put("1", "one", AsyncCommon.wrap(callback))
		]
		
		Async.waitFor [ callback |
			map.stop(AsyncCommon.wrap(callback))
		]
	
		
	}
	
	
}
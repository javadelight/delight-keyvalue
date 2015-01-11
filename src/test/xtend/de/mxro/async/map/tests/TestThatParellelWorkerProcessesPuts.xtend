package de.mxro.async.map.tests

import de.mxro.async.map.AsyncMaps
import de.mxro.async.map.jre.AsyncMapsJre
import org.junit.Test
import de.mxro.async.AsyncCommon
import static de.mxro.async.jre.Async.*

class TestThatParellelWorkerProcessesPuts {
	
	@Test
	def void test() {
		
		val map = AsyncMapsJre.divideWork(4, AsyncMaps.hashMap())
		
		waitFor [ callback | 
			map.start(AsyncCommon.wrap(callback))
		]
		
		
		waitFor [ callback | 
			map.put("1", "one", AsyncCommon.wrap(callback))
		]
		
		waitFor [ callback | 
			map.put("1", "one", AsyncCommon.wrap(callback))
		]
		
		waitFor [ callback |
			map.stop(AsyncCommon.wrap(callback))
		]
		
	}
	
	
}
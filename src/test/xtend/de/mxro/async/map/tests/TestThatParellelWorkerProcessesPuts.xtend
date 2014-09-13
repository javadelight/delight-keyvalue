package de.mxro.async.map.tests

import de.mxro.async.Async
import static de.mxro.async.jre.AsyncJre
import de.mxro.async.map.AsyncMaps
import de.mxro.async.map.jre.AsyncMapsJre
import org.junit.Test


class TestThatParellelWorkerProcessesPuts {
	
	@Test
	def void test() {
		
		val map = AsyncMapsJre.divideWork(4, AsyncMaps.hashMap())
		
		AsyncJre.waitFor [ callback | 
			map.put("1", "one", Async.wrap(callback))
		]
		
		AsyncJre.waitFor [ callback | 
			map.put("1", "one", Async.wrap(callback))
		]
		
		AsyncJre.waitFor [ callback |
			map.stop(Async.wrap(callback))
		]
		
	}
	
}
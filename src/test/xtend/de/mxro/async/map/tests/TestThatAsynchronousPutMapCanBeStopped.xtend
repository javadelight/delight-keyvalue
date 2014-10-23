package de.mxro.async.map.tests

import de.mxro.async.Async
import de.mxro.async.jre.AsyncJre
import de.mxro.async.map.AsyncMaps
import de.mxro.concurrency.jre.JreConcurrency
import org.junit.Test

class TestThatAsynchronousPutMapCanBeStopped {

	@Test
	def void test() {

		val map = AsyncMaps.enforceAsynchronousPut(10, new JreConcurrency(),
			AsyncMaps.<String, String>hashMap());

		AsyncJre.waitFor [ callback |
			map.start(Async.wrap(callback));
		]

		AsyncJre.waitFor [ callback |
			map.put("1", "one", Async.wrap(callback));
		]

		AsyncJre.waitFor [ callback |
			map.put("2", "two", Async.wrap(callback));
		]

		AsyncJre.waitFor [ callback |
			map.stop(Async.wrap(callback));
		]

	}
	

}

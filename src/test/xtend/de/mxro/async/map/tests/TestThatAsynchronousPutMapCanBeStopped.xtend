package de.mxro.async.map.tests

import delight.concurrency.jre.JreConcurrency
import delight.async.AsyncCommon
import delight.async.jre.Async
import org.junit.Test
import de.mxro.async.map.Stores

class TestThatAsynchronousPutMapCanBeStopped {

	@Test
	def void test() {

		val map = Stores.enforceAsynchronousPut(10, new JreConcurrency(),
			Stores.<String, String>hashMap());

		Async.waitFor [ callback |
			map.start(AsyncCommon.wrap(callback));
		]

		Async.waitFor [ callback |
			map.put("1", "one", AsyncCommon.wrap(callback));
		]

		Async.waitFor [ callback |
			map.put("2", "two", AsyncCommon.wrap(callback));
		]

		Async.waitFor [ callback |
			map.stop(AsyncCommon.wrap(callback));
		]

	}
	

}

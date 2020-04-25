/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.flink.runtime.util.log;

import org.apache.log4j.Priority;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.TimeUnit;

public class LogLevelWorkerTest {

	@Test
	public void testChangeLogLevel() throws InterruptedException {
		LogLevelWorker w0 = new LogLevelWorker("jobManager|root|trace|1|info");
		w0.changeLogLevel();

		Assert.assertEquals(1, LogLevelWorker.getWorkers().size());

		LogLevelWorker w1 = new LogLevelWorker("jobManager|org.apache.flink.runtime.util.log.LogLevelWorkerTest|debug|1|info");
		w1.changeLogLevel();
		Assert.assertEquals(2, LogLevelWorker.getWorkers().size());

		LogLevelWorker w2 = new LogLevelWorker("jobManager|org.apache.flink.runtime.util.log.LogLevelWorkerTest|debug|1|warn");
		// will remove w1 for this logger "root", so after 1 seconds, log level will set to "warn"
		w2.changeLogLevel();
		Assert.assertEquals(2, LogLevelWorker.getWorkers().size());

		Assert.assertTrue(w2.getLogger().isDebugEnabled());
		TimeUnit.MILLISECONDS.sleep(1100);
		Assert.assertFalse(w2.getLogger().isDebugEnabled());
		Assert.assertFalse(w2.getLogger().isInfoEnabled());
		Assert.assertTrue(w2.getLogger().isEnabledFor(Priority.WARN));
	}
}

